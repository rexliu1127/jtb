package io.grx.modules.sys.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.annotation.SysLog;
import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.R;
import io.grx.modules.sys.service.SysMerchantService;
import io.grx.modules.sys.service.SysUserService;

@RestController
public class SysRegisterController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMerchantService sysMerchantService;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private LuosimaoService luosimaoService;

    /**
     * 登录
     */
    @SysLog("商户注册")
    @RequestMapping(value = "/sys/register", method = RequestMethod.POST)
    public Map<String, Object> register(final String mobile, final String password,
                                     final String name, final String email, final String code,
                                     final String captcha) throws IOException {
        if (luosimaoService.needCaptcha() && StringUtils.isBlank(captcha)) {
            return R.error("请点击进行人机交互验证");
        }
        if (StringUtils.isBlank(code) || !smsCodeService.isVerifyCodeMatch(mobile, "login", code)) {
            return R.error("手机号验证码错误或已失败, 请重新获取(获取后10分钟内有效)");
        }

        return sysMerchantService.register(mobile, password, name, email);
    }
}
