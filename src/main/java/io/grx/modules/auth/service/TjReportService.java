package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.entity.AuthTjMobileVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.TianjiType;

/**
 * 天机认证
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-04-13 14:52:36
 */
public interface TjReportService {
	
	TjReportEntity queryObject(Long id);
	
	List<TjReportEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TjReportEntity tjReport);
	
	void update(TjReportEntity tjReport);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	TjReportEntity queryByUniqueId(String uniqueId);

    TjReportEntity queryLatestByUserId(Long userId, TianjiType tianjiType);
    
	void expireReports();
	AuthTjMobileVO getTjMobileVo(String jsoninfo);
	AuthTjMobileVO getTjCallogVo(String jsoninfo, AuthUserEntity authUser,String mobileRawReport);
	
	TjReportEntity querySuccessReportByTaskId(String taskId,TianjiType tianjiType);
}
