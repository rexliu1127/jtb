package io.grx.modules.auth.controller;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import io.grx.auth.service.YouDunService;
import io.grx.common.annotation.SysLog;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.AuthUserOCREntity;
import io.grx.modules.auth.entity.AuthYouDunReportVO;
import io.grx.modules.auth.service.AuthUserOCRService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import io.grx.modules.sys.controller.AbstractController;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysMerchantService;
import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.tx.service.TxLenderService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.auth.converter.AuthRequestHistoryVOConverter;
import io.grx.modules.auth.converter.AuthRequestIntegrationDTOConverter;
import io.grx.modules.auth.converter.AuthRequestVOConverter;
import io.grx.modules.auth.dto.AuthRequestHistoryVO;
import io.grx.modules.auth.dto.AuthRequestVO;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.RequestStatus;
import io.grx.modules.auth.service.AuthRequestHistoryService;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.impl.AuthUserContactServiceImpl;


/**
 * 申请单
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
@RestController
@RequestMapping("/autha/request")
public class AuthRequestController extends AbstractController{
    protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AuthRequestService authRequestService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthRequestVOConverter authRequestVOConverter;

	@Autowired
    private AuthRequestHistoryService authRequestHistoryService;

	@Autowired
    private AuthRequestHistoryVOConverter authRequestHistoryVOConverter;

	@Autowired
	private AuthRequestIntegrationDTOConverter authRequestIntegrationDTOConverter;

	@Autowired
    private AuthUserContactServiceImpl authUserContactService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SysMerchantService sysMerchantService;

	@Autowired
	private PoiUtils poiUtils;
	@Autowired
	private YouDunService youDunService;
	@Autowired
	private TxLenderService txLenderService;
	@Autowired
	private AuthUserOCRService authUserOCRService;
	/**
	 * 列表
	 */
	@SysLog("查看申请单列表")
	@RequestMapping("/list")
	@RequiresPermissions("auth:request:list")
	public R list(@RequestParam Map<String, Object> params){
		//商户号
		String merchantno = getMerchantNo();
		//查询列表数据
        Query query = new Query(params);

		List<AuthRequestVO> authRequestList = authRequestVOConverter.convert(authRequestService.queryList(query));
		int total = authRequestService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(authRequestList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil).put("merchantno", merchantno);
	}


	/**
	 * 列表
	 */
	@SysLog("申请单导出")
	@RequestMapping("/export")
	@RequiresPermissions("auth:request:export")
	public void exportList(@RequestParam Map<String, Object> params, HttpServletResponse response){
		//查询列表数据
		Query query = new Query(params);

		List<AuthRequestVO> authRequestList = authRequestVOConverter.convert(authRequestService.queryList(query));

		exportAuthRequests(authRequestList, response);
	}

	/**
	 * 列表
	 */
	@SysLog("申请单导出")
	@RequestMapping("/export_by_id")
	@RequiresPermissions("auth:request:export")
	public void exportListById(String requestIds, HttpServletResponse response){
		Map<String, Object> params = new HashMap<>();
		params.put("requestIds", CharUtils.splitToArrayList(requestIds, ","));
		Query query = new Query(params);

		List<AuthRequestVO> authRequestList = authRequestVOConverter.convert(authRequestService.queryList(query));
		exportAuthRequests(authRequestList, response);
	}

	private void exportAuthRequests(List<AuthRequestVO> requestVOList, HttpServletResponse response) {
		String filename = "auth_request_list_" + System.currentTimeMillis();

		// Set the content type and attachment header.
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/msexcel");// 设置为下载application/x-download
		response.setHeader("Content-Disposition", "inline;filename=\""
				+ filename + ".xls\"");

		List<Object[]> dataList = new ArrayList<>();
		Object[] header = new Object[] {"姓名", "手机", "渠道", "申请时间", "审核时间", "审核员", "处理员", "状态"};
		dataList.add(header);

		for (AuthRequestVO requestVO : requestVOList) {
			Object[] row = new String[] {
					requestVO.getName(),
					requestVO.getMobile(),
					requestVO.getChannelName(),
					requestVO.getCreateTime(),
					requestVO.getUpdateTime(),
					requestVO.getProcessorUserName(),
					requestVO.getAssigneeUserName(),
					requestVO.getStatus().getDisplayName()
			};
			dataList.add(row);
		}

		try {
			poiUtils.createExcel("申请单列表", dataList, response.getOutputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * 所有状态
     */
    @RequestMapping("/status/list")
    @RequiresPermissions(value = {"auth:request:list", "opt:channel:order_list"}, logical = Logical.OR)
    public R listRequestStatus(){
		return R.ok().put("list", RequestStatus.values());
    }

	/**
	 * 信息
	 */
	@RequestMapping("/info/{requestUuid}")
	@RequiresPermissions("auth:request:info")
	public R info(@PathVariable("requestUuid") String requestUuid){
		AuthRequestEntity authRequest = authRequestService.queryByUuid(requestUuid);

		AuthUserEntity authUser = authUserService.queryObject(authRequest.getUserId());

		int count = authRequestService.queryRequestCount(authUser.getUserId(), authRequest.getRequestId());

		List<AuthRequestHistoryVO> histories = authRequestHistoryVOConverter.convert(
				authRequestHistoryService.queryHistories(authRequest.getRequestId()));
		//查询多头报告获取身份证上地址
		AuthYouDunReportVO youduInfoVO = new AuthYouDunReportVO();
		String jsonPth  = authUser.getAuthReportJsonUrl();
		if(StringUtils.isNotBlank(jsonPth) && !StringUtils.startsWithIgnoreCase(jsonPth, "http"))
		{
			//有时候会存储null
			youduInfoVO =  youDunService.getYoudunInfo(jsonPth);
		}
		//查询出借人列表
		List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
		//查找借款申请人用户信息
		if(authUser!=null)
		{
			String merchantNo = authUser.getMerchantNo(); //商户号

			Map<String, Object> map = new HashMap<>();
			map.put("merchantNo",merchantNo);
			Query query = new Query(map);
			//查询出借人
			txlenderList = txLenderService.queryListByMerchantNo(query);

			if (authUser.isAuthStatus()) {

				/*
				// 旧多头照片使用http
				// 新多头照片使用oss
				// 此处兼容
				if (StringUtils.isNotBlank(authUser.getIdUrl1()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl1(), "http")) {
					authUser.setIdUrl1(youDunService.getCloudStorageUrl(authUser.getIdUrl1()));
				}
				if (StringUtils.isNotBlank(authUser.getIdUrl2()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl2(), "http")) {
					authUser.setIdUrl2(youDunService.getCloudStorageUrl(authUser.getIdUrl2()));
				}
				if (StringUtils.isNotBlank(authUser.getIdUrl3()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl3(), "http")) {
					authUser.setIdUrl3(youDunService.getCloudStorageUrl(authUser.getIdUrl3()));
				}*/

				AuthUserOCREntity authUserOCREntity =  authUserOCRService.queryByUserId(authUser.getUserId());
				if(authUserOCREntity!=null){

					authUser.setIdUrl1(authUserOCREntity.getIdUrl1());
					authUser.setIdUrl2(authUserOCREntity.getIdUrl2());
					authUser.setIdUrl3(authUserOCREntity.getIdUrl3());
				}

			}
		}
		boolean ower = false;
		boolean isBuy = false;
		if(!ower && !isBuy) {
			if(null != authRequest) {
				authRequest.setContact1Mobile("******");
				authRequest.setContact2Mobile("******");
				authRequest.setContact3Mobile("******");
				authRequest.setContact1Name("***");
				authRequest.setContact2Name("***");
				authRequest.setContact3Name("***");
				authRequest.setName("***");
				authRequest.setIdNo("***");
				authRequest.setQqNo("******");
				authRequest.setWechatNo("******");

			}
			if(null !=authUser) {
				authUser.setContact1Mobile("******");
				authUser.setContact2Mobile("******");
				authUser.setContact3Mobile("******");
				authUser.setContact1Name("***");
				authUser.setContact2Name("***");
				authUser.setContact3Name("***");
				authUser.setName("***");
				authUser.setIdNo("***");
				authUser.setQqNo("******");
				authUser.setWechatNo("******");
				authUser.setMobile("******");
			}
			if(null!=youduInfoVO) {
				youduInfoVO.setName("***");
			}
		}
		return R.ok().put("request", authRequest)
				.put("user", authUser)
				.put("count", count)
				.put("histories", histories)
				.put("youduInfoVO", youduInfoVO)
				.put("ydEnabled", channelService.isYouDaoCreditEnabled(authRequest.getMerchantNo()))
				.put("txlenderList",txlenderList);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/req/info/{requestUuid}")
	@RequiresPermissions("auth:request:info")
	public R reqInfo(@PathVariable("requestUuid") String requestUuid){
		AuthRequestEntity authRequest = authRequestService.queryByUuid(requestUuid);

		AuthUserEntity authUser = authUserService.queryObject(authRequest.getUserId());
		int count = authRequestService.queryRequestCount(authUser.getUserId(), authRequest.getRequestId());

		List<AuthRequestHistoryVO> histories = authRequestHistoryVOConverter.convert(
				authRequestHistoryService.queryHistories(authRequest.getRequestId()));
		//查询多头报告获取身份证上地址
		AuthYouDunReportVO youduInfoVO = new AuthYouDunReportVO();
		String jsonPth  = authUser.getAuthReportJsonUrl();
		if(StringUtils.isNotBlank(jsonPth) && !StringUtils.startsWithIgnoreCase(jsonPth, "http"))
		{
			//有时候会存储null
			youduInfoVO =  youDunService.getYoudunInfo(jsonPth);
		}
		//查询出借人列表
		List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
		//查找借款申请人用户信息
		if(authUser!=null)
		{
			String merchantNo = authUser.getMerchantNo(); //商户号

			Map<String, Object> map = new HashMap<>();
			map.put("merchantNo",merchantNo);
			Query query = new Query(map);
			//查询出借人
			txlenderList = txLenderService.queryListByMerchantNo(query);

			if (authUser.isAuthStatus()) {
				// 旧多头照片使用http
				// 新多头照片使用oss
				// 此处兼容
				if (StringUtils.isNotBlank(authUser.getIdUrl1()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl1(), "http")) {
					authUser.setIdUrl1(youDunService.getCloudStorageUrl(authUser.getIdUrl1()));
				}
				if (StringUtils.isNotBlank(authUser.getIdUrl2()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl2(), "http")) {
					authUser.setIdUrl2(youDunService.getCloudStorageUrl(authUser.getIdUrl2()));
				}
				if (StringUtils.isNotBlank(authUser.getIdUrl3()) && !StringUtils.startsWithIgnoreCase(authUser.getIdUrl3(), "http")) {
					authUser.setIdUrl3(youDunService.getCloudStorageUrl(authUser.getIdUrl3()));
				}
			}
		}
		boolean ower = false;
		boolean isBuy = false;
		if(null != authRequest) {
			String merchantNo = authUser.getMerchantNo(); //商户号
			String merchanNo = authRequest.getMerchantNo();
			if(null!=merchantNo &&merchantNo.equals(merchanNo)) {
				ower = true;
			}
			Long assigneeId = authRequest.getAssigneeId();
			if(null != assigneeId) {
				isBuy = true;
			}

		}
		if(!ower && !isBuy) {
			if(null != authRequest) {
				authRequest.setContact1Mobile("******");
				authRequest.setContact2Mobile("******");
				authRequest.setContact3Mobile("******");
				authRequest.setContact1Name("***");
				authRequest.setContact2Name("***");
				authRequest.setContact3Name("***");
				authRequest.setName("***");
				authRequest.setIdNo("***");
				authRequest.setQqNo("******");
				authRequest.setWechatNo("******");
			}
			if(null !=authUser) {
				authUser.setContact1Mobile("******");
				authUser.setContact2Mobile("******");
				authUser.setContact3Mobile("******");
				authUser.setContact1Name("***");
				authUser.setContact2Name("***");
				authUser.setContact3Name("***");
				authUser.setName("***");
				authUser.setIdNo("***");
				authUser.setQqNo("******");
				authUser.setWechatNo("******");
			}
			if(null!=youduInfoVO) {
				youduInfoVO.setName("***");
			}
		}
		return R.ok().put("request", authRequest)
				.put("user", authUser)
				.put("count", count)
				.put("histories", histories)
				.put("youduInfoVO", youduInfoVO)
				.put("ydEnabled", channelService.isYouDaoCreditEnabled(authRequest.getMerchantNo()))
				.put("txlenderList",txlenderList);
	}
	/**
	 * 信息
	 */
	@RequestMapping("/contact/{userId}")
	@RequiresPermissions("auth:request:info")
	public R contactInfo(@PathVariable("userId") Long userId){

        Map<String, String> contactMap = authUserContactService.getContacts(userId);

        if (MapUtils.isNotEmpty(contactMap)) {
            List<Map<String, String>> results = new ArrayList<>();
            for (Map.Entry<String, String> entry : contactMap.entrySet()) {
                HashMap<String, String> contact = new HashMap<>();
                contact.put("mobile", entry.getKey());
                contact.put("name", entry.getValue());
                results.add(contact);
            }

            return R.ok().put("contacts", results);
        }

		return R.ok();
	}

	/**
	 * 分配
	 */
	@SysLog("分配申请单")
	@RequestMapping("/allocate")
	@RequiresPermissions("auth:request:allocate")
	public R allocateAuditor(@RequestParam(value = "requestIds[]") Long[] requestIds, Long auditorUserId){
		return authRequestService.allocateAuditor(auditorUserId, requestIds);
	}

	/**
	 * 保存
	 */
	@SysLog("处理申请单")
	@RequestMapping("/status/{requestUuid}")
	@RequiresPermissions("auth:request:process")
	public R updateStatus(@PathVariable(value = "requestUuid") String requestUuid, int status, String userRemark,
						  String adminRemark){
		return authRequestService.updateStatus(requestUuid, RequestStatus.valueOf(status), userRemark, adminRemark);
	}

	/**
	 * 修改
	 */
	@SysLog("修改申请单")
	@RequestMapping("/update")
	@RequiresPermissions("auth:request:update")
	public R update(@RequestBody AuthRequestEntity authRequest){
		authRequestService.update(authRequest);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除申请单")
	@RequestMapping("/delete")
	@RequiresPermissions("auth:request:delete")
	public R delete(@RequestBody Long[] requestIds){
		authRequestService.deleteBatch(requestIds);
		
		return R.ok();
	}


	/**
	 * 信息
	 */
	@RequestMapping("/latest/{idNo}")
	@RequiresPermissions("auth:request:info")
	public R getLatestByIdNo(@PathVariable("idNo") String idNo, String channelId){
		AuthRequestEntity authRequest = authRequestService.queryLatestByIdNo(idNo, null);

		Object result = null;
        if (authRequest != null) {
			authRequest.setMobilePass(null);

			String baseUrl = HttpContextUtils.getRequestBaseUrl() + "/userimg/";
			if (StringUtils.isNotBlank(authRequest.getIdUrl1())
				&& !StringUtils.startsWithIgnoreCase(authRequest.getIdUrl1(), "http")) {
				authRequest.setIdUrl1(baseUrl + authRequest.getIdUrl1());
			}
			if (StringUtils.isNotBlank(authRequest.getIdUrl2())
					&& !StringUtils.startsWithIgnoreCase(authRequest.getIdUrl2(), "http")) {
				authRequest.setIdUrl2(baseUrl + authRequest.getIdUrl2());
			}
			if (StringUtils.isNotBlank(authRequest.getIdUrl3())
					&& !StringUtils.startsWithIgnoreCase(authRequest.getIdUrl3(), "http")) {
				authRequest.setIdUrl3(baseUrl + authRequest.getIdUrl3());
			}
			result = authRequestIntegrationDTOConverter.convert(authRequest);
		}
		return R.ok().put("request", result);
	}


	/**
	 * 保存
	 */
	@RequestMapping("/recommend")
	@RequiresPermissions("auth:request:recommend")
	public R recommend(@RequestParam(value = "requestIds[]") Long[] requestIds, Long channelId, String merchantNo){

	    if (!StringUtils.equalsIgnoreCase(ShiroUtils.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)) {
	        return R.error("您没有此操作权限");
        }

        if (StringUtils.isNotBlank(merchantNo)) {
	    	ChannelEntity channelEntity = channelService.queryMerchantDefaultChannel(merchantNo);
	    	if (channelEntity != null) {
	    		channelId = channelEntity.getChannelId();
			}
		}

		int errorCount = 0;
		int successCount = 0;
		if (requestIds != null) {
			for (Long requestId : requestIds) {
				R r = authRequestService.copyRequestToOtherChannel(requestId, channelId);
				if (r.getCode() == 0) {
					successCount++;
				} else {
					errorCount++;
				}
			}
		}

		return R.ok().put("successCount", successCount).put("errorCount", errorCount);
	}



	/**
	 * 列表
	 */
	@RequestMapping("/sum/today")
	@RequiresPermissions("auth:request:list")
	public R todaySum(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

		String today = DateUtils.formateDate(new Date());
		query.put("startDate", today);
        query.put("endDate", today);

		return R.ok().put("sum", authRequestService.querySummary(query));
	}


	/**
	 * 列表
	 */
	@RequestMapping("/sum/history")
	@RequiresPermissions("auth:request:list")
	public R historySum(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);

        return R.ok().put("sum", authRequestService.querySummary(query));
	}

}
