package io.grx.modules.tx.service.impl;

import java.math.BigDecimal;

import io.grx.common.utils.Constant;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.tx.entity.*;
import io.grx.modules.tx.service.TxUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.exception.RRException;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.dao.TxUserBalanceDao;
import io.grx.modules.tx.dao.TxUserBalanceLogDao;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxUserBalanceService;



@Service("txUserBalanceService")
public class TxUserBalanceServiceImpl implements TxUserBalanceService {
	@Autowired
	private TxUserBalanceDao txUserBalanceDao;

	@Autowired
    private TxUserBalanceLogDao txUserBalanceLogDao;

	@Autowired
    private TxBaseService txBaseService;

	@Autowired
    private TxUserService txUserService;

	@Autowired
    private SysConfigService sysConfigService;

	@Override
	public TxUserBalanceEntity queryObject(Long userId){
		return txUserBalanceDao.queryObject(userId);
	}

	@Override
	public void addBalanceByDeletingTx(final TxBaseEntity txBaseEntity) {
		if ((txBaseEntity.getStatus() != TxStatus.NEW && txBaseEntity.getStatus() != TxStatus.REJECTED)
                || txBaseEntity.getFeeAmount().doubleValue() <= 0) {
		    return;
        }
        Long userId = txBaseEntity.getBorrowerUserId();

		TxUserBalanceEntity userBalanceEntity = txUserBalanceDao.getAndLock(userId);

		if (userBalanceEntity == null) {
			userBalanceEntity = new TxUserBalanceEntity();
			userBalanceEntity.setUserId(userId);
            userBalanceEntity.setBalance(BigDecimal.ZERO);

			txUserBalanceDao.save(userBalanceEntity);
		}

        userBalanceEntity.setBalance(userBalanceEntity.getBalance().add(txBaseEntity.getFeeAmount()));
		txUserBalanceDao.update(userBalanceEntity);

        TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
        userBalanceLogEntity.setUserId(userId);
        userBalanceLogEntity.setIncome(txBaseEntity.getFeeAmount());
        userBalanceLogEntity.setBalance(userBalanceEntity.getBalance());
        userBalanceLogEntity.setTxId(txBaseEntity.getTxId());

        txUserBalanceLogDao.save(userBalanceLogEntity);
	}

    @Override
    public boolean useBalanceByPayTx(final TxBaseEntity txBaseEntity) {
        TxUserEntity user = ShiroUtils.getTxUser();
        if (user == null) {
            throw new RRException("Invalid request");
        }

        if (user.getUserId() == null) {
            throw new RRException("Invalid request 2");
        }

        if (user.getUserId().equals(txBaseEntity.getLenderUserId())) {
            throw new RRException("出借人不用支付费用");
        }

        TxUserBalanceEntity userBalanceEntity = txUserBalanceDao.getAndLock(user.getUserId());

        if (userBalanceEntity == null
                || userBalanceEntity.getBalance().doubleValue() < txBaseEntity.getFeeAmount().doubleValue()) {
            return false;
        }

        userBalanceEntity.setBalance(userBalanceEntity.getBalance().subtract(txBaseEntity.getFeeAmount()));
        txUserBalanceDao.update(userBalanceEntity);

        TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
        userBalanceLogEntity.setUserId(user.getUserId());
        userBalanceLogEntity.setExpense(txBaseEntity.getFeeAmount());
        userBalanceLogEntity.setBalance(userBalanceEntity.getBalance());
        userBalanceLogEntity.setTxId(txBaseEntity.getTxId());

        txUserBalanceLogDao.save(userBalanceLogEntity);

        return true;
    }

    @Override
    @Transactional
    public boolean useBalanceByPayExtension(final TxExtensionEntity txExtensionEntity) {
        TxUserEntity user = ShiroUtils.getTxUser();

        if (txExtensionEntity.getFeeAmount() == null || txExtensionEntity.getFeeAmount().doubleValue() == 0) {
            return true;
        }

        TxBaseEntity txBaseEntity = txBaseService.queryObject(txExtensionEntity.getTxId());
        if (txBaseEntity.getLenderUserId().equals(user.getUserId())) {
            throw new RRException("出借人不用支付费用");
        }

        synchronized (user.getUserId().toString().intern()) {
            TxUserBalanceEntity userBalanceEntity = txUserBalanceDao.getAndLock(user.getUserId());

            if (userBalanceEntity == null || userBalanceEntity.getBalance().doubleValue() < txBaseEntity.getFeeAmount()
                    .doubleValue()) {
                return false;
            }

            BigDecimal newBalance = userBalanceEntity.getBalance().subtract(txExtensionEntity.getFeeAmount());
            if (newBalance.doubleValue() < 0) {
                return false;
            }

            userBalanceEntity.setBalance(newBalance);
            txUserBalanceDao.update(userBalanceEntity);


            TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
            userBalanceLogEntity.setUserId(user.getUserId());
            userBalanceLogEntity.setExpense(txExtensionEntity.getFeeAmount());
            userBalanceLogEntity.setBalance(userBalanceEntity.getBalance());
            userBalanceLogEntity.setExtensionId(txExtensionEntity.getExtensionId());

            txUserBalanceLogDao.save(userBalanceLogEntity);
        }

        return true;
    }

