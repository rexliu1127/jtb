package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthUserReportDsEntity;
import io.grx.modules.auth.enums.VerifyStatus;

/**
 * 电商认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public interface AuthUserReportDsService {
	
	AuthUserReportDsEntity queryObject(Long id);
	
	List<AuthUserReportDsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthUserReportDsEntity authUserReportDs);
	
	void update(AuthUserReportDsEntity authUserReportDs);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	AuthUserReportDsEntity queryByTaskId(String taskId);

	List<AuthUserReportDsEntity> queryByStatuses(Long userId, VerifyStatus... verifyStatuses);
}
