package io.grx.common.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.service.SmsService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.Md5Utils;
import io.grx.common.utils.UUIDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service("noNameSmsService")
public class NoNameSmsService implements SmsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private Environment env;

    @Value("${sms.noName.url}")
    private String url;
    @Value("${sms.noName.account}")
    private String account;
    @Value("${sms.noName.password}")
    private String password;
    @Value("${sms.noName.extNo}")
    private String extNo;
    @Value("${sms.noName.sign}")
    private String sign;

    @Override
    public boolean sendVerifyCode(String mobile, String type, String code, Map<String, String> params) {
        String label = "";
        if (env != null) {
            label = StringUtils.defaultIfBlank(env.getProperty(String.format("sms.dh3t.%1$sLabel", type)), "");
        }

        String msg = String.format("%3$s尊敬的用户，您本次%1$s操作的短信验证码：%2$s。切勿告知他人！", label, code, sign);
        Map<String, String> bodys = new LinkedHashMap<>();

        bodys.put("action", "send");
        bodys.put("account", account);
        bodys.put("password", password);
        bodys.put("msgid", UUIDGenerator.getUUID());
        bodys.put("mobile", mobile);
        bodys.put("content", msg);
        bodys.put("extno", extNo);
        bodys.put("rt", "json");

        logger.info("Sending SMS code. params: {}", bodys);
        try {
//            HttpResponse httpResponse = HttpUtils.doPost(url, MapUtils.EMPTY_MAP, bodys);
            HttpResponse httpResponse = HttpUtils.doGet(url, "", MapUtils.EMPTY_MAP, bodys);
            String response = EntityUtils.toString(httpResponse.getEntity(), Constant.ENCODING_UTF8);
            logger.info("result: {}", response);

            Map<String, Object> responseMap = new ObjectMapper().readValue(response,
                    new TypeReference<Map<String, Object>>() {});
            if (MapUtils.getIntValue(responseMap, "status", -1) == 0) {
                Collection<Map<String, Object>> list = (Collection) responseMap.get("list");
                if (CollectionUtils.isNotEmpty(list)) {
                    int result = MapUtils.getIntValue(list.iterator().next(), "result", -1);
                    if (result == 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("failed to send sms", e);
        }

        return false;
    }

    public static void main(String[] args) {
        NoNameSmsService service = new NoNameSmsService();
        service.url = "http://120.79.207.210:7862/sms";
        service.account = "888008";
        service.password = "";
        service.extNo = "";
        service.sign = "【】";

        boolean result = service.sendVerifyCode("", "login", "123456", new HashMap<>());
        System.out.println(result);
    }

}