    @Override
    @Transactional
    public void addBalanceByReward(TxUserRewardEntity userRewardEntity) {
	    Long userId = userRewardEntity.getUserId();
        TxUserEntity user = txUserService.queryObject(userId);

        TxUserBalanceEntity userBalanceEntity = txUserBalanceDao.getAndLock(userId);

        if (userBalanceEntity == null) {
            userBalanceEntity = new TxUserBalanceEntity();
            userBalanceEntity.setUserId(userId);
            userBalanceEntity.setBalance(BigDecimal.ZERO);

            txUserBalanceDao.save(userBalanceEntity);
        }

        userBalanceEntity.setBalance(userBalanceEntity.getBalance().add(userRewardEntity.getReward()));
        txUserBalanceDao.update(userBalanceEntity);

        TxUserBalanceLogEntity userBalanceLogEntity = new TxUserBalanceLogEntity();
        userBalanceLogEntity.setUserId(userId);
        userBalanceLogEntity.setIncome(userRewardEntity.getReward());
        userBalanceLogEntity.setBalance(userBalanceEntity.getBalance());
        userBalanceLogEntity.setTxId(userRewardEntity.getTxId());
        userBalanceLogEntity.setExtensionId(userRewardEntity.getExtensionId());

        txUserBalanceLogDao.save(userBalanceLogEntity);
    }

    @Override
    public void updateBalanceByLog(TxUserBalanceLogEntity userBalanceLogEntity) {
        Long userId = userBalanceLogEntity.getUserId();

        TxUserEntity user = txUserService.queryObject(userId);

        TxUserBalanceEntity userBalanceEntity = txUserBalanceDao.getAndLock(userId);

        if (userBalanceEntity == null) {
            userBalanceEntity = new TxUserBalanceEntity();
            userBalanceEntity.setUserId(userId);
            userBalanceEntity.setBalance(BigDecimal.ZERO);

            txUserBalanceDao.save(userBalanceEntity);
        }

        if (userBalanceLogEntity.getIncome() != null && userBalanceLogEntity.getIncome().doubleValue() > 0) {
            userBalanceEntity.setBalance(userBalanceEntity.getBalance().add(userBalanceLogEntity.getIncome()));
            txUserBalanceDao.update(userBalanceEntity);
        } else if (userBalanceLogEntity.getExpense() != null && userBalanceLogEntity.getExpense().doubleValue() > 0) {
            BigDecimal newBalance = userBalanceEntity.getBalance().subtract(userBalanceLogEntity.getExpense());
            if (newBalance.doubleValue() < 0) {
                throw new RRException("余额不足");
            }

            userBalanceEntity.setBalance(newBalance);
            txUserBalanceDao.update(userBalanceEntity);
        } else {
            throw new RRException("错误操作");
        }

        userBalanceLogEntity.setBalance(userBalanceEntity.getBalance());
        txUserBalanceLogDao.save(userBalanceLogEntity);
    }

    @Override
    public BigDecimal getWithdrawalFeeRate() {
        String s = sysConfigService.getValue(Constant.KEY_WITHDRAWAL_FEE_RATE);

        if (StringUtils.isBlank(s)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(s);
        }
    }

    @Override
    public BigDecimal getWithdrawalMinFee() {
        String s = sysConfigService.getValue(Constant.KEY_WITHDRAWAL_MIN_FEE);

        if (StringUtils.isBlank(s)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(s);
        }
    }

    @Override
    public BigDecimal getWithdrawalMaxFee() {
        String s = sysConfigService.getValue(Constant.KEY_WITHDRAWAL_MAX_FEE);

        if (StringUtils.isBlank(s)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(s);
        }
    }

    @Override
    public BigDecimal getWithdrawalMinAmount() {
        String s = sysConfigService.getValue(Constant.KEY_WITHDRAWAL_MIN_AMOUNT);

        if (StringUtils.isBlank(s)) {
            return BigDecimal.ZERO;
        } else {
            return new BigDecimal(s);
        }
    }

}
