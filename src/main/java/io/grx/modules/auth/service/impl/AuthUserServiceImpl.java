package io.grx.modules.auth.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import io.grx.common.annotation.DataFilter;
import io.grx.common.utils.*;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.sys.service.SysFunDetailsService;
import io.grx.modules.sys.service.SysFunService;
import io.grx.modules.sys.service.SysFunTypeService;
import io.grx.modules.sys.service.SysMerchantService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.grx.modules.auth.dao.AuthUserDao;
import io.grx.modules.auth.dto.AuthStatVO;
import io.grx.modules.auth.dto.AuthUserStatVO;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthUserService;



@Service("authUserService")
public class AuthUserServiceImpl implements AuthUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${integration.auth.host}")
	private String integrationHost;
    @Value("${integration.auth.path}")
	private String integrationPath;

    @Value("${integration.auth.source}")
    private String integrationSource;

    @Autowired
	private AuthRequestService authRequestService;

	@Autowired
	private AuthUserDao authUserDao;

	@Override
	public AuthUserEntity queryObject(Long userId){
		return authUserDao.queryObject(userId);
	}
	
	@Override
	@DataFilter(tableAlias = "c", user = false)
	public List<AuthUserEntity> queryList(Map<String, Object> map){
		return authUserDao.queryList(map);
	}
	
	@Override
	@DataFilter(tableAlias = "c", user = false)
	public int queryTotal(Map<String, Object> map){
		return authUserDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(AuthUserEntity authUser){
		authUserDao.save(authUser);
	}

    @Override
	@Transactional
    public void saveWithNewRequest(AuthUserEntity authUser) {
        authUserDao.save(authUser);

        ShiroUtils.setAuthUser(authUser);

        authRequestService.createNewRequest();
    }

    @Override
	@Transactional
	public void update(AuthUserEntity authUser){

//		//多头认证成功
//		if(authUser.isAuthStatus())
//		{
//			Map<String, Object> params=new HashMap<String, Object>();
//			params.put("merchantNo",authUser.getMerchantNo());
//			params.put("funType",Constant.FUN_TYPE_DUOTOU);
//			Query query = new Query(params);
//			//查询消费类型信息
//			SysFunTypeEntity   funtype =  sysFunTypeService.queryObjectByConditions(query);
//
//			//根据商户编号查询账户总览记录
//			SysFunEntity fun  = sysFunService.queryinfo(authUser.getMerchantNo());
//
//
//			//根据商户编号和计算类型  查询单笔扣费金额,扣除可用余额
//			BigDecimal  free = funtype.getSingleFee();
//			fun.setRemainingSum(fun.getRemainingSum().subtract(free).setScale(2,BigDecimal.ROUND_HALF_UP));
//			//扣除商户的可用余额
//			sysFunService.update(fun);
//
//			//新增扣费明细
//			SysFunDetailsEntity   fundetail = new SysFunDetailsEntity();
//			fundetail.setUserId(authUser.getUserId());
//			fundetail.setBorrowerPhone(authUser.getMobile());
//			fundetail.setFunType(Constant.FUN_TYPE_DUOTOU);  //多头报告扣费
//			fundetail.setAmount(free);
//			fundetail.setUserName(authUser.getName());
//			fundetail.setMerchantNo(authUser.getMerchantNo());
//			fundetail.setCreateTime(new Date());
//			sysFunDetailsService.save(fundetail);
//		}


		authUserDao.update(authUser);
	}
	
	@Override
	@Transactional
	public void delete(Long userId){
		authUserDao.delete(userId);
	}
	
	@Override
	@Transactional
	public void deleteBatch(Long[] userIds){
		authUserDao.deleteBatch(userIds);
	}

	@Override
	public AuthUserEntity queryByMobile(final String mobile, final Long channelId) {
		return authUserDao.queryByMobile(mobile, channelId);
	}

	@Override
	public List<AuthUserStatVO> queryStatList(final Map<String, Object> map) {
		return authUserDao.queryStatList(map);
	}

	@Override
	public int queryStatTotal(final Map<String, Object> map) {
		return authUserDao.queryStatTotal(map);
	}

	@Override
	public AuthStatVO queryRequestStat() {
		return authUserDao.queryRequestStat();
	}

	@Override
	public void sendIntegrationRequest(String mobile, String channelId, String idNo, String name) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (request == null) {
            return;
        }

        String requestUrl = HttpContextUtils.getRequestBaseUrl();
        if (!StringUtils.endsWith(requestUrl, "/")) {
            requestUrl = requestUrl + "/";
        }
        if (StringUtils.contains(requestUrl, integrationSource)) {
            Map<String, String> bodys = new HashMap<>();
            bodys.put("mobile", mobile);
            bodys.put("channelId", channelId);

            if (StringUtils.isNotEmpty(idNo)) {
                bodys.put("idNo", idNo);
            }
            if (StringUtils.isNotEmpty(name)) {
                bodys.put("name", name);
            }

            try {
                logger.info("Sending integration request: {}", bodys);
                HttpResponse response = HttpUtils.doPost(integrationHost,
                        integrationPath, MapUtils.EMPTY_MAP,
                        MapUtils.EMPTY_MAP, bodys);
                String responseStr = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
                logger.info("send integration result: {}", responseStr);
            } catch (Exception e) {
                logger.error("failed to send integration request", e);
            }
        } else {
            logger.debug("no need to send integration request for: {}", requestUrl);
        }
	}

	@Override
	public List<AuthUserEntity> queryAllChannelUsersByMobile(final String mobile) {
		return authUserDao.queryAllChannelUsersByMobile(mobile);
	}

	@Override
	public AuthUserEntity queryByMerchantNoAndMobile(String mobile, String merchantNo) {
		return authUserDao.queryByMerchantNoAndMobile(mobile, merchantNo);
	}

	@Override
	public List<AuthUserEntity> queryByAuthTaskId(String taskId) {
		return authUserDao.queryAuthTaskId(taskId);
	}

	@Override
	public AuthUserEntity queryAuthByYiXiangReportTaskId(String taskId) {
		return authUserDao.queryAuthByYiXiangReportTaskId(taskId);
	}

	@Override
	public int updateAuthStatus(long userId,String name,String idNo, boolean status) {


		 AuthUserEntity authUserEntity = 	authUserDao.queryObject(userId);
		 if(authUserEntity!=null){
			 AuthUserEntity authUser = new AuthUserEntity();
			 authUser.setMerchantNo(authUserEntity.getMerchantNo());
			 authUser.setUserId(userId);
			 authUser.setName(name);
			 authUser.setIdNo(idNo);
			 authUser.setAuthStatus(status);
			 return authUserDao.update(authUser);
		 }

		 return 0;

	}
}
