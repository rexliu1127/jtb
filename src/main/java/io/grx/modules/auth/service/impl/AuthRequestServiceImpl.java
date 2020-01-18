package io.grx.modules.auth.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.*;
import io.grx.modules.auth.dto.AuthSummary;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.enums.*;
import io.grx.modules.auth.service.*;
import io.grx.modules.opt.dto.ChannelStatVO;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.annotation.DataFilter;
import io.grx.common.exception.RRException;
import io.grx.modules.auth.dao.AuthRequestDao;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.service.SysConfigService;
import org.springframework.web.bind.annotation.RequestBody;


@Service("authRequestService")
public class AuthRequestServiceImpl implements AuthRequestService {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthRequestDao authRequestDao;

	@Autowired
    private AuthRequestHistoryService authRequestHistoryService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private TjReportService tjReportService;

	@Autowired
	private YxReportService yxReportService;

	@Autowired
	private AuthUserContactService authUserContactService;

	@Autowired
	private AuthRecommendService authRecommendService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private CacheUtils cacheUtils;

	@Override
	public AuthRequestEntity queryObject(Long requestId){
		return authRequestDao.queryObject(requestId);
	}

	@Override
    @DataFilter(tableAlias = "c", userFieldId = "ar.assignee_id")
	public List<AuthRequestEntity> queryList(Map<String, Object> map){
		return authRequestDao.queryList(map);
	}

	@Override
    @DataFilter(tableAlias = "c", userFieldId = "ar.assignee_id")
	public int queryTotal(Map<String, Object> map){
		return authRequestDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(AuthRequestEntity authRequest){
		authRequestDao.save(authRequest);

		if (authRequest.getAssigneeId() != null) {
			insertAllocateHistory(authRequest);
		}
	}

	@Override
	@Transactional
	public void update(AuthRequestEntity authRequest){
		authRequestDao.update(authRequest);
	}

	@Override
	@Transactional
	public void delete(Long requestId){
		authRequestDao.delete(requestId);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] requestIds){
		authRequestDao.deleteBatch(requestIds);
	}

	@Override
	@Transactional
	public List<AuthRequestEntity> queryByUserAndStatus(final Long userId, final RequestStatus... status) {
		return authRequestDao.queryByUserAndStatus(userId, status);
	}

    @Override
    @Transactional
    public R allocateAuditor(final Long auditorUserId, final Long... requestIds) {
	    int successCount = 0;
        for (Long requestId : requestIds) {
            AuthRequestEntity requestEntity = queryObject(requestId);
            if (auditorUserId != null && requestEntity != null
                    && !requestId.equals(requestEntity.getProcessorId())) {
                requestEntity.setAssigneeId(auditorUserId);
//				requestEntity.setStatus(RequestStatus.PROCESSING);
                update(requestEntity);

				insertAllocateHistory(requestEntity);

                successCount++;
            }
        }
        return R.ok().put("total", requestIds.length)
                .put("successCount", successCount);
    }

    private void insertAllocateHistory(AuthRequestEntity requestEntity) {
		// history
		AuthRequestHistoryEntity historyEntity = new AuthRequestHistoryEntity();
		historyEntity.setRequestId(requestEntity.getRequestId());
		historyEntity.setCreateUserId(ShiroUtils.getUserId());
		historyEntity.setProcessorId(requestEntity.getAssigneeId());
		historyEntity.setCreateTime(new Date());
		historyEntity.setStatus(requestEntity.getStatus());

		authRequestHistoryService.save(historyEntity);
	}

    @Override
    @Transactional
    public R updateStatus(String requestUuid, RequestStatus status, String userRemark, String adminRemark) {
        AuthRequestEntity requestEntity = queryByUuid(requestUuid);
        if (requestEntity == null) {
            return R.error("非法请求");
        }

		requestEntity.setProcessorId(ShiroUtils.getUserId());
		requestEntity.setStatus(status);
		requestEntity.setUpdateTime(new Date());
		update(requestEntity);

		// history
		AuthRequestHistoryEntity historyEntity = new AuthRequestHistoryEntity();
		historyEntity.setRequestId(requestEntity.getRequestId());
		historyEntity.setCreateUserId(ShiroUtils.getUserId());
		historyEntity.setCreateTime(requestEntity.getUpdateTime());
		historyEntity.setStatus(requestEntity.getStatus());
		historyEntity.setUserRemark(userRemark);
		historyEntity.setAdminRemark(adminRemark);

		authRequestHistoryService.save(historyEntity);
        return R.ok();
    }

