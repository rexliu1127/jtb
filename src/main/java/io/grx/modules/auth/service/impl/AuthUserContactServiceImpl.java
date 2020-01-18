package io.grx.modules.auth.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.CharUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.contact.entity.UserEntity;
import io.grx.modules.contact.service.UserService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import io.grx.modules.auth.dao.AuthUserContactDao;
import io.grx.modules.auth.entity.AuthUserContactEntity;
import io.grx.modules.auth.service.AuthUserContactService;



@Service("authUserContactService")
public class AuthUserContactServiceImpl implements AuthUserContactService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthUserContactDao authUserContactDao;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private UserService userService;
	
	@Override
	public AuthUserContactEntity queryObject(Long userId){
		return authUserContactDao.queryObject(userId);
	}
	
	@Override
	public List<AuthUserContactEntity> queryList(Map<String, Object> map){
		return authUserContactDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authUserContactDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthUserContactEntity authUserContact){
		authUserContactDao.save(authUserContact);
	}
	
	@Override
	@Transactional
	public void update(AuthUserContactEntity authUserContact){
		authUserContactDao.update(authUserContact);
	}

	@Override
	@Transactional
	public void delete(Long userId){
		authUserContactDao.delete(userId);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		authUserContactDao.deleteBatch(userIds);
	}

	@Override
	public Map<String, String> getContacts(final Long userId) {
		String contact = "";
		AuthUserEntity userEntity = authUserService.queryObject(userId);
		UserEntity contactUser = userService.queryByMobile(userEntity.getMobile());
		if (contactUser != null) {
			contact = contactUser.getContact();
		}
		if (StringUtils.isBlank(contact)) {
			AuthUserContactEntity contactEntity = queryObject(userId);

			if (contactEntity != null) {
				contact = contactEntity.getContact();
			}
		}
		try {
			if (StringUtils.isNotBlank(contact)) {
				return new Gson().fromJson(contact, HashMap.class);
			}
		} catch (Exception e) {
			logger.error("failed to convert contact: " + contact);
		}
		return MapUtils.EMPTY_MAP;
	}

	@Override
	public void saveOrUpdate(final Long userId, final Map<String, String> contacts) {
		if (contacts == null || contacts.size() == 0) {
			return;
		}

		String contactsJson = new Gson().toJson(contacts);
		contactsJson = CharUtils.toValid3ByteUTF8String(contactsJson);

		AuthUserContactEntity contactEntity = queryObject(userId);
		if (contactEntity != null) {
			contactEntity.setContact(contactsJson);
			update(contactEntity);
		} else {
			contactEntity = new AuthUserContactEntity();
			contactEntity.setUserId(userId);
			contactEntity.setContact(contactsJson);
			save(contactEntity);
		}
	}

}
