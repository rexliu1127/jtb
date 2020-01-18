package io.grx.modules.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.auth.dao.AuthMissingRecordDao;
import io.grx.modules.auth.entity.AuthMissingRecordEntity;
import io.grx.modules.auth.service.AuthMissingRecordService;



@Service("authMissingRecordService")
public class AuthMissingRecordServiceImpl implements AuthMissingRecordService {
	@Autowired
	private AuthMissingRecordDao authMissingRecordDao;
	
	@Override
	public AuthMissingRecordEntity queryObject(Long id){
		return authMissingRecordDao.queryObject(id);
	}
	
	@Override
	public List<AuthMissingRecordEntity> queryList(Map<String, Object> map){
		return authMissingRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authMissingRecordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthMissingRecordEntity authMissingRecord){
		authMissingRecordDao.save(authMissingRecord);
	}
	
	@Override
	@Transactional
	public void update(AuthMissingRecordEntity authMissingRecord){
		authMissingRecordDao.update(authMissingRecord);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		authMissingRecordDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		authMissingRecordDao.deleteBatch(ids);
	}

	@Override
	public List<AuthMissingRecordEntity> queryMissingRecords() {
		return authMissingRecordDao.queryMissingRecords();
	}

}
