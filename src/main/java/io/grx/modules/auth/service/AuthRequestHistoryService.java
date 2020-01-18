package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthRequestHistoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 申请单历史
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
public interface AuthRequestHistoryService {
	
	AuthRequestHistoryEntity queryObject(Long id);
	
	List<AuthRequestHistoryEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthRequestHistoryEntity authRequestHistory);
	
	void update(AuthRequestHistoryEntity authRequestHistory);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<AuthRequestHistoryEntity> queryHistories(Long requestId);
}
