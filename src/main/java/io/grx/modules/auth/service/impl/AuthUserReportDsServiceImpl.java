package io.grx.modules.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.auth.dao.AuthUserReportDsDao;
import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthUserReportDsService;



@Service("authUserReportDsService")
public class AuthUserReportDsServiceImpl implements AuthUserReportDsService {
	@Autowired
	private AuthUserReportDsDao authUserReportDsDao;
	
	@Override
	public AuthUserReportDsEntity queryObject(Long id){
		return authUserReportDsDao.queryObject(id);
	}
	
	@Override
	public List<AuthUserReportDsEntity> queryList(Map<String, Object> map){
		return authUserReportDsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return authUserReportDsDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthUserReportDsEntity authUserReportDs){
		authUserReportDsDao.save(authUserReportDs);
	}
	
	@Override
	@Transactional
	public void update(AuthUserReportDsEntity authUserReportDs){
		authUserReportDsDao.update(authUserReportDs);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		authUserReportDsDao.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		authUserReportDsDao.deleteBatch(ids);
	}

	@Override
	public AuthUserReportDsEntity queryByTaskId(final String taskId) {
		return authUserReportDsDao.queryByTaskId(taskId);
	}

	@Override
	public List<AuthUserReportDsEntity> queryByStatuses(final Long userId, final VerifyStatus... verifyStatuses) {
		return authUserReportDsDao.queryByStatuses(userId, verifyStatuses);
	}

}
