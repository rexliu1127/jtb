package io.grx.modules.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dao.AuthStaffDao;
import io.grx.modules.auth.entity.AuthStaffEntity;
import io.grx.modules.auth.service.AuthStaffService;



@Service("authStaffService")
public class AuthStaffServiceImpl implements AuthStaffService {
	@Autowired
	private AuthStaffDao authStaffDao;
	
	@Override
	public AuthStaffEntity queryObject(Long staffId){
		return authStaffDao.queryObject(staffId);
	}
	
	@Override
	public List<AuthStaffEntity> queryList(Map<String, Object> map){
		return authStaffDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authStaffDao.queryTotal(map);
	}
	
	@Override
	public void save(AuthStaffEntity authStaff){
		authStaffDao.save(authStaff);
	}
	
	@Override
	public void update(AuthStaffEntity authStaff){
		authStaffDao.update(authStaff);
	}
	
	@Override
	public void delete(Long staffId){
		authStaffDao.delete(staffId);
	}
	
	@Override
	public void deleteBatch(Long[] staffIds){
		authStaffDao.deleteBatch(staffIds);
	}

	@Override
	public List<AuthStaffEntity> queryByProcessorId(Long processorId) {
		return authStaffDao.queryByProcessorId(processorId);
	}

}
