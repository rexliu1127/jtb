package io.grx.modules.contact.service;


import io.grx.modules.contact.entity.UserEntity;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

/**
 * 用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:06
 */
public interface UserService {

	UserEntity queryObject(Long userId);
	
	List<UserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	UserEntity save(String mobile, String contact);
	
	void update(UserEntity user);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	UserEntity queryByMobile(String mobile);

}
