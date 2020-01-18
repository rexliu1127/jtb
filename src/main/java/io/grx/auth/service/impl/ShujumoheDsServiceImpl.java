package io.grx.auth.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.auth.service.ShujumoheDsService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.DsType;

@Service
@ConfigurationProperties(prefix = "shujumohe")
public class ShujumoheDsServiceImpl implements ShujumoheDsService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String partnerCode;
    private String partnerKey;
    private String host;
    private String createTaskPath;
    private String verifyDsPath;
    private String retryTaskPath;
    private String queryTaskPath;
    private String queryReportPath;

    @Override
    public R createTask(final AuthUserEntity authUser, final DsType dsType) {
        R result = createTask(authUser.getName(), authUser.getIdNo(), authUser.getMobile(), "DS",
                dsType == DsType.JD ? "000001" : "000002");
        return result;
    }

    private R createTask(final String realName, String identityCode, String mobile, final String channelType, String
            channelCode) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("channel_type", channelType);
        requestMap.put("channel_code", channelCode);
        requestMap.put("real_name", realName);
        requestMap.put("identity_code", identityCode);
        requestMap.put("user_mobile", mobile);

        return postRequest(createTaskPath, requestMap);
    }

    @Override
    public R initVerify(final String taskId, final String userName, final String password, String taskStage,
                         String requestType) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);
        requestMap.put("user_name", userName);
        requestMap.put("user_pass", password);
        requestMap.put("login_type", "0");
        requestMap.put("task_stage", "INIT");
        requestMap.put("request_type", requestType);

        return postRequest(verifyDsPath, requestMap);
    }

    @Override
    public R submitVerify(final String taskId, final String smsCode, final String authCode, String taskStage,
                        String requestType) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);
        requestMap.put("sms_code", smsCode);
        requestMap.put("auth_code", authCode);
        requestMap.put("task_stage", taskStage);
        requestMap.put("request_type", requestType);

        return postRequest(verifyDsPath, requestMap);
    }

    @Override
    public String queryTaskResult(final String taskId) {

        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);

        try {
            String responseJson = postJsonDataAsString(queryTaskPath, requestMap);

            return responseJson;
        } catch (Exception e) {
            logger.error("Failed to get response as json text", e);
        }

        return null;
    }

    @Override
    public int queryTaskResultCode(final String taskId) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);

        try {
            String responseJson = postJsonDataAsString(queryTaskPath, requestMap);

            Map<String, Object> responseMap = new ObjectMapper().readValue(responseJson,
                    new TypeReference<Map<String, Object>>() {
                    });
            return MapUtils.getIntValue(responseMap, "code", -1);
        } catch (Exception e) {
            logger.error("Failed to get response as json text", e);
        }
        return -1;
    }

    private String getPath(String path) {
        return path + String.format("?partner_code=%1$s&partner_key=%2$s",
                partnerCode, partnerKey);
    }

    private R postRequest(String path, Map<String, String> requestMap) {
        try {
            Map<String, Object> responseMap = postJsonData(path, requestMap);
            return R.ok().put("remoteRes", responseMap);
        } catch (Exception e) {
            logger.error("Error when creating task", e);
            return R.error("系统错误, 请稍后再试!");
        }
    }

    private Map<String, Object> postJsonData(String path, Map<String, String> requestMap) throws Exception {
        String pathWithPartnerInfo = getPath(path);
        logger.info("Shujumohe request. URL: {}, parameters: {}", pathWithPartnerInfo, requestMap);

        final HttpResponse response = HttpUtils.doPost(host, pathWithPartnerInfo,
                MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, requestMap);

        final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        logger.info("Shujumohe response json: {}", responseJson);

        return new ObjectMapper().readValue(responseJson,
                new TypeReference<Map<String, Object>>() {
                });
    }

    private String postJsonDataAsString(String path, Map<String, String> requestMap) throws Exception {
        String pathWithPartnerInfo = getPath(path);
        logger.info("Shujumohe request. URL: {}, parameters: {}", pathWithPartnerInfo, requestMap);

        final HttpResponse response = HttpUtils.doPost(host, pathWithPartnerInfo,
                MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, requestMap);

        final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        logger.info("Shujumohe response json: {}", responseJson);

        return responseJson;
    }

    public void setPartnerCode(final String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public void setPartnerKey(final String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public void setCreateTaskPath(final String createTaskPath) {
        this.createTaskPath = createTaskPath;
    }

    public void setRetryTaskPath(final String retryTaskPath) {
        this.retryTaskPath = retryTaskPath;
    }

    public void setQueryTaskPath(final String queryTaskPath) {
        this.queryTaskPath = queryTaskPath;
    }

    public void setQueryReportPath(final String queryReportPath) {
        this.queryReportPath = queryReportPath;
    }

    public void setVerifyDsPath(final String verifyDsPath) {
        this.verifyDsPath = verifyDsPath;
    }
}
