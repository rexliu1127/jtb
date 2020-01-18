package io.grx.modules.tx.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxRepayPlanEntity;
import io.grx.modules.tx.entity.TxRepaymentEntity;

/**
 * 还款计划
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-25 22:23:48
 */
public interface TxRepayPlanService {
	
	TxRepayPlanEntity queryObject(Long planId);
	
	List<TxRepayPlanEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxRepayPlanEntity txRepayPlan);
	
	void update(TxRepayPlanEntity txRepayPlan);
	
	void delete(Long planId);
	
	void deleteBatch(Long[] planIds);

	TxRepayPlanEntity getLatestByTx(Long txId);

	void updateByRepayment(TxRepaymentEntity repaymentEntity);

    void createRepayPlanForTx(TxBaseEntity entity);

    void createRepayPlanByExtension(TxBaseEntity entity, Date previousRepayDate);
}
