package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthRequestLoanEntity;

/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
public interface AuthRequestLoanService {
	
	AuthRequestLoanEntity queryObject(Long requestId);
	
	void save(AuthRequestLoanEntity authRequest);
	
	void update(AuthRequestLoanEntity authRequest);
	
	void delete(Long requestId);
	
	void deleteBatch(Long[] requestIds);
	
	public List<AuthRequestLoanEntity> queryApplyList(Map<String,Object> param);
	
	public AuthRequestLoanEntity queryLastApply(Map<String,Object> param);
	
}
