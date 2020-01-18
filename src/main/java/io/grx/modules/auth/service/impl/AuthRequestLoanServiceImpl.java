package io.grx.modules.auth.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.grx.modules.auth.dao.AuthRequestLoanDao;
import io.grx.modules.auth.entity.AuthRequestLoanEntity;
import io.grx.modules.auth.service.AuthRequestLoanService;

@Service("authRequestLoanService")
public class AuthRequestLoanServiceImpl implements AuthRequestLoanService{

	@Autowired
	private AuthRequestLoanDao authRequestLoanDao;
	
	@Override
	public AuthRequestLoanEntity queryObject(Long requestId) {
		return this.authRequestLoanDao.queryObject(requestId);
	}

	@Override
	public void save(AuthRequestLoanEntity authRequest) {
		this.authRequestLoanDao.save(authRequest);
	}

	@Override
	public void update(AuthRequestLoanEntity authRequest) {
		this.authRequestLoanDao.update(authRequest);
	}

	@Override
	public void delete(Long requestId) {
		this.authRequestLoanDao.delete(requestId);
	}

	@Override
	public void deleteBatch(Long[] requestIds) {
		this.authRequestLoanDao.deleteBatch(requestIds);
	}
	
	public List<AuthRequestLoanEntity> queryApplyList(Map<String,Object> param){
		return this.authRequestLoanDao.queryApplyList(param);
	}

	@Override
	public AuthRequestLoanEntity queryLastApply(Map<String, Object> param) {
		return this.authRequestLoanDao.queryLastApply(param);
	}

}
