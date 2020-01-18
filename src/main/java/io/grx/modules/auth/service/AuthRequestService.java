package io.grx.modules.auth.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.R;
import io.grx.modules.auth.dto.AuthSummary;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.opt.dto.ChannelStatVO;

/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
public interface AuthRequestService {
	
	AuthRequestEntity queryObject(Long requestId);
	
	List<AuthRequestEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AuthRequestEntity authRequest);
	
	void update(AuthRequestEntity authRequest);
	
	void delete(Long requestId);
	
	void deleteBatch(Long[] requestIds);

	List<AuthRequestEntity> queryByUserAndStatus(Long userId,
												 RequestStatus... status);

	R allocateAuditor(Long auditorUserId, Long... requestIds);

	AuthRequestEntity queryByUuid(String uuid);

	int queryRequestCount(Long userId, Long requestId);

	R updateStatus(String requestUuid, RequestStatus status, String userRemark, String adminRemark);

	List<AuthRequestEntity> queryPendingVerificationList();

	void createAuthRequest(String requestIdentifier, AuthVendorType vendorType, String channelKey);

	void createAuthRequest(String requestIdentifier, AuthVendorType vendorType, String channelKey, String addr,String latitude,String longitude);

	AuthRequestEntity queryLatestByUserId(Long userId, Long channelId);

	AuthRequestEntity queryLatestByIdNo(String idNo, Long channelId);

	AuthRequestEntity queryByToken(String token);

    void createAuthRequest(String taskId, AuthUserEntity user, Date createTime);

    List<AuthRequestEntity> queryPendingVerifyRequests();

    AuthVendorType getSystemVendorType();

    boolean hasAuthRequest(String idNo, List<Long> deptIds, int inRecentDays);

    R copyRequestToOtherChannel(Long requestId, Long channelId);

	AuthSummary querySummary(Map<String, Object> params);

	List<ChannelStatVO> queryChannelStatList(Map<String, Object> params);

	int queryChannelStatTotal(Map<String, Object> map);

	AuthRequestEntity createNewRequest();
	
	List<AuthRequestEntity> queryFlowCopyList(String merchantNo,Integer status,String startTime);

	List<AuthRequestEntity> queryFlowChangeList(String merchantNo,String createTime);
	
	/**
	 * 根据身份证查询当前用户在当前商户下拒绝的申请数,一般情况下只要拒绝过一次,当前商户的所有渠道都不允许申请
	 * @param merchantNo
	 * @param idNo
	 * @return
	 */
	int queryRejectCountByIdNo(String merchantNo,String idNo);
}
