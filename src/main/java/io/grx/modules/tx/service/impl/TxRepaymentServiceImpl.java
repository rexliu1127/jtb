package io.grx.modules.tx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.exception.RRException;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.tx.dao.TxRepaymentDao;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.RepaymentStatus;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxOverdueRecordService;
import io.grx.modules.tx.service.TxRepayPlanService;
import io.grx.modules.tx.service.TxRepaymentService;



@Service("txRepaymentService")
public class TxRepaymentServiceImpl implements TxRepaymentService {
	@Autowired
	private TxRepaymentDao txRepaymentDao;

	@Autowired
    private TxBaseService txBaseService;

	@Autowired
	private TxOverdueRecordService txOverdueRecordService;

	@Autowired
	private TxRepayPlanService txRepayPlanService;
	
	@Override
	public TxRepaymentEntity queryObject(Long repaymentId){
		return txRepaymentDao.queryObject(repaymentId);
	}
	
	@Override
	public List<TxRepaymentEntity> queryList(Map<String, Object> map){
		return txRepaymentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txRepaymentDao.queryTotal(map);
	}
	
	@Override
    @Transactional
	public void save(TxRepaymentEntity txRepayment){
		txRepaymentDao.save(txRepayment);
	}
	
	@Override
    @Transactional
	public void update(TxRepaymentEntity txRepayment){
		txRepaymentDao.update(txRepayment);
	}
	
	@Override
    @Transactional
	public void delete(Long repaymentId){
		txRepaymentDao.delete(repaymentId);
	}
	
	@Override
    @Transactional
	public void deleteBatch(Long[] repaymentIds){
		txRepaymentDao.deleteBatch(repaymentIds);
	}

	@Override
	public TxRepaymentEntity getLastRepaymentByTx(final Long txId) {
		return txRepaymentDao.getLastRepaymentByTx(txId);
	}

	@Override
    @Transactional
	public void updateStatus(final TxRepaymentEntity repaymentEntity, RepaymentStatus status) {

	    if (repaymentEntity.getStatus() != RepaymentStatus.NEW) {
	        throw new RRException("还款状态非待确定");
        }

        TxBaseEntity baseEntity = txBaseService.queryObject(repaymentEntity.getTxId());
	    if (baseEntity == null) {
            throw new RRException("非法请求1");
        }
        TxUserEntity userEntity = ShiroUtils.getTxUser();
	    if (userEntity.getUserId().equals(baseEntity.getBorrowerUserId())) {
	        if (status != RepaymentStatus.CANCELLED) {
                throw new RRException("非法请求2");
            }
        } else if (userEntity.getUserId().equals(baseEntity.getLenderUserId())) {
            if (status != RepaymentStatus.CONFIRMED && status != RepaymentStatus.REJECTED) {
                throw new RRException("非法请求3");
            }
        }

		repaymentEntity.setStatus(status);
        repaymentEntity.setUpdateTime(new Date());

		if (status == RepaymentStatus.CONFIRMED) {
            baseEntity.setStatus(TxStatus.COMPLETED);
            baseEntity.setRepayDate(new Date());
            baseEntity.setUpdateTime(new Date());
            txBaseService.update(baseEntity);

            txRepayPlanService.updateByRepayment(repaymentEntity);
//			TxOverdueRecordEntity txOverdueRecordEntity = txOverdueRecordService.queryLatestByTxId(baseEntity.getTxId
//					());
//			if (txOverdueRecordEntity != null && txOverdueRecordEntity.getOverdueEndDate() == null) {
//				txOverdueRecordEntity.setOverdueEndDate(new Date());
//				txOverdueRecordService.update(txOverdueRecordEntity);
//			}
        }

        update(repaymentEntity);
	}

    @Override
    public List<TxRepaymentEntity> queryListByLender(final Long userId) {
        return txRepaymentDao.queryListByLender(userId, null);
    }

	@Override
	public List<TxRepaymentEntity> queryListByLender(final Long userId, String userName) {
		return txRepaymentDao.queryListByLender(userId, userName);
	}

    @Override
    public List<TxRepaymentEntity> queryListByBorrower(final Long userId) {
        return txRepaymentDao.queryListByBorrower(userId, null);
    }

    @Override
    public List<TxRepaymentEntity> queryListByBorrower(final Long userId, String userName) {
        return txRepaymentDao.queryListByBorrower(userId, userName);
    }

	@Override
	public List<TxRepaymentEntity> queryListByTx(final Long txId) {
		return txRepaymentDao.queryListByTx(txId);
	}

}
