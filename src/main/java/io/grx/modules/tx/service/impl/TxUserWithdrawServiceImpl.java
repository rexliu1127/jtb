package io.grx.modules.tx.service.impl;

import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.tx.dto.TxUserWithdrawalVO;
import io.grx.modules.tx.entity.TxUserBalanceLogEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.WithdrawalStatus;
import io.grx.modules.tx.service.TxUserBalanceService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.pay.service.impl.AllinPayTranxServiceImpl;
import io.grx.pay.util.AllinPayTranxResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.tx.dao.TxUserWithdrawDao;
import io.grx.modules.tx.entity.TxUserWithdrawEntity;
import io.grx.modules.tx.service.TxUserWithdrawService;
import org.springframework.transaction.annotation.Transactional;


@Service("txUserWithdrawService")
public class TxUserWithdrawServiceImpl implements TxUserWithdrawService {
	@Autowired
	private TxUserWithdrawDao txUserWithdrawDao;

    @Autowired
    private TxUserBalanceService txUserBalanceService;

    @Autowired
    private TxUserService txUserService;

    private AllinPayTranxServiceImpl tranxService;

    public TxUserWithdrawServiceImpl() {
        tranxService = new AllinPayTranxServiceImpl();
    }

	@Override
	public TxUserWithdrawEntity queryObject(Long id){
		return txUserWithdrawDao.queryObject(id);
	}
	
	@Override
	public List<TxUserWithdrawEntity> queryList(Map<String, Object> map){
		return txUserWithdrawDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txUserWithdrawDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxUserWithdrawEntity txUserWithdraw){
		txUserWithdrawDao.save(txUserWithdraw);
	}
	
	@Override
	@Transactional
	public void update(TxUserWithdrawEntity txUserWithdraw){
		txUserWithdrawDao.update(txUserWithdraw);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		txUserWithdrawDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		txUserWithdrawDao.deleteBatch(ids);
	}

    @Override
    @Transactional
    public void withdraw(Long userId, BigDecimal amount, BigDecimal feeAmount) {


        TxUserWithdrawEntity userWithdrawEntity = new TxUserWithdrawEntity();
        userWithdrawEntity.setUserId(userId);
        userWithdrawEntity.setAmount(new BigDecimal(String.valueOf(amount)));
        userWithdrawEntity.setFeeAmount(feeAmount);
        userWithdrawEntity.setCreateTime(new Date());
        save(userWithdrawEntity);

        TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
        userBalanceLogEntity.setUserId(userId);
        userBalanceLogEntity.setExpense(userWithdrawEntity.getAmount());

        txUserBalanceService.updateBalanceByLog(userBalanceLogEntity);

    }

    @Override
    public double sumUserWithdrawal(Long userId) {
        return txUserWithdrawDao.sumUserWithdrawal(userId);
    }

    @Override
    public List<TxUserWithdrawalVO> queryAdminList(Map<String, Object> params) {
        return txUserWithdrawDao.queryAdminList(params);
    }

	@Override
	public R updateStatus(Long id, WithdrawalStatus status) {
		TxUserWithdrawEntity txUserWithdrawEntity = queryObject(id);
		if (txUserWithdrawEntity == null || !StringUtils.equalsIgnoreCase(txUserWithdrawEntity.getMerchantNo(), ShiroUtils.getMerchantNo())) {
			return R.error("你没有此权限");
		}

		if (txUserWithdrawEntity.getStatus() != WithdrawalStatus.NEW) {
			return R.error("该申请已被审核过");
		}

		txUserWithdrawEntity.setApprovalUserId(ShiroUtils.getUserId());
		txUserWithdrawEntity.setApprovalTime(new Date());
		txUserWithdrawEntity.setStatus(status);
		update(txUserWithdrawEntity);

        if (status == WithdrawalStatus.REJECTED) {
            TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
            userBalanceLogEntity.setUserId(txUserWithdrawEntity.getUserId());
            userBalanceLogEntity.setIncome(txUserWithdrawEntity.getAmount());

            txUserBalanceService.updateBalanceByLog(userBalanceLogEntity);
        } else if (status == WithdrawalStatus.APPROVED) {
        	// 提现
            TxUserEntity txUserEntity = txUserService.queryObject(txUserWithdrawEntity.getUserId());

            long actualAmount = (long) txUserWithdrawEntity.getAmount().subtract(txUserWithdrawEntity.getFeeAmount()).doubleValue() * 100;
            if (actualAmount > 0) {
                String reqSn = UUIDGenerator.getUUID();
                txUserWithdrawEntity.setTranxId(reqSn);
                AllinPayTranxResult result = tranxService.singleTranxPay(reqSn, txUserEntity.getName(), txUserEntity.getBankAccount(), "", actualAmount);
                if (result.isSuccess()) {
                    txUserWithdrawEntity.setStatus(WithdrawalStatus.COMPLETED);
                    txUserWithdrawEntity.setCompleteTime(new Date());
                    txUserWithdrawEntity.setTranxCode(result.getReturnCode());
                } else if (result.isProcessing()) {
                    txUserWithdrawEntity.setStatus(WithdrawalStatus.PROCESSING);
                    txUserWithdrawEntity.setTranxCode(result.getReturnCode());
                    txUserWithdrawEntity.setTranxMessage(result.getResultMessage());
                } else {
                    txUserWithdrawEntity.setStatus(WithdrawalStatus.FAILED);
                    txUserWithdrawEntity.setTranxCode(result.getReturnCode());
                    txUserWithdrawEntity.setTranxMessage(result.getResultMessage());
                }
            }

            update(txUserWithdrawEntity);
		}

        return R.ok();
	}

}
