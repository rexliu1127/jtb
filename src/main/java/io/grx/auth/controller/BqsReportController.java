package io.grx.auth.controller;

import com.google.common.collect.Interners;
import io.grx.auth.service.BaiQiShiService;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.auth.entity.BqsReportEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.*;
import io.grx.modules.auth.service.AuthUserService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.Interner;
import io.grx.modules.auth.service.BqsReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.lang.StringUtils;
import io.grx.modules.auth.entity.AuthUserEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2019-01-24 10:41:24
 */
@RestController
@RequestMapping("/bqs")
public class BqsReportController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${baiqishi.userPrefix}")
	private String userPrefix;
	@Autowired
	private BqsReportService bqsReportService;
	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private BaiQiShiService baiQiShiService;
	private Interner<String> pool;
	public BqsReportController() {
		pool = Interners.newWeakInterner();
	}

	/*@RequestMapping(value = "/return")
	public void returnApply(HttpServletRequest request) {
		JSONObject resultJSON  =new JSONObject();
		try{
			String callJson = HttpUtils.readReqStr(request);
			logger.debug("Baiqishi return. str {}=======",callJson);
			// 解析异步通知数据
			JSONObject paramsJson  =  JSONObject.fromObject(callJson);
			logger.debug("Baiqishi return. json {}",paramsJson);

			if(paramsJson != null) {
				String msgCode = paramsJson.getString("msgCode");
				String msgDesc = paramsJson.getString("msgDesc");
				String dataType = paramsJson.getString("dataType");
				String name = paramsJson.getString("name");
				String mobile = paramsJson.getString("mobile");
				String certNo = paramsJson.getString("certNo");
				String customParam = paramsJson.getString("customParam");
				BaiqishiType baiqishiType = BaiqishiType.valueOf(StringUtils.upperCase(dataType));
				String customParamStr = "";
				try {
					customParamStr = URLDecoder.decode(URLDecoder.decode(customParam, "UTF-8"), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				JSONObject customParamStrJSON = JSONObject.fromObject(customParamStr);
				String outUniqueId = customParamStrJSON.getString("outUniqueId");
				String userId = customParamStrJSON.getString("userId");
				logger.debug("Baiqishi return. outUniqueId {}=======",outUniqueId);
				logger.debug("Baiqishi return. userId {}=======",userId);
				synchronized (pool.intern(outUniqueId)) {
					BqsReportEntity bqsReportEntity = bqsReportService.queryByUniqueId(outUniqueId);
					if (bqsReportEntity == null) {
						bqsReportEntity = new BqsReportEntity();
						Long authUserId = NumberUtils.toLong(StringUtils.substringAfter(userId, userPrefix));
						AuthUserEntity userEntity = authUserService.queryObject(authUserId);
						bqsReportEntity.setCreateTime(new Date());
						bqsReportEntity.setUserId(userEntity.getUserId());
						bqsReportEntity.setTaskId(outUniqueId);
						bqsReportEntity.setIdNo(userEntity.getIdNo());
						bqsReportEntity.setName(userEntity.getName());
						bqsReportEntity.setMobile(userEntity.getMobile());
						bqsReportEntity.setBaiqishiType(baiqishiType);
						bqsReportEntity.setUpdateTime(new Date());
						bqsReportEntity.setVerifyStatus(VerifyStatus.PROCESSING);
						if (bqsReportEntity.getId() == null) {
							bqsReportService.save(bqsReportEntity);
						} else {
							bqsReportService.update(bqsReportEntity);
						}
					}
				}
			}
		}catch (Exception e){
			logger.error("Baiqishi return--",e);
			e.printStackTrace();
		}
	}*/
	/**
	 * 白骑士认证爬取回传
	 *
	 * @return
	 */
	@RequestMapping(value = "/callback")
	public String callback(HttpServletRequest request) {
		JSONObject resultJSON  =new JSONObject();
		try{
			String callJson = HttpUtils.readReqStr(request);
			logger.debug("Baiqishi callback. str {}=======",callJson);
			// 解析异步通知数据
			JSONObject paramsJson  =  JSONObject.fromObject(callJson);
			logger.debug("Baiqishi callback. json {}",paramsJson);

			if(paramsJson != null){
				String msgCode=paramsJson.getString("msgCode");
				String msgDesc=paramsJson.getString("msgDesc");
				String dataType=paramsJson.getString("dataType");
				String name=paramsJson.getString("name");
				String mobile=paramsJson.getString("mobile");
				String certNo=paramsJson.getString("certNo");
				BaiqishiType baiqishiType = BaiqishiType.valueOf(StringUtils.upperCase(dataType));
				BaiqishiState baiqishiState=null;
				if("CCOM1000".equals(msgCode)||"CCOM3074".equals(msgCode)){//成功
					baiqishiState = BaiqishiState.CRAWL;
				}else if("CCOM3016".equals(msgCode)){
					baiqishiState = BaiqishiState.CRAWL_FAIL;
				}
				Map<String, Object> params = new HashMap<>();
				params.put("id_no", certNo);
				params.put("mobile", mobile);
				params.put("name", name);
				params.put("serchNow", "true");
				List<BqsReportEntity> bqsReportEntityList = bqsReportService.queryObjectByCondition(params);
				if(!bqsReportEntityList.isEmpty()){
					for (BqsReportEntity bqsReportEntity:bqsReportEntityList){
						if((bqsReportEntity.getReportHtmlPath()==null||"".equals(bqsReportEntity.getReportHtmlPath()))&&(bqsReportEntity.getReportJsonPath()==null||"".equals(bqsReportEntity.getReportJsonPath()))){
							synchronized (pool.intern(bqsReportEntity.getId().toString())) {
								if (bqsReportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
									System.out.println("Duplicated callback. ignore it.");
									resultJSON.put("resultCode","CCOM8999");
									resultJSON.put("resultDesc","失败");
									return resultJSON.toString();
								}
								bqsReportEntity.setBaiqishiState(baiqishiState);
								bqsReportEntity.setUpdateTime(new Date());
								if (baiqishiState == BaiqishiState.CRAWL_FAIL|| baiqishiState == BaiqishiState.REPORT_FAIL) {
									bqsReportEntity.setVerifyStatus(VerifyStatus.FAILED);
								} else {
									bqsReportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
								}
								if (baiqishiState == BaiqishiState.CRAWL && baiqishiType == BaiqishiType.MNO) {
									if((bqsReportEntityList.get(0).getReportHtmlPath()==null||"".equals(bqsReportEntityList.get(0).getReportHtmlPath()))&&(bqsReportEntityList.get(0).getReportJsonPath()==null||"".equals(bqsReportEntityList.get(0).getReportJsonPath()))){
										String report_html_path=baiQiShiService.getMobileRawReport(bqsReportEntity);
										bqsReportEntity.setReportHtmlPath(report_html_path);
										/*String report_json_path=baiQiShiService.getMobileRawReportJson(bqsReportEntity);
										bqsReportEntity.setReportJsonPath(report_json_path);*/
									}else{
										bqsReportEntity.setReportHtmlPath(bqsReportEntityList.get(0).getReportHtmlPath());
										bqsReportEntity.setReportJsonPath(bqsReportEntityList.get(0).getReportJsonPath());
									}
								}
								if (bqsReportEntity.getId() == null) {
									bqsReportService.save(bqsReportEntity);
								} else {
									bqsReportService.update(bqsReportEntity);
								}
							}
						}
					}
				}
			}
			resultJSON.put("resultCode","CCOM1000");
			resultJSON.put("resultDesc","成功");
			return resultJSON.toString();
		}catch (Exception e){

			logger.error("failed to baiqishi callback", e);

			resultJSON.put("resultCode","CCOM8999");
			resultJSON.put("resultDesc","失败");
			return resultJSON.toString();
		}
	}

	/**
	 * 手动获取咨询云
	 * @param name
	 * @param mobile
	 * @param certNo
	 * @return
	 */
	@RequestMapping(value = "/getBqsMnoReport")
	public String getBqsMnoReport(String name,String mobile,String certNo) {
		JSONObject resultJSON  =new JSONObject();
		if(name==null||"".equals(name)||mobile==null||"".equals(mobile)||certNo==null||"".equals(certNo)){
			resultJSON.put("resultCode","CCOM8999");
			resultJSON.put("resultDesc","请输入重新获取运营商报告的姓名、手机号、身份证号");
			return resultJSON.toString();
		}
		try{
			BaiqishiState baiqishiState=BaiqishiState.CRAWL;
			Map<String, Object> params = new HashMap<>();
			params.put("id_no", certNo);
			params.put("mobile", mobile);
			params.put("name", name);
			List<BqsReportEntity> bqsReportEntityList = bqsReportService.queryObjectByCondition(params);
			if(!bqsReportEntityList.isEmpty()) {
				for (BqsReportEntity bqsReportEntity : bqsReportEntityList) {
					if ((bqsReportEntity.getReportHtmlPath() == null || "".equals(bqsReportEntity.getReportHtmlPath())) && (bqsReportEntity.getReportJsonPath() == null || "".equals(bqsReportEntity.getReportJsonPath()))) {
						synchronized (pool.intern(bqsReportEntity.getId().toString())) {
							bqsReportEntity.setBaiqishiState(baiqishiState);
							bqsReportEntity.setUpdateTime(new Date());

							if (baiqishiState == BaiqishiState.CRAWL_FAIL|| baiqishiState == BaiqishiState.REPORT_FAIL) {
								bqsReportEntity.setVerifyStatus(VerifyStatus.FAILED);
							} else {
								bqsReportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
							}
							String report_html_path=baiQiShiService.getMobileRawReport(bqsReportEntity);
							bqsReportEntity.setReportHtmlPath(report_html_path);
							/*String report_json_path=baiQiShiService.getMobileRawReportJson(bqsReportEntity);
							bqsReportEntity.setReportJsonPath(report_json_path);*/
							if (bqsReportEntity.getId() == null) {
								bqsReportService.save(bqsReportEntity);
							} else {
								bqsReportService.update(bqsReportEntity);
							}
						}
					}
				}
			}
			resultJSON.put("resultCode","CCOM1000");
			resultJSON.put("resultDesc","成功");
			return resultJSON.toString();
		}catch (Exception e){

			logger.error("failed to baiqishi callback", e);

			resultJSON.put("resultCode","CCOM8999");
			resultJSON.put("resultDesc","失败");
			return resultJSON.toString();
		}
	}
}
