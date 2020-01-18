package io.grx.modules.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dao.AuthMachineContactDao;
import io.grx.modules.auth.entity.AuthMachineContactEntity;
import io.grx.modules.auth.service.AuthMachineContactService;
import org.springframework.transaction.annotation.Transactional;


@Service("authMachineContactService")
public class AuthMachineContactServiceImpl implements AuthMachineContactService {
	@Autowired
	private AuthMachineContactDao authMachineContactDao;
	
	@Override
	public AuthMachineContactEntity queryObject(String machineId){
		return authMachineContactDao.queryObject(machineId);
	}
	
	@Override
	public List<AuthMachineContactEntity> queryList(Map<String, Object> map){
		return authMachineContactDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authMachineContactDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthMachineContactEntity authMachineContact){
		authMachineContactDao.save(authMachineContact);
	}
	
	@Override
	@Transactional
	public void update(AuthMachineContactEntity authMachineContact){
		authMachineContactDao.update(authMachineContact);
	}
	
	@Override
	@Transactional
	public void delete(String machineId){
		authMachineContactDao.delete(machineId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(String[] machineIds){
		authMachineContactDao.deleteBatch(machineIds);
	}
	
}
