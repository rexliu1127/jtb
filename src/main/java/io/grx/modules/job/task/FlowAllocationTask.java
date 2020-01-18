package io.grx.modules.job.task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;

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
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.modules.auth.service.BqsReportService;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.service.YxReportService;
import io.grx.modules.flow.entity.FlowAllocationRecordEntity;
import io.grx.modules.flow.entity.FlowSettingEntity;
import io.grx.modules.flow.service.FlowAllocationRecordService;
import io.grx.modules.flow.service.FlowSettingService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.oauth2.TokenGenerator;
import io.grx.modules.sys.service.SysConfigService;

/**
 * 流量分发任务
 * 前置条件
 * 1.需要一个流量专门商户  see  sys_config  KEY=FLOW_MERCHANT_NO
 * 2.需要配置一个并发数  see sys_config  KEY=FLOW_CONCURRENT
 * 3.配置一个CPA的价格 see sys_config KEY=FLOW_CPA
 * 4.用户配置购买流量后，平台人员挂相关链接到各大贷超
 * 5.此时就开始执行任务,以下是任务执行流程
 * 	A.获取配置了购买流量的商户以及数量
 *  B.获取已进件的流量(默认只获取2天以内的),倒序排列
 *  C.开始分发流量,分发的时候不得超过配置的并发数 FLOW_CONCURRENT
 *	
 *
 */
@Component("flowAllocationTask")
public class FlowAllocationTask {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private AuthRequestService authRequestService;
	
	@Autowired
	private FlowSettingService flowSettingService;
	
	@Autowired
	private FlowAllocationRecordService flowAllocationRecordService;
	
	@Autowired
	private AuthUserService authUserService;
	
	@Autowired
	private ChannelService channelService;
	
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
	
	/**
	 * 流量分发-任务
	 */
	public void runAllocationTask() {
		
		logger.info("开始分发流量----------------------");
		
		String flowMerchantNo = this.getFlowMerchantNo();
		Integer flowConcurrent = this.getFlowConcurrent();
		if(flowMerchantNo==null||flowConcurrent==null) {
			return;
		}
		
		//查询开启了购买流量的商户
		List<FlowSettingEntity> settingList = this.flowSettingService.queryOpenSettingFlow();
		
		//查询从x分钟之前开始的流量  x=
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - this.getFlowLimitMin());
        Date yesterday = calendar.getTime();
        String yesterdayStr = DateUtils.formateDate(yesterday);// yyyy-MM-dd
		Date yesterdayParam = DateUtils.parseDate(yesterdayStr);
		List<AuthRequestEntity> flowRequestList = this.authRequestService.queryFlowCopyList(flowMerchantNo,RequestStatus.PENDING.getValue(),DateUtils.formateDate(yesterdayParam, DateUtils.DATE_TIME_PATTERN));
		//开始分配-不超过最大的并发数
		this.allocationFlow(settingList, flowRequestList,flowConcurrent);
		
		List<AuthRequestEntity> flowRequestList2 = this.authRequestService.queryFlowCopyList(flowMerchantNo,RequestStatus.NEW.getValue(),DateUtils.formateDate(yesterdayParam, DateUtils.DATE_TIME_PATTERN));
		//开始分配-不超过最大的并发数
		this.allocationFlow(settingList, flowRequestList2,flowConcurrent);
		
