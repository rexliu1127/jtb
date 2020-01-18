package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxComplainEntity;
import io.grx.modules.tx.entity.TxComplainResultEntity;

/**
 * 借条申诉
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
public interface TxComplainService {
	
	TxComplainEntity queryObject(Long complainId);
	
	List<TxComplainEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxComplainEntity txComplain);
	
	void update(TxComplainEntity txComplain);
	
	void delete(Long complainId);
	
	void deleteBatch(Long[] complainIds);

	TxComplainEntity getLatestByTxId(Long txId);
}
