package io.grx.modules.auth.service;

import java.util.List;
import java.util.Map;

import io.grx.modules.auth.dto.AuthStatVO;
import io.grx.modules.auth.dto.AuthUserStatVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 认证用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
public interface AuthUserService {
	
	AuthUserEntity queryObject(Long userId);
	
	List<AuthUserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthUserEntity authUser);

	void saveWithNewRequest(AuthUserEntity authUser);
	
	void update(AuthUserEntity authUser);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	AuthUserEntity queryByMobile(String mobile, Long channelId);

	List<AuthUserStatVO> queryStatList(Map<String, Object> map);

	int queryStatTotal(Map<String, Object> map);

	AuthStatVO queryRequestStat();

	void sendIntegrationRequest(String mobile, String channelId, String idNo, String name);

	List<AuthUserEntity> queryAllChannelUsersByMobile(String mobile);

	AuthUserEntity queryByMerchantNoAndMobile(String mobile, String merchantNo);

	List<AuthUserEntity> queryByAuthTaskId(String taskId);

	AuthUserEntity queryAuthByYiXiangReportTaskId(String taskId);

	int updateAuthStatus(long userId,String name,String idNo, boolean status);
}
