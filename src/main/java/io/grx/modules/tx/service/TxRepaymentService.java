package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxRepaymentEntity;
import io.grx.modules.tx.enums.RepaymentStatus;

/**
 * 还款记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
public interface TxRepaymentService {
	
	TxRepaymentEntity queryObject(Long repaymentId);
	
	List<TxRepaymentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxRepaymentEntity txRepayment);
	
	void update(TxRepaymentEntity txRepayment);
	
	void delete(Long repaymentId);
	
	void deleteBatch(Long[] repaymentIds);

	TxRepaymentEntity getLastRepaymentByTx(Long txId);

	void updateStatus(TxRepaymentEntity repaymentEntity, RepaymentStatus status);

	List<TxRepaymentEntity> queryListByLender(Long userId);

	List<TxRepaymentEntity> queryListByLender(Long userId, String userName);

    List<TxRepaymentEntity> queryListByBorrower(Long userId);

	List<TxRepaymentEntity> queryListByBorrower(Long userId, String userName);

	List<TxRepaymentEntity> queryListByTx(Long txId);
}
