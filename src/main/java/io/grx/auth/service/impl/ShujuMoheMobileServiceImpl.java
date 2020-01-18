package io.grx.auth.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.auth.service.ShujuMoheMobileService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.ContactType;
import io.grx.modules.auth.service.AuthRequestService;

/**
 * 数据模盒运营商数据接口
 */
@Service
@ConfigurationProperties(prefix = "shujumohe")
public class ShujuMoheMobileServiceImpl implements ShujuMoheMobileService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String partnerCode;
    private String partnerKey;
    private String host;
    private String createTaskPath;
    private String verifyMobilePath;
    private String retryTaskPath;
    private String queryTaskPath;
    private String queryReportPath;


    private static final String REQUEST_TYPE_SUBMIT = "submit";

    private static final String REQUEST_TYPE_QUERY = "query";

    @Autowired
    private AuthRequestService authRequestService;

    @Override
    public R createTask(final AuthUserEntity authUser) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("channel_type", "YYS");
        requestMap.put("channel_code", "100000");
        requestMap.put("real_name", authUser.getName());
        requestMap.put("identity_code", authUser.getIdNo());
        requestMap.put("user_mobile", authUser.getMobile());

        R result = postRequest(createTaskPath, requestMap);

        Map<String, Object> response = MapUtils.getMap(result, "remoteRes");
        if (MapUtils.getIntValue(response, "code", -1) == 0) {
            String taskId = MapUtils.getString(response, "task_id");
            authRequestService.createAuthRequest(taskId, AuthVendorType.SJMH, HttpContextUtils.getCookieValue
                    ("channelId", ""));
        }

        return result;

    }

    @Override
    public R initVerifyTask(final String taskId, final String mobile, final String password) {
        return doInitVerifyTask(taskId, mobile, password, REQUEST_TYPE_SUBMIT);
    }

    @Override
    public R queryInitVerifyTask(final String taskId, final String mobile, final String password) {
        return doInitVerifyTask(taskId, mobile, password, REQUEST_TYPE_QUERY);
    }

    @Override
    public R verifyTask(final String taskId, final String smsCode, final String authCode, final String taskStage) {
        return doVerifyTask(taskId, smsCode, authCode, taskStage, REQUEST_TYPE_SUBMIT);
    }

    @Override
    public R queryVerifyTask(final String taskId, final String smsCode, final String authCode, final String taskStage) {
        return doVerifyTask(taskId, smsCode, authCode, taskStage, REQUEST_TYPE_QUERY);
    }

    @Override
    public R retryTask(final String taskId) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);

        return postRequest(retryTaskPath, requestMap);
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

    @Override
    public String queryReportResult(final String taskId, final AuthUserEntity userEntity) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);
        requestMap.put("contact1_name", userEntity.getContact1Name());
        requestMap.put("contact1_mobile", userEntity.getContact1Mobile());
        requestMap.put("contact1_relation", getQueryRelation(userEntity.getContact1Type()));
        requestMap.put("contact2_name", userEntity.getContact2Name());
        requestMap.put("contact2_mobile", userEntity.getContact2Mobile());
        requestMap.put("contact2_relation", getQueryRelation(userEntity.getContact2Type()));

        try {
            String responseJson = postJsonDataAsString(queryReportPath, requestMap);

            Map<String, Object> responseMap = new ObjectMapper().readValue(responseJson,
                    new TypeReference<Map<String, Object>>() {
                    });
            if (MapUtils.getIntValue(responseMap, "code", -1) == 0) {
                return gunzip((String) responseMap.get("data"));
            }
        } catch (Exception e) {
            logger.error("Failed to get response as json text", e);
        }

        return null;
    }

    private String getQueryRelation(ContactType contactType) {
        if (ContactType.PARENT == contactType) {
            return "FATHER";
        } else if (ContactType.SPOUSE == contactType) {
            return "SPOUSE";
        } else if (ContactType.RELATIVE == contactType) {
            return "OTHER_RELATIVE";
        }
        return "";
    }

    private R doVerifyTask(final String taskId, final String smsCode, final String authCode,
                           final String taskStage, final String requestType) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);
        requestMap.put("sms_code", smsCode);
        requestMap.put("auth_code", authCode);
        requestMap.put("task_stage", taskStage);
        requestMap.put("request_type", requestType);

        return postRequest(verifyMobilePath, requestMap);
    }

    private R doInitVerifyTask(final String taskId, final String mobile, final String password, String requestType) {
        final Map<String, String> requestMap = new LinkedHashMap<>();
        requestMap.put("task_id", taskId);
        requestMap.put("user_name", mobile);
        requestMap.put("user_pass", password);
        requestMap.put("task_stage", "INIT");
        requestMap.put("request_type", requestType);

        return postRequest(verifyMobilePath, requestMap);
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

    private String getPath(String path) {
        return path + String.format("?partner_code=%1$s&partner_key=%2$s",
                partnerCode, partnerKey);
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

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(final String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public void setPartnerKey(final String partnerKey) {
        this.partnerKey = partnerKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getCreateTaskPath() {
        return createTaskPath;
    }

    public void setCreateTaskPath(final String createTaskPath) {
        this.createTaskPath = createTaskPath;
    }

    public String getVerifyMobilePath() {
        return verifyMobilePath;
    }

    public void setVerifyMobilePath(final String verifyMobilePath) {
        this.verifyMobilePath = verifyMobilePath;
    }

    public String getRetryTaskPath() {
        return retryTaskPath;
    }

    public void setRetryTaskPath(final String retryTaskPath) {
        this.retryTaskPath = retryTaskPath;
    }

    public String getQueryTaskPath() {
        return queryTaskPath;
    }

    public void setQueryTaskPath(final String queryTaskPath) {
        this.queryTaskPath = queryTaskPath;
    }

    public String getQueryReportPath() {
        return queryReportPath;
    }

    public void setQueryReportPath(final String queryReportPath) {
        this.queryReportPath = queryReportPath;
    }

    public String gunzip(String compressedStr) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            // 对返回数据BASE64解码
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            // 解码后对数据gzip解压缩
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }

            // 最后对数据进行utf-8转码
            decompressed = out.toString("utf-8");
        } catch (IOException e) {
            logger.error("gunzip error ", e);
        } finally {

        }
        return decompressed;
    }
}