	@Override
	public List<AuthRequestEntity> queryPendingVerificationList() {
		return authRequestDao.queryByVerifyStatus(VerifyStatus.SUBMITTED);
	}

	@Override
    @Transactional
	public void createAuthRequest(final String requestIdentifier, AuthVendorType vendorType, String channelKey) {
        createAuthRequest(requestIdentifier, vendorType, channelKey, null,null,null);
	}

	@Override
	public void createAuthRequest(final String requestIdentifier, final AuthVendorType vendorType,
                                  final String channelKey, final String addr,final String latitude,final String longitude) {
		AuthUserEntity user = ShiroUtils.getAuthUser();
		if (user == null) {
			throw new RRException("invalid request");
		}

		AuthRequestEntity request = queryLatestByUserId(user.getUserId(), ShiroUtils.getAuthChannelId());
		if (request == null || request.getStatus() != RequestStatus.NEW) {
			request = createNewAuthRequest();
		}
		request.setName(user.getName());
		request.setIdNo(user.getIdNo());
		request.setQqNo(user.getQqNo());
		request.setWechatNo(user.getWechatNo());
		request.setCompanyName(user.getCompanyName());
		request.setCompanyAddr(user.getCompanyAddr());
		request.setCompanyTel(user.getCompanyTel());
		request.setIdUrl1(user.getIdUrl1());
		request.setIdUrl2(user.getIdUrl2());
		request.setIdUrl3(user.getIdUrl3());
		request.setMobilePass(user.getMobilePass());
		request.setContact1Type(user.getContact1Type());
		request.setContact1Name(user.getContact1Name());
		request.setContact1Mobile(user.getContact1Mobile());
		request.setContact2Type(user.getContact2Type());
		request.setContact2Name(user.getContact2Name());
		request.setContact2Mobile(user.getContact2Mobile());
		request.setContact3Type(user.getContact3Type());
		request.setContact3Name(user.getContact3Name());
		request.setContact3Mobile(user.getContact3Mobile());
		request.setStatus(RequestStatus.PENDING);
		request.setVerifyToken(requestIdentifier);
		request.setVerifyStatus(vendorType == AuthVendorType.TJ ? VerifyStatus.SUCCESS : VerifyStatus.PROCESSING);
		request.setCreateTime(new Date());
		request.setVendorType(vendorType);
		request.setGpsAddr(addr);
		request.setLatitude(latitude);
		request.setLongitude(longitude);

		if (request.getAssigneeId() == null) {
			ChannelEntity channel = ShiroUtils.getAuthChannel();
			if (channel != null) {
				request.setAssigneeId(channelService.getAssigneeId(channel));
			}
		}

		if (request.getRequestId() == null) {
			save(request);
		} else {
			update(request);
		}

//		authUserService.sendIntegrationRequest(user.getMobile(), (channel != null ? channel.getChannelKey() : null),
//				user.getIdNo(), user.getName());
	}

	private AuthRequestEntity createNewAuthRequest() {
		AuthUserEntity user = ShiroUtils.getAuthUser();
		ChannelEntity channel = ShiroUtils.getAuthChannel();
		AuthRequestEntity request = new AuthRequestEntity();
		request.setMerchantNo(channel.getMerchantNo());
		if (channel != null) {
			request.setChannelId(channel.getChannelId());
			request.setMerchantNo(channel.getMerchantNo());
			request.setDeptId(channel.getDeptId());

			request.setAssigneeId(channelService.getAssigneeId(channel));
		}

		request.setUserId(user.getUserId());
		request.setRequestUuid(UUIDGenerator.getShortUUID());
		request.setName(user.getName());
		request.setIdNo(user.getIdNo());
		request.setQqNo(user.getQqNo());
		request.setWechatNo(user.getWechatNo());
		request.setCompanyName(user.getCompanyName());
		request.setCompanyAddr(user.getCompanyAddr());
		request.setCompanyTel(user.getCompanyTel());
		request.setIdUrl1(user.getIdUrl1());
		request.setIdUrl2(user.getIdUrl2());
		request.setIdUrl3(user.getIdUrl3());
		request.setMobilePass(user.getMobilePass());
		request.setContact1Type(user.getContact1Type());
		request.setContact1Name(user.getContact1Name());
		request.setContact1Mobile(user.getContact1Mobile());
		request.setContact2Type(user.getContact2Type());
		request.setContact2Name(user.getContact2Name());
		request.setContact2Mobile(user.getContact2Mobile());
		request.setContact3Type(user.getContact3Type());
		request.setContact3Name(user.getContact3Name());
		request.setContact3Mobile(user.getContact3Mobile());
		request.setStatus(RequestStatus.NEW);
		request.setVerifyStatus(VerifyStatus.NEW);
		request.setCreateTime(new Date());

		return request;
	}

