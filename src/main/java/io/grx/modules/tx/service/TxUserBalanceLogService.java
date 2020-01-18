package io.grx.modules.tx.service;

import io.grx.modules.tx.entity.TxUserBalanceLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户余额记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
public interface TxUserBalanceLogService {
	
	TxUserBalanceLogEntity queryObject(Long id);
	
	List<TxUserBalanceLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserBalanceLogEntity txUserBalanceLog);
	
	void update(TxUserBalanceLogEntity txUserBalanceLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
