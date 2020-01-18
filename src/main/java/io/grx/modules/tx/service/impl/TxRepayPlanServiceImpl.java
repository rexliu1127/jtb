package io.grx.modules.tx.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.tx.dao.TxBaseDao;
import io.grx.modules.tx.dao.TxRepayPlanDao;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxRepayPlanEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.service.TxRepayPlanService;



@Service("txRepayPlanService")
public class TxRepayPlanServiceImpl implements TxRepayPlanService {
	@Autowired
	private TxRepayPlanDao txRepayPlanDao;

	@Autowired
    private TxBaseDao txBaseDao;
	
	@Override
	public TxRepayPlanEntity queryObject(Long planId){
		return txRepayPlanDao.queryObject(planId);
	}
	
	@Override
	public List<TxRepayPlanEntity> queryList(Map<String, Object> map){
		return txRepayPlanDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return txRepayPlanDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TxRepayPlanEntity txRepayPlan){
		txRepayPlanDao.save(txRepayPlan);
	}
	
	@Override
	@Transactional
	public void update(TxRepayPlanEntity txRepayPlan){
		txRepayPlanDao.update(txRepayPlan);
	}
	
	@Override
	@Transactional
	public void delete(Long planId){
		txRepayPlanDao.delete(planId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] planIds){
		txRepayPlanDao.deleteBatch(planIds);
	}

    @Override
    public TxRepayPlanEntity getLatestByTx(final Long txId) {
        return txRepayPlanDao.queryLatestByTx(txId);
    }

    @Override
    @Transactional
    public void updateByRepayment(final TxRepaymentEntity repaymentEntity) {
        TxRepayPlanEntity planEntity = getLatestByTx(repaymentEntity.getTxId());

        if (planEntity == null) {
            TxBaseEntity txBaseEntity = txBaseDao.queryObject(repaymentEntity.getTxId());
            planEntity = createRepayPlanOnly(txBaseEntity);
        }

        planEntity.setActualAmount(planEntity.getPlannedAmount());
        planEntity.setActualInterest(planEntity.getPlannedInterest());
        planEntity.setUpdateTime(new Date());
        planEntity.setRepayDate(repaymentEntity.getUpdateTime());

        if (planEntity.getPlanId() == null) {
            save(planEntity);
        } else {
            update(planEntity);
        }
    }

    @Override
    @Transactional
    public void createRepayPlanForTx(TxBaseEntity entity) {
        TxRepayPlanEntity planEntity = createRepayPlanOnly(entity);
        save(planEntity);
    }

    @Override
    public void createRepayPlanByExtension(final TxBaseEntity entity, Date previousRepayDate) {

        TxRepayPlanEntity planEntity = getLatestByTx(entity.getTxId());
        if (planEntity != null && planEntity.getRepayDate() == null) {
            planEntity.setRepayDate(previousRepayDate);
            planEntity.setActualAmount(planEntity.getPlannedAmount() - entity.getOutstandingAmount());
            planEntity.setActualInterest(planEntity.getPlannedInterest());
            planEntity.setUpdateTime(new Date());
            update(planEntity);
        }

        TxBaseEntity tempTx = new TxBaseEntity();
        tempTx.setTxId(entity.getTxId());
        tempTx.setAmount(entity.getOutstandingAmount());
        tempTx.setInterest(entity.getOutstandingInterest());
        tempTx.setBeginDate(previousRepayDate);
        tempTx.setEndDate(entity.getEndDate());

        planEntity = createRepayPlanOnly(tempTx);
        save(planEntity);

    }

    private TxRepayPlanEntity createRepayPlanOnly(TxBaseEntity entity) {
        TxRepayPlanEntity planEntity = new TxRepayPlanEntity();
        planEntity.setTxId(entity.getTxId());
        planEntity.setPlannedAmount(entity.getAmount());
        planEntity.setBeginDate(entity.getBeginDate());
        planEntity.setEndDate(entity.getEndDate());
        planEntity.setPlannedInterest(entity.getInterest());
        return planEntity;
    }
}
