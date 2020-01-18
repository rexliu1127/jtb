package io.grx.modules.contact.service.impl;


import io.grx.common.utils.CharUtils;
import io.grx.modules.contact.service.UserService;
import io.grx.modules.contact.dao.UserDao;
import io.grx.modules.contact.entity.UserEntity;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service("userService")
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Override
	public UserEntity queryObject(Long userId){
		return userDao.queryObject(userId);
	}
	
	@Override
	public List<UserEntity> queryList(Map<String, Object> map){
		return userDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public UserEntity save(String mobile, String contact){
		UserEntity user = new UserEntity();
		user.setMobile(mobile);
		user.setContact(contact);
		user.setCreateTime(new Date());
//		user.setUpdateTime(user.getCreateTime());
		userDao.save(user);
		return user;
	}
	
	@Override
	@Transactional
	public void update(UserEntity user){
		user.setUpdateTime(new Date());
		if (StringUtils.isNotBlank(user.getContact())) {
			user.setContact(CharUtils.toValid3ByteUTF8String(user.getContact()));
		}
		userDao.update(user);
	}

	@Override
	@Transactional
	public void delete(Long userId){
		userDao.delete(userId);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		userDao.deleteBatch(userIds);
	}

	@Override
	public UserEntity queryByMobile(String mobile) {
		return userDao.queryByMobile(mobile);
	}
}
