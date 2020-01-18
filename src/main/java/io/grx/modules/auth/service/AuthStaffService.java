package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthStaffEntity;

import java.util.List;
import java.util.Map;

/**
 * 客服人员
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-01 00:36:13
 */
public interface AuthStaffService {
	
	AuthStaffEntity queryObject(Long staffId);
	
	List<AuthStaffEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthStaffEntity authStaff);
	
	void update(AuthStaffEntity authStaff);
	
	void delete(Long staffId);
	
	void deleteBatch(Long[] staffIds);

	List<AuthStaffEntity> queryByProcessorId(Long processorId);
}
