package io.grx.modules.flow.service.impl;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.common.utils.Constant;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserContactEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthUserTokenEntity;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.entity.YxReportEntity;
import io.grx.modules.auth.enums.BaiqishiType;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.modules.auth.service.BqsReportService;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.service.YxReportService;
import io.grx.modules.flow.dao.FlowAllocationRecordDao;
import io.grx.modules.flow.entity.FlowAllocationRecordEntity;
import io.grx.modules.flow.service.FlowAllocationRecordService;
import io.grx.modules.sys.oauth2.TokenGenerator;
import io.grx.modules.sys.service.SysConfigService;



@Service("flowAllocationRecordService")
public class FlowAllocationRecordServiceImpl implements FlowAllocationRecordService {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowAllocationRecordServiceImpl.class);
	
	@Autowired
	private FlowAllocationRecordDao flowAllocationRecordDao;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private AuthRequestService authRequestService;
	
	@Autowired
	private FlowAllocationRecordService flowAllocationRecordService;
	
	@Autowired
	private AuthUserService authUserService;
	
	@Autowired
	private AuthUserContactService authUserContactService;
	
	@Autowired
	private AuthUserTokenService authUserTokenService;
	
	@Autowired
	private TjReportService tjReportService;
	
	@Autowired
	private YxReportService yxReportService;
	
	@Autowired
	private BqsReportService bqsReportService;
	
	@Override
	public FlowAllocationRecordEntity queryObject(Long userId){
		return flowAllocationRecordDao.queryObject(userId);
	}
	
	@Override
	public List<FlowAllocationRecordEntity> queryList(Map<String, Object> map){
		return flowAllocationRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return flowAllocationRecordDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public FlowAllocationRecordEntity save(FlowAllocationRecordEntity entity){
		this.flowAllocationRecordDao.save(entity);
		return entity;
	}
	
	@Override
	@Transactional
	public void update(FlowAllocationRecordEntity entity){
		this.flowAllocationRecordDao.update(entity);
	}

	@Override
	@Transactional
	public void delete(Long id){
		flowAllocationRecordDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		flowAllocationRecordDao.deleteBatch(ids);
	}
	
	public Integer queryConcurrentCountByRequestId(Long sourceRequestId) {
		return this.flowAllocationRecordDao.queryConcurrentCountByRequestId(sourceRequestId);
	}
	
	public List<Map<String,Object>> queryAllocationStatis(Map<String,Object> params){
		return this.flowAllocationRecordDao.queryAllocationStatis(params);
	}
	
	
	public Integer queryAllocationStatisTotal(Map<String,Object> params) {
		return this.flowAllocationRecordDao.queryAllocationStatisTotal(params);
	}
	
	public List<Map<String,Object>> querySpreadList(Map<String,Object> params){
		return this.flowAllocationRecordDao.querySpreadList(params);
	}
	
	public Integer querySpreadListTotal(Map<String,Object> params) {
		return this.flowAllocationRecordDao.querySpreadListTotal(params);
	}
	
	public List<FlowAllocationRecordEntity> queryFollowDraftList(String time){
		return this.flowAllocationRecordDao.queryFollowDraftList(time);
	}
	
	/**
	 * 开始跟踪报告
	 */
	public void trackFlowRequest() {
		//1.查询24小时之内的
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -this.getTrackRequestHour());
        Date time = calendar.getTime();
		List<FlowAllocationRecordEntity> list = this.flowAllocationRecordService.queryFollowDraftList(DateUtils.formateDateTime(time));
		int size = list==null?0:list.size();
		logger.info("查询到待跟踪的注册订单数:"+size+"条");
		if(size>0) {
			//查询 分发时是待提交的状态,此时是待审核的订单
			List<AuthRequestEntity> requestList = this.authRequestService.queryFlowChangeList(getFlowMerchantNo(), DateUtils.formateDateTime(time));
			if(CollectionUtils.isEmpty(requestList)) {
				logger.warn("查询流量分发[未提交-待审核]进件数为0");
				return;
			}
			//转Map key就是requestId
			Map<Long,AuthRequestEntity> requestMap = new HashMap<Long,AuthRequestEntity>();
			for(AuthRequestEntity entity : requestList) {
				requestMap.put(entity.getRequestId(), entity);
			}
			
			//开始一波骚操作-数据同步
			for(FlowAllocationRecordEntity record : list) {
				AuthRequestEntity sourceRequest = requestMap.get(record.getSourceRequestId());
				if(sourceRequest!=null) {
					this.syncFlowRequest(record.getNewRequestId(),sourceRequest,record,true);
				}
			}
		}
	}

	private boolean syncFlowRequest(Long newRequestId,AuthRequestEntity sourceRequest,FlowAllocationRecordEntity record,boolean isAll) {
		try {
			AuthRequestEntity newRequest = this.authRequestService.queryObject(newRequestId);
			String targetMerchantNo = newRequest.getMerchantNo();
			Long targetChannelId = newRequest.getChannelId();
			Long userId = sourceRequest.getUserId();
			/*1.跟踪AuthUser*/
			AuthUserEntity newUser = this.authUserService.queryObject(newRequest.getUserId());
			Long newUserId = newUser.getUserId();
			AuthUserEntity user = this.authUserService.queryObject(sourceRequest.getUserId());
			//开始同步
			BeanUtils.copyProperties(newUser, user);
			newUser.setUserId(newUserId);
			newUser.setMerchantNo(targetMerchantNo);
			newUser.setChannelId(targetChannelId);
			this.authUserService.update(newUser);
			
			/*2.跟踪AuthUserToken*/
			AuthUserTokenEntity newUserToken = this.authUserTokenService.queryObject(newUserId);
			AuthUserTokenEntity userToken = authUserTokenService.queryObject(userId);
			if(userToken!=null&&newUserToken==null) {
				newUserToken = new AuthUserTokenEntity();
				BeanUtils.copyProperties(newUserToken, userToken);
				newUserToken.setUserId(newUserId);
				newUserToken.setToken(TokenGenerator.generateValue());
				authUserTokenService.save(newUserToken);
			}
			
			/*3.跟踪AuthUserContact*/
			AuthUserContactEntity contact = authUserContactService.queryObject(userId);
			AuthUserContactEntity newContact = authUserContactService.queryObject(newUserId);
			if(contact!=null&&newContact==null) {
				newContact = new AuthUserContactEntity();
				newContact.setUserId(newUserId);
				newContact.setContact(contact.getContact());
				authUserContactService.save(newContact);
			}
			
			
			/*4.跟踪AuthRequest*/
			Long deptId = newRequest.getDeptId();
			Long assigneedId = newRequest.getAssigneeId();
			String uuid = newRequest.getRequestUuid();
			BeanUtils.copyProperties(newRequest, sourceRequest);
			newRequest.setRequestId(newRequestId);
			newRequest.setMerchantNo(targetMerchantNo);
			newRequest.setChannelId(targetChannelId);
			newRequest.setRequestUuid(uuid);
			newRequest.setUserId(newUser.getUserId());
			newRequest.setDeptId(deptId);
			newRequest.setAssigneeId(assigneedId);
			newRequest.setProcessorId(null);
			this.authRequestService.update(newRequest);
			
			if(isAll) {
				/*5.跟踪TianjiReport*/
				for(TianjiType type : TianjiType.values()) {
					TjReportEntity report = tjReportService.queryLatestByUserId(userId, type);
					if(report!=null) {
						TjReportEntity newReport = new TjReportEntity();
						BeanUtils.copyProperties(newReport, report);
						newReport.setMerchantNo(targetMerchantNo);
						newReport.setUserId(newUserId);
						newReport.setId(null);
						this.tjReportService.save(newReport);
					}
				}
				
				/*6.跟踪XinYanReport*/
				for(YiXiangType type : YiXiangType.values()) {
					YxReportEntity report = yxReportService.queryLatestByUserId(userId, type);
					if(report!=null) {
						YxReportEntity newReport = new YxReportEntity();
						BeanUtils.copyProperties(newReport, report);
						newReport.setMerchantNo(targetMerchantNo);
						newReport.setUserId(newUser.getUserId());
						newReport.setId(null);
						this.yxReportService.save(newReport);
					}
				}
				
				/*7.跟踪BaiqishiReport*/
				for(BaiqishiType type : BaiqishiType.values()) {
					BqsReportEntity report = bqsReportService.queryLatestByUserId(userId, type);
					if(report!=null) {
						BqsReportEntity newReport = new BqsReportEntity();
						BeanUtils.copyProperties(newReport, report);
						newReport.setMerchantNo(targetMerchantNo);
						newReport.setUserId(newUser.getUserId());
						newReport.setId(null);
						this.bqsReportService.save(newReport);
					}
				}
				
				/*8.更新状态*/
				if(record!=null) {
					record.setReqStatus(1);
					this.flowAllocationRecordService.update(record);
				}
			}
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 流量分发用户跟踪
	 * @param requestId	原始申请单ID
	 */
	public void updateTrackAuthUser(Long requestId) {
		logger.info("开始跟踪流量分发申请[用户信息] requestId:{}",requestId);
		int count = 0;
		//1.根据申请单ID查询当前分发记录
		List<FlowAllocationRecordEntity> list = this.flowAllocationRecordDao.queryListBySourceRequestId(requestId);
		if(CollectionUtils.isEmpty(list)) {
			logger.info("当前申请单据requestId:{} 分发记录为空,结束",requestId);
			return;
		}
		int total = list.size();
		AuthRequestEntity sourceRequest = this.authRequestService.queryObject(requestId);
		if(sourceRequest==null) {
			logger.info("当前申请单据requestId:{} 原始申请记录不存在,结束",requestId);
			return;
		}
		AuthUserEntity sourceUser = this.authUserService.queryObject(sourceRequest.getUserId());
		if(sourceUser==null) {
			logger.info("当前申请单据requestId:{} 原始申请用户不存在,结束",requestId);
			return;
		}
		for(FlowAllocationRecordEntity entity : list) {
			Long newRequestId = entity.getNewRequestId();
			boolean bool = this.syncFlowRequest(newRequestId, sourceRequest, entity, false);
			if(bool) {
				count++;
			}
		}
		logger.info("当前申请单据requestId:{} 分发完成,总数:{} 成功数:{}",requestId,total,count);
	}
	
	/**
	 * 流量分发申请跟踪
	 * @param requestId	原始申请单ID
	 */
	public void updateTrackAuthUserRequest(Long requestId) {
		logger.info("开始跟踪流量分发申请[用户申请] requestId:{}",requestId);
		int count = 0;
		//1.根据申请单ID查询当前分发记录
		List<FlowAllocationRecordEntity> list = this.flowAllocationRecordDao.queryListBySourceRequestId(requestId);
		if(CollectionUtils.isEmpty(list)) {
			logger.info("当前申请单据requestId:{} 分发记录为空,结束",requestId);
			return;
		}
		int total = list.size();
		AuthRequestEntity sourceRequest = this.authRequestService.queryObject(requestId);
		if(sourceRequest==null) {
			logger.info("当前申请单据requestId:{} 原始申请记录不存在,结束",requestId);
			return;
		}
		AuthUserEntity sourceUser = this.authUserService.queryObject(sourceRequest.getUserId());
		if(sourceUser==null) {
			logger.info("当前申请单据requestId:{} 原始申请用户不存在,结束",requestId);
			return;
		}
		for(FlowAllocationRecordEntity entity : list) {
			Long newRequestId = entity.getNewRequestId();
			boolean bool = this.syncFlowRequest(newRequestId, sourceRequest, entity, true);
			if(bool) {
				count++;
			}
		}
		logger.info("当前申请单据requestId:{} 分发完成,总数:{} 成功数:{}",requestId,total,count);
	}
	
	private Integer getTrackRequestHour() {
		String value = this.sysConfigService.getValue("FLOW_TRACK_HOUR");
		if(StringUtils.isEmpty(value)||!NumberUtils.isNumber(value)) {
			return 1;
		}
		return Integer.parseInt(value.trim());
	}
	
	private String getFlowMerchantNo() {
		String merchantNo = sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
		if(StringUtils.isEmpty(merchantNo)) {
			logger.warn("未配置流量管理员商户号");
			return null;
		}
		return merchantNo;
	}
}
