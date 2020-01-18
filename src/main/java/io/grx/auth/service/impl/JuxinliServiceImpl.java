package io.grx.auth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.auth.service.JuxinliService;
import io.grx.common.utils.CacheUtils;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.AuthVendorType;
import io.grx.modules.auth.enums.ContactType;
import io.grx.modules.auth.service.AuthRequestService;


@Service
public class JuxinliServiceImpl implements JuxinliService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${juxinli.orgCode}")
    private String orgCode;
    @Value("${juxinli.host}")
    private String host;
    @Value("${juxinli.clientSecret}")
    private String clientSecret;
    @Value("${juxinli.applicationsPath}")
    private String applicationsPath;
    @Value("${juxinli.messagesPath}")
    private String messagesPath;
    @Value("${juxinli.tokenPath}")
    private String tokenPath;
    @Value("${juxinli.reportDataPath}")
    private String reportDataPath;
    @Value("${juxinli.rawDataPath}")
    private String rawDataPath;
    @Value("${juxinli.jobStatusPath}")
    private String jobStatusPath;

    @Autowired
    private CacheUtils cacheUtils;

    @Autowired
    private AuthRequestService authRequestService;

    // token有效时间
    private static final int TOKEN_CACHE_HOUR = 24;

    @Override
    public R sendApplications(final AuthUserEntity authUser) {
//        {
//            "skip_mobile": false,
//                "basic_info": {
//            "name": "黄文业",
//                    "id_card_num": "440921198306038036",
//                    "cell_phone_num": "18928446641"
//        },
//            "contacts": []
//        }
        final Map<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("skip_mobile", false);

        final Map<String, Object> basicInfo = new LinkedHashMap<>();
        basicInfo.put("name", authUser.getName());
        basicInfo.put("id_card_num", authUser.getIdNo());
        basicInfo.put("cell_phone_num", authUser.getMobile());

        requestMap.put("basic_info", basicInfo);

        final List<Map<String, Object>> contacts = new ArrayList<>();
        if (StringUtils.isNotBlank(authUser.getContact1Name())) {
            final Map<String, Object> contact = new LinkedHashMap<>();
            contact.put("contact_name", authUser.getContact1Name());
            contact.put("contact_tel", authUser.getContact1Mobile());

            String contactType = "";
            if (authUser.getContact1Type() == ContactType.SPOUSE) {
                contactType = "0";
            } else if (authUser.getContact1Type() == ContactType.PARENT) {
                contactType = "1";
            } else if (authUser.getContact1Type() == ContactType.RELATIVE) {
                contactType = "2";
            }
            contact.put("contact_type", contactType);
            contacts.add(contact);
        }

        if (StringUtils.isNotBlank(authUser.getContact2Name())) {
            final Map<String, Object> contact = new LinkedHashMap<>();
            contact.put("contact_name", authUser.getContact2Name());
            contact.put("contact_tel", authUser.getContact2Mobile());

            String contactType = "";
            if (authUser.getContact2Type() == ContactType.SPOUSE) {
                contactType = "0";
            } else if (authUser.getContact2Type() == ContactType.PARENT) {
                contactType = "1";
            } else if (authUser.getContact2Type() == ContactType.RELATIVE) {
                contactType = "2";
            }
            contact.put("contact_type", contactType);
            contacts.add(contact);
        }

        requestMap.put("contacts", contacts);

        try {
            final Map<String, Object> responseMap = postJsonData(applicationsPath + "/" + orgCode, requestMap);

            String message = MapUtils.getString(responseMap, "message");
            boolean result = MapUtils.getBooleanValue(responseMap, "success", false);
            if (!result) {
                return R.error(message);
            }

            Map<String, Object> data = MapUtils.getMap(responseMap, "data", MapUtils.EMPTY_MAP);
            String token = MapUtils.getString(data, "token");

            Map<String, Object> datasource = MapUtils.getMap(data, "datasource", MapUtils.EMPTY_MAP);
            String website = MapUtils.getString(datasource, "website");
            String websiteName = MapUtils.getString(datasource, "name");
            String category = MapUtils.getString(datasource, "category");

            authRequestService.createAuthRequest(token, AuthVendorType.JXL, HttpContextUtils.getCookieValue
                    ("channelId", ""));

            return R.ok().put("token", token)
                    .put("website", website)
                    .put("websiteName", websiteName)
                    .put("category", category);
        } catch (final Throwable t) {
            logger.error("error in send applications", t);
            return R.error("发送错误, 请联系客服. 参考编号 010001");
        }
    }

    @Override
    public R sendMessages(final String mobile, final String token, final String website, final String password,
                          final String queryPwd, String captcha, final String type) {
//        {
//            "token": "a42f92dc13f04dec85cb7ee9fe9e5421",
//                "account":"18928446641",
//                "password":"123456",
//                "captcha":"",
//                "type":"",
//                "website":"chinatelecomgd"
//        }
        final Map<String, Object> requestMap = new LinkedHashMap<>();
        requestMap.put("token", token);
        requestMap.put("account", mobile);
        requestMap.put("password", password);
        requestMap.put("captcha", StringUtils.defaultIfBlank(captcha, ""));
        if (StringUtils.isNotBlank(queryPwd)) {
            requestMap.put("queryPwd", queryPwd);
        }
        requestMap.put("type", StringUtils.defaultIfBlank(type, ""));
        requestMap.put("website", website);

        try {
            final Map<String, Object> responseMap = postJsonData(messagesPath, requestMap);

            String message = MapUtils.getString(responseMap, "message");
            boolean result = MapUtils.getBooleanValue(responseMap, "success", false);
            if (!result) {
                return R.error(message);
            }

            return R.ok()
                    .put("data", MapUtils.getMap(responseMap, "data", MapUtils.EMPTY_MAP));
        } catch (final Throwable t) {
            logger.error("error in send applications", t);
            return R.error("发送错误, 请联系客服. 参考编号 010002");
        }
    }

    @Override
    public String getReportDataByToken(final String token) {
        Map<String, String> queryMap = buildQueryByTokenMap(token);

        try {
            HttpResponse response = HttpUtils.doGet(host, reportDataPath, buildJsonRequestHeader(), queryMap);

            final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("JXL report data response json: {}", responseJson);

            return responseJson;
        } catch (Exception e) {
            logger.error("Failed to get report data", e);
        }

        return null;
    }

    @Override
    public String getRawDataByToken(final String token) {
        Map<String, String> queryMap = buildQueryByTokenMap(token);

        try {
            HttpResponse response = HttpUtils.doGet(host, rawDataPath, buildJsonRequestHeader(), queryMap);

            final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("JXL raw data response json: {}", responseJson);

            return responseJson;
        } catch (Exception e) {
            logger.error("Failed to get report data", e);
        }

        return null;
    }

    @Override
    public boolean isJobCompleted(final String token) {
        Map<String, String> queryMap = buildQueryByTokenMap(token);

        try {
            HttpResponse response = HttpUtils.doGet(host, jobStatusPath, buildJsonRequestHeader(), queryMap);

            final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("JXL job status response json: {}", responseJson);

            Map<String, Object> responseMap = new ObjectMapper().readValue(responseJson,
                    new TypeReference<Map<String, Object>>() {});

            if (StringUtils.equals(MapUtils.getString(responseMap, "code"), "30015")) {
                Map<String, Object> dataMap = MapUtils.getMap(responseMap, "data");
                List<Map<String, Object>> details = (List<Map<String, Object>>) dataMap.get("details");
                for (Map<String, Object> detail : details) {
                    if (StringUtils.equalsIgnoreCase(MapUtils.getString(detail, "category"), "mobile")) {
                        if (StringUtils.equalsIgnoreCase(MapUtils.getString(detail, "websiteStatus"), "SUCCESS")) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get report data", e);
        }

        return false;
    }

    private String getAccessToken() {
        String cacheKey = "jxl:token";
        String token = cacheUtils.get(cacheKey);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }

        Map<String, String> headers = buildJsonRequestHeader();

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("client_secret", clientSecret);
        queryMap.put("hours", String.valueOf(TOKEN_CACHE_HOUR));
        queryMap.put("org_name", orgCode);

        try {
            HttpResponse response = HttpUtils.doGet(host, tokenPath, headers, queryMap);

            final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("JXL access token response json: {}", responseJson);

            Map<String, Object> responseMap = new ObjectMapper().readValue(responseJson,
                    new TypeReference<Map<String, Object>>() {});
            if (MapUtils.getIntValue(responseMap, "code", -1) == 200) {
                token = MapUtils.getString(responseMap, "access_token");
                cacheUtils.put(cacheKey, token, 60 * 60 * 24);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get token", e);
        }

        return token;
    }

    private Map<String, Object> postJsonData(String path, Map<String, Object> requestMap) throws Exception {
        final String requestJson = new ObjectMapper().writeValueAsString(requestMap);
        logger.info("JXL request json: {}", requestJson);

        Map<String, String> headers = buildJsonRequestHeader();

        final HttpResponse response = HttpUtils.doPost(host, path,
                headers, MapUtils.EMPTY_MAP,
                requestJson);

        final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        logger.info("JXL response json: {}", responseJson);

        return new ObjectMapper().readValue(responseJson,
                new TypeReference<Map<String, Object>>() {});
    }

    private Map<String, String> buildJsonRequestHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }

    private Map<String, String> buildQueryByTokenMap(String token) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("client_secret", clientSecret);
        queryMap.put("access_token", getAccessToken());
        queryMap.put("token", token);
        return queryMap;
    }
}
