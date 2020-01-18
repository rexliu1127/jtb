package io.grx.common.service.impl;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;

@Service
public class LuosimaoService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${luosimao.siteHost}")
    private String siteHost;
    @Value("${luosimao.siteKey}")
    private String siteKey;
    @Value("${luosimao.appKey}")
    private String appKey;

    private static final String CAPTCHA_VERIFY_HOST = "https://captcha.luosimao.com";
    private static final String CAPTCHA_VERIFY_PATH = "/api/site_verify";

    public boolean isResponseVerified(String response) {
        if (StringUtils.isBlank(response)) {
            return false;
        }

        Map<String, String> params = new HashMap<>();
        params.put("response", response);
        params.put("api_key", appKey);

        try {
            HttpResponse httpResponse = HttpUtils.doPost(CAPTCHA_VERIFY_HOST, CAPTCHA_VERIFY_PATH, MapUtils.EMPTY_MAP,
                    MapUtils.EMPTY_MAP, params);
            final String responseStr = EntityUtils.toString(httpResponse.getEntity(), Constant.ENCODING_UTF8);

            Map<String, Object> responseMap = new ObjectMapper().readValue(responseStr,
                    new TypeReference<Map<String, Object>>() {
                    });
            if (MapUtils.getIntValue(responseMap, "error", -1) == 0) {
                return true;
            } else {
                logger.debug("Luosimao captcha not match. {}", responseStr);
            }
        } catch (Throwable t) {
            logger.error("Failed to verify luosimao captcha.", t);
        }
        return false;
    }

    public String getSiteKey() {
        return siteKey;
    }

    public boolean needCaptcha() {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (request != null) {
            String machineId = (String) request.getAttribute(Constant.COOKIE_MACHINE_ID);
            return StringUtils.containsIgnoreCase(request.getServerName(), siteHost) && StringUtils.isBlank(machineId);
        }
        return false;
    }
}