	@Override
	@Transactional
	public void createAuthRequest(final String taskId, AuthUserEntity user, Date createTime) {
		if (user == null) {
			throw new RRException("invalid request");
		}

//		ChannelEntity channel = channelService.queryObject(user.getChannelId());
		AuthRequestEntity request = new AuthRequestEntity();
//		if (channel != null) {
//			request.setChannelId(channel.getChannelId());
//		}
		request.setUserId(user.getUserId());
		request.setRequestUuid(UUIDGenerator.getShortUUID());
		request.setName(user.getName());
		request.setIdNo(user.getIdNo());
		request.setQqNo(user.getQqNo());
		request.setIdUrl1(user.getIdUrl1());
		request.setIdUrl2(user.getIdUrl2());
		request.setIdUrl3(user.getIdUrl3());
		request.setMobilePass(user.getMobilePass());
		request.setContact1Type(user.getContact1Type());
		request.setContact1Name(user.getContact1Name());
		request.setContact1Mobile(user.getContact1Mobile());
		request.setContact2Type(user.getContact2Type());
		request.setContact2Name(user.getContact2Name());
		request.setContact2Mobile(user.getContact2Mobile());
		request.setStatus(RequestStatus.PENDING);
		request.setVerifyToken(taskId);
		request.setVerifyStatus(VerifyStatus.SUBMITTED);
		request.setCreateTime(createTime);
		save(request);

//		if (channel != null && channel.getAuditorUserId() != null) {
//			allocateAuditor(channel.getAuditorUserId(), request.getRequestId());
//		}

//		authUserService.sendIntegrationRequest(user.getMobile(), (channel != null ? channel.getChannelKey() : null),
//				user.getIdNo(), user.getName());
	}

	@Override
	public List<AuthRequestEntity> queryPendingVerifyRequests() {
		return authRequestDao.queryPendingVerifyRequests();
	}

	@Override
	public AuthVendorType getSystemVendorType() {
		String type = sysConfigService.getValue("AUTH_VENDOR_TYPE");
		if (StringUtils.equalsIgnoreCase(type, AuthVendorType.JXL.name())) {
			return AuthVendorType.JXL;
		}
		return AuthVendorType.SJMH;
	}

	@Override
	public boolean hasAuthRequest(final String idNo, final List<Long> deptIds, final int inRecentDays) {
		return authRequestDao.idNoHasRequest(idNo, inRecentDays, deptIds.toArray(new Long[deptIds.size()]));
	}

	@Override
	@Transactional
	public R copyRequestToOtherChannel(Long requestId, Long channelId) {
		if (!StringUtils.equalsIgnoreCase(ShiroUtils.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)) {
			return R.error("您没有此操作权限");
		}
		AuthRequestEntity requestEntity = queryObject(requestId);
		if (requestEntity == null) {
			return R.error("非法requestId");
		}

		ChannelEntity channelEntity = channelService.queryObject(channelId);
		if (channelEntity == null) {
			return R.error("非法渠道");
		}

		if (StringUtils.equalsIgnoreCase(requestEntity.getMerchantNo(), channelEntity.getMerchantNo())) {
			return R.error("不能推荐给相同公司下的其他渠道");
		}

		try {
			AuthUserEntity originalUser = authUserService.queryObject(requestEntity.getUserId());

			AuthUserEntity userEntity = authUserService.queryByMerchantNoAndMobile(originalUser.getMobile(), channelEntity.getMerchantNo());
			if (userEntity == null) {
				userEntity = new AuthUserEntity();
				BeanUtils.copyProperties(userEntity, originalUser);
//				userEntity.setMobile(originalUser.getMobile());
				userEntity.setUserId(null);
				userEntity.setMerchantNo(channelEntity.getMerchantNo());
				userEntity.setCreateTime(new Date());
//            userEntity.setChannelId(channelId);
				authUserService.save(userEntity);
			} else {
			    return R.error("该公司已有该用户申请");
            }

			for (TianjiType type : TianjiType.values()) {
				TjReportEntity reportEntity = tjReportService.queryLatestByUserId(originalUser.getUserId(), type);

				if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
					TjReportEntity clonedReportEntity = new TjReportEntity();
					BeanUtils.copyProperties(clonedReportEntity, reportEntity);
					clonedReportEntity.setId(null);
					clonedReportEntity.setMerchantNo(channelEntity.getMerchantNo());
					clonedReportEntity.setUserId(userEntity.getUserId());
					tjReportService.save(clonedReportEntity);
				}
			}

			for (YiXiangType type : YiXiangType.values()) {
				YxReportEntity reportEntity = yxReportService.queryLatestByUserId(originalUser.getUserId(), type);

				if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
					YxReportEntity clonedReportEntity = new YxReportEntity();
					BeanUtils.copyProperties(clonedReportEntity, reportEntity);
					clonedReportEntity.setId(null);
					clonedReportEntity.setMerchantNo(channelEntity.getMerchantNo());
					clonedReportEntity.setUserId(userEntity.getUserId());
					yxReportService.save(clonedReportEntity);
				}
			}

