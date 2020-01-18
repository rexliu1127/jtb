package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthUserContactEntity;

/**
 * 通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-20 22:35:22
 */
public interface AuthUserContactService {
	
	AuthUserContactEntity queryObject(Long userId);
	
	List<AuthUserContactEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthUserContactEntity authUserContact);
	
	void update(AuthUserContactEntity authUserContact);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	Map<String, String> getContacts(Long userId);

	void saveOrUpdate(Long userId, Map<String, String> contacts);
}
