package io.grx.common.service.impl;

import com.google.gson.Gson;
import com.wlwx.client.SmsClient;
import com.wlwx.vo.ResultMsg;
import com.wlwx.vo.SmsReq;
import io.grx.common.service.SmsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Service("wlwxSmsService")
public class WlwxSmsService implements SmsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Value("${sms.wlwx.host}")
    private String host;
    @Value("${sms.wlwx.custCode}")
    private String custCode;
    @Value("${sms.wlwx.password}")
    private String password;

    @Override
    public boolean sendVerifyCode(String mobile, String type, String code, Map<String, String> params) {
        String label = "";
        if (env != null) {
            label = StringUtils.defaultIfBlank(env.getProperty(String.format("sms.wlwx.%1$sLabel", type)), "");
        }

        String msg = String.format("尊敬的用户，您本次%1$s操作的短信校验码：%2$s。切勿告知他人！", label, code);

        SmsReq smsReq = new SmsReq();
        smsReq.setUid(UUID.randomUUID().toString());
        smsReq.setCust_code(custCode);
        smsReq.setContent(msg);
        smsReq.setDestMobiles(mobile);

        logger.info("Sending SMS code. params: {}", new Gson().toJson(smsReq));
        ResultMsg resultMsg = new SmsClient().sendSms(smsReq, password, host);

        if (resultMsg.isSuccess()) {
            logger.info("Result: {}", resultMsg.getData());
            System.out.println();
        } else {
            logger.info("Error Result: {}, {}", resultMsg.getCode(), resultMsg.getMsg());
        }

        return resultMsg.isSuccess();
    }

    public static void main(String[] args) throws Exception {
        String password = "";

        String custCode  = "860353";
        String serviceBaseUrl = "http://123.58.255.70:8860";

        SmsReq smsReq = new SmsReq();
        smsReq.setUid(UUID.randomUUID().toString());
        smsReq.setCust_code(custCode);
        smsReq.setContent("尊敬的用户，您本次登录操作的短信校验码：123456。切勿告知他人");
        smsReq.setDestMobiles("");

        ResultMsg resultMsg = new SmsClient().sendSms(smsReq, password, serviceBaseUrl);

        if (resultMsg.isSuccess()) {
            System.out.println(resultMsg.getData());
        } else {
            System.out.println(resultMsg.getCode());
            System.out.println(resultMsg.getMsg());
        }
    }
}
