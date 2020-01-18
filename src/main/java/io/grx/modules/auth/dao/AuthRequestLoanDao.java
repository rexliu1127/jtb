package io.grx.modules.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import io.grx.modules.auth.entity.AuthRequestLoanEntity;
import io.grx.modules.sys.dao.BaseDao;

/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
@Mapper
public interface AuthRequestLoanDao extends BaseDao<AuthRequestLoanEntity> {
	
	public List<AuthRequestLoanEntity> queryApplyList(Map<String,Object> param);
	
	public AuthRequestLoanEntity queryLastApply(Map<String,Object> param);
}