		/**
		 * 认证数据跟踪
		 * 1.获取24H之内分发是原始数据ID
		 * 2.获取24H之内分发的目标数据ID
		 * 3.对比数据,是否有分发基本信息/报告等
		 * 4.如果3为否,则再次复制
		 */
		this.flowAllocationRecordService.trackFlowRequest();
	}
	
	/**
	 * 准备分发流量
	 * @param settingList
	 * @param flowRequestList
	 */
	private void allocationFlow(List<FlowSettingEntity> settingList,List<AuthRequestEntity> flowRequestList,Integer flowConcurrent) {
		if(CollectionUtils.isEmpty(settingList)||CollectionUtils.isEmpty(flowRequestList)) {
			logger.warn("购买流量的商户为空或进件为空");
			return;
		}
		for(FlowSettingEntity setting : settingList) {
			//流量需求数
			Integer total = setting.getFlowCount();
			//已完成导流
			Integer complete = setting.getCompleteCount();
			//目标商户号
			String targetMerchantNo = setting.getMerchantNo();
			//剩余数量
			Integer surplus = total-complete;
			if(surplus==0) {
				this.finishAllocation(setting);
				logger.info("settingId:"+setting.getId()+"  商户号:"+setting.getMerchantNo()+" 已完成流量分发");
				continue;
			}
			//目标渠道
			Long channelId = setting.getChannelId();
			/**
			 * 开始分配
			 * 1.流量商户号里面的数据分发数量不能超过并发数  可在sys_flow_allocation_record查看记录
			 * 2.根据手机号匹配,商户已存在的流量直接跳过 通过auth_request 查询
			 */
			surplus = this.allocationRequest(setting.getId(),targetMerchantNo, channelId, surplus, flowRequestList,flowConcurrent);
			if(surplus==0) {
				setting.setCompleteCount(total);
				setting.setStatus(1);
				this.flowSettingService.update(setting);
			}else {
				setting.setCompleteCount(total-surplus);
				this.flowSettingService.update(setting);
			}
		}
	}
	
	/**
	 * 分发流量
	 * @param targetChannelId	目标渠道ID
	 * @param surplus	本次导流数量
	 * @return 剩余多少没有分配
	 */
	private Integer allocationRequest(Long settingId,String targetMerchantNo,Long targetChannelId,Integer surplus,List<AuthRequestEntity> flowRequestList,Integer flowConcurrent) {
		for(AuthRequestEntity request : flowRequestList) {
			//获取当前申请人的手机号
			Long userId = request.getUserId();
			AuthUserEntity user = authUserService.queryObject(userId);
			String mobile = user.getMobile();
			//判断当前用户是否在当前商户下已存在
			AuthUserEntity existsUser = this.authUserService.queryByMerchantNoAndMobile(mobile, targetMerchantNo);
			if(existsUser!=null) {
				continue;
			}
			//当前流量如果分发>流量分发并发配置则跳过
			Integer concurrentCount = this.flowAllocationRecordService.queryConcurrentCountByRequestId(request.getRequestId());
			if(concurrentCount>=flowConcurrent) {
				//超过并发数-跳过
				continue;
			}
			ChannelEntity channel = channelService.queryObject(targetChannelId);
			if(channel==null) {
				return surplus;
			}
			if(copyAuthUserRequest(settingId,targetMerchantNo, channel, userId, request)) {
				surplus--;
			}
			if(surplus==0) {
				return surplus;
			}
		}
		return surplus;
	}
	
	@Transactional
	private boolean copyAuthUserRequest(Long settingId,String targetMerchantNo,ChannelEntity targetChannel,Long userId,AuthRequestEntity request) {
		//拷贝对象
		try {
			AuthUserEntity newUser = new AuthUserEntity();
			AuthUserEntity user = authUserService.queryObject(userId);
			
			AuthUserContactEntity contact = authUserContactService.queryObject(userId);
			AuthUserTokenEntity userToken = authUserTokenService.queryObject(userId);
			
			//拷贝-用户存储
			BeanUtils.copyProperties(newUser, user);
			newUser.setUserId(null);
			newUser.setMerchantNo(targetMerchantNo);
			newUser.setChannelId(targetChannel.getChannelId());
			authUserService.save(newUser);
			
			//拷贝-用户Token
			AuthUserTokenEntity newUserToken = new AuthUserTokenEntity();
			if(userToken!=null) {
				BeanUtils.copyProperties(newUserToken, userToken);
				newUserToken.setUserId(newUser.getUserId());
				newUserToken.setToken(TokenGenerator.generateValue());
				authUserTokenService.save(newUserToken);
			}
			
			//拷贝-联系人
			if(contact!=null) {
				AuthUserContactEntity newContact = new AuthUserContactEntity();
				newContact.setUserId(newUser.getUserId());
				newContact.setContact(contact.getContact());
				authUserContactService.save(newContact);
			}
			
			//拷贝-保存申请信息
			AuthRequestEntity newRequest = new AuthRequestEntity();
			BeanUtils.copyProperties(newRequest, request);
			newRequest.setRequestId(null);
			newRequest.setMerchantNo(targetMerchantNo);
			newRequest.setChannelId(targetChannel.getChannelId());
			newRequest.setRequestUuid(UUIDGenerator.getShortUUID());
			newRequest.setUserId(newUser.getUserId());
			newRequest.setDeptId(targetChannel.getDeptId());
//			newRequest.setCreateTime(new Date());
			newRequest.setAssigneeId(channelService.getAssigneeId(targetChannel));
			newRequest.setProcessorId(null);
			this.authRequestService.save(newRequest);
			
			//拷贝-保存报告信息-天机
			for(TianjiType type : TianjiType.values()) {
				TjReportEntity report = tjReportService.queryLatestByUserId(userId, type);
				if(report!=null) {
					TjReportEntity newReport = new TjReportEntity();
					BeanUtils.copyProperties(newReport, report);
					newReport.setMerchantNo(targetMerchantNo);
					newReport.setUserId(newUser.getUserId());
					newReport.setId(null);
					this.tjReportService.save(newReport);
				}
			}
			
			//拷贝-保存报告信息-新颜
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
			
			//拷贝-保存报告信息-白骑士
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
			
			//添加分配记录
			FlowAllocationRecordEntity allocationRecord = new FlowAllocationRecordEntity();
			allocationRecord.setSettingId(settingId);
			allocationRecord.setSourceRequestId(request.getRequestId());
			allocationRecord.setNewRequestId(newRequest.getRequestId());
			allocationRecord.setCreateTime(new Date());
			
			if(RequestStatus.NEW.getValue()==request.getStatus().getValue()) {
				//新注册的单据,需要后续跟踪分发
				allocationRecord.setReqStatus(0);
			}else if(RequestStatus.PENDING.getValue()==request.getStatus().getValue()) {
				//待审核的单据,无需后续操作
				allocationRecord.setReqStatus(1);
			}else {
				//非草稿和待审核单据,后续跟踪跳过
				allocationRecord.setReqStatus(-1);
			}
			
			flowAllocationRecordService.save(allocationRecord);
			
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private void finishAllocation(FlowSettingEntity setting) {
		try {
			setting.setStatus(1);
			this.flowSettingService.update(setting);
		}catch(Exception e) {
			logger.error("更新流量购买配置失败 "+JSON.toJSONString(setting));
		}
	}
	
	private String getFlowMerchantNo() {
		String merchantNo = sysConfigService.getValue(Constant.KEY_FLOW_MERCHANT_NO);
		if(StringUtils.isEmpty(merchantNo)) {
			logger.warn("未配置流量管理员商户号");
			return null;
		}
		return merchantNo;
	}
	
	/**
	 * 获取流量失效时间-分钟
	 * @return
	 */
	private Integer getFlowLimitMin() {
		String value = this.sysConfigService.getValue("FLOW_IMPORT_LIMIT_MIN");
		if(StringUtils.isEmpty(value)||!NumberUtils.isNumber(value)) {
			return 5;
		}
		return Integer.parseInt(value.trim());
	}
	
	private Integer getFlowConcurrent() {
		String count = this.sysConfigService.getValue(Constant.KEY_FLOW_CONCURRENT);
		if(StringUtils.isEmpty(count)||!NumberUtils.isNumber(count)) {
			logger.warn("未配置流量并发数,或配置有误  当前配置："+count);
			return null;
		}
		return Integer.parseInt(count);
	}
}
