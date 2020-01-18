package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthMissingRecordEntity;

/**
 * 丢失记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-13 01:25:30
 */
public interface AuthMissingRecordService {
	
	AuthMissingRecordEntity queryObject(Long id);
	
	List<AuthMissingRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthMissingRecordEntity authMissingRecord);
	
	void update(AuthMissingRecordEntity authMissingRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<AuthMissingRecordEntity> queryMissingRecords();
}
