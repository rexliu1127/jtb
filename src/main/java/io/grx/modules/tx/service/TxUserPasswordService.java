package io.grx.modules.tx.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.entity.TxUserPasswordEntity;

/**
 * 交易用户密码
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 10:41:32
 */
public interface TxUserPasswordService {
	
	TxUserPasswordEntity queryObject(Long userId);
	
	List<TxUserPasswordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TxUserPasswordEntity txUserPassword);
	
	void update(TxUserPasswordEntity txUserPassword);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	boolean isPasswordMatched(Long userId, String password);

	void saveOrUpdatePassword(Long userId, String password);
}
