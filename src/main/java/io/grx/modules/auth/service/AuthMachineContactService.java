package io.grx.modules.auth.service;

import io.grx.modules.auth.entity.AuthMachineContactEntity;

import java.util.List;
import java.util.Map;

/**
 * APP机器通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-10-22 14:27:10
 */
public interface AuthMachineContactService {
	
	AuthMachineContactEntity queryObject(String machineId);
	
	List<AuthMachineContactEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthMachineContactEntity authMachineContact);
	
	void update(AuthMachineContactEntity authMachineContact);
	
	void delete(String machineId);
	
	void deleteBatch(String[] machineIds);
}