			Map<String, String> contacts = authUserContactService.getContacts(originalUser.getUserId());
			if (MapUtils.isNotEmpty(contacts)) {
				authUserContactService.saveOrUpdate(userEntity.getUserId(), contacts);
			}

			AuthRequestEntity newRequest = new AuthRequestEntity();
			BeanUtils.copyProperties(newRequest, requestEntity);
			newRequest.setRequestId(null);
			newRequest.setRequestUuid(UUIDGenerator.getShortUUID());
			newRequest.setMerchantNo(channelEntity.getMerchantNo());
			newRequest.setUserId(userEntity.getUserId());
			newRequest.setChannelId(channelId);
			newRequest.setStatus(RequestStatus.PENDING);
			newRequest.setCreateTime(new Date());
			newRequest.setUpdateTime(null);
			newRequest.setDeptId(channelEntity.getDeptId());
			newRequest.setProcessorId(null);
			newRequest.setAssigneeId(channelService.getAssigneeId(channelEntity));
			save(newRequest);

			AuthRecommendEntity recommendEntity = new AuthRecommendEntity();
			recommendEntity.setFromUserId(originalUser.getUserId());
			recommendEntity.setToUserId(userEntity.getUserId());
			recommendEntity.setMobile(originalUser.getMobile());
			recommendEntity.setCreateUserId(ShiroUtils.getUserId());
			recommendEntity.setCreateTime(newRequest.getCreateTime());
            authRecommendService.save(recommendEntity);

			logger.info("Recommended request {} to channel {}", newRequest.getRequestId(), channelId);

			return R.ok();
		} catch (Exception e) {
			logger.error("error in copy entities", e);
			throw new RRException("推荐过程发生错误");
		}
	}

	@Override
	@DataFilter(tableAlias = "ar", user = false)
	public AuthSummary querySummary(Map<String, Object> params) {
		return authRequestDao.querySummary(params);
	}

	@Override
	@DataFilter(tableAlias = "c", user = false)
	public List<ChannelStatVO> queryChannelStatList(Map<String, Object> params) {
		return authRequestDao.queryChannelStatList(params);
	}

	@Override
	@DataFilter(tableAlias = "c", user = false)
	public int queryChannelStatTotal(Map<String, Object> map) {
		return authRequestDao.queryChannelStatTotal(map);
	}

	@Override
	public AuthRequestEntity createNewRequest() {
		AuthRequestEntity requestEntity = createNewAuthRequest();
		save(requestEntity);
		return requestEntity;
	}

	@Override
	public AuthRequestEntity queryLatestByUserId(final Long userId, final Long channelId) {
		return authRequestDao.queryLatestByUserId(userId, channelId);
	}

	@Override
	public AuthRequestEntity queryLatestByIdNo(final String idNo, final Long channelId) {
		return authRequestDao.queryLatestByIdNo(idNo, channelId);
	}

	@Override
	public AuthRequestEntity queryByToken(final String token) {
		return authRequestDao.queryByToken(token);
	}

	@Override
	public AuthRequestEntity queryByUuid(String uuid) {
		return authRequestDao.queryByUuid(uuid);
	}

	@Override
	public int queryRequestCount(Long userId, Long requestId) {
		return authRequestDao.queryRequestCount(userId, requestId);
	}

	@Override
	public List<AuthRequestEntity> queryFlowCopyList(String merchantNo,Integer status,String startTime){
		return authRequestDao.queryFlowCopyList(merchantNo,status,startTime);
	}
	
	@Override
	public List<AuthRequestEntity> queryFlowChangeList(String merchantNo,String createTime){
		return authRequestDao.queryFlowChangeList(merchantNo, createTime);
	}
	
	@Override
	public int queryRejectCountByIdNo(String merchantNo,String idNo) {
		return authRequestDao.queryRejectCountByIdNo(merchantNo, idNo);
	}
}
