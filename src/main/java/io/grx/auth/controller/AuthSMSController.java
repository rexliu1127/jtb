package io.grx.auth.controller;

import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthSMSController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsCodeService smsCodeService;


    /**
     * 获取手机验证码
     */
    @RequestMapping(value = "/sms/{type}/{mobile}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getSmsCode(@PathVariable String type, @PathVariable String mobile) {

        return smsCodeService.getVerifyCode(mobile, type);
    }


}
