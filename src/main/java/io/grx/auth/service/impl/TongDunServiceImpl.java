package io.grx.auth.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import io.grx.auth.service.TongDunService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthUserService;

@Service
@ConfigurationProperties(prefix = "tongdun")
public class TongDunServiceImpl implements TongDunService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String host;
    private String partnerCode;
    private String partnerKey;
    private String appName;
    private String bodyGuardPath;

    @Autowired
    private AuthUserService authUserService;

    @Override
    public String getBodyGuardReport(final AuthRequestEntity requestEntity) {
        Map<String, String> querys = new HashMap<>();
        querys.put("partner_code", partnerCode);
        querys.put("partner_key", partnerKey);
        querys.put("app_name", appName);

        AuthUserEntity userEntity = authUserService.queryObject(requestEntity.getUserId());
        Map<String, String> params = new LinkedHashMap<>();
        params.put("account_mobile", userEntity.getMobile());
        params.put("id_number", requestEntity.getIdNo());
        params.put("account_name", requestEntity.getName());
        params.put("biz_code", "loanweb");

        params.put("contact1_mobile", requestEntity.getContact1Mobile());
        params.put("contact1_name", requestEntity.getContact1Name());
        params.put("contact2_mobile", requestEntity.getContact2Mobile());
        params.put("contact2_name", requestEntity.getContact2Name());

        try {
            HttpResponse response = HttpUtils.doPost(host, bodyGuardPath, MapUtils.EMPTY_MAP,
                    querys, params);
            logger.info("Tongdun body guard request: {}", params);
            String res = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("Tondun body guard response: {}", res);
            if (response.getStatusLine().getStatusCode() != 200) {
                logger.error("error response code: {}", response.getStatusLine().getStatusCode());
            } else {

                return res;
            }
        } catch (Exception e) {
            logger.error("error getting bodyguard report", e);
        }

        return null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
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

    public String getAppName() {
        return appName;
    }

    public void setAppName(final String appName) {
        this.appName = appName;
    }

    public String getBodyGuardPath() {
        return bodyGuardPath;
    }

    public void setBodyGuardPath(final String bodyGuardPath) {
        this.bodyGuardPath = bodyGuardPath;
    }
}
