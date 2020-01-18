package io.grx.modules.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dao.AuthRequestHistoryDao;
import io.grx.modules.auth.entity.AuthRequestHistoryEntity;
import io.grx.modules.auth.service.AuthRequestHistoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("authRequestHistoryService")
public class AuthRequestHistoryServiceImpl implements AuthRequestHistoryService {
	@Autowired
	private AuthRequestHistoryDao authRequestHistoryDao;
	
	@Override
	public AuthRequestHistoryEntity queryObject(Long id){
		return authRequestHistoryDao.queryObject(id);
	}
	
	@Override
	public List<AuthRequestHistoryEntity> queryList(Map<String, Object> map){
		return authRequestHistoryDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authRequestHistoryDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthRequestHistoryEntity authRequestHistory){
		authRequestHistoryDao.save(authRequestHistory);
	}
	
	@Override
	@Transactional
	public void update(AuthRequestHistoryEntity authRequestHistory){
		authRequestHistoryDao.update(authRequestHistory);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		authRequestHistoryDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		authRequestHistoryDao.deleteBatch(ids);
	}

	@Override
	public List<AuthRequestHistoryEntity> queryHistories(Long requestId) {
		return authRequestHistoryDao.queryHistories(requestId);
	}

}
