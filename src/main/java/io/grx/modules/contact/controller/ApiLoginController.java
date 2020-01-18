package io.grx.modules.contact.controller;


import io.grx.common.service.SmsCodeService;
import io.grx.common.validator.Assert;
import io.grx.modules.contact.entity.UserEntity;
import io.grx.modules.contact.service.UserService;
import io.grx.modules.contact.utils.JwtUtils;
import io.grx.common.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * APP登录授权
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/contact")
public class ApiLoginController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private UserService userService;

    /**
     * 登录
     */
    @PostMapping("login")
    public R login(String mobile, String verifyCode){
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(verifyCode, "手机验证码不能为空");

        if (!smsCodeService.isVerifyCodeMatch(mobile, "login", verifyCode)) {
            return R.error("手机号验证码错误或已失败, 请重新获取");
        }

        UserEntity userEntity = userService.queryByMobile(mobile);
        if (userEntity == null) {
            userEntity = userService.save(mobile, null);
        }
        //生成token
        String token = jwtUtils.generateToken(mobile);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("lastUpdateTime", userEntity.getUpdateTime() != null ? userEntity.getUpdateTime().getTime() : null);

        return R.ok(map);
    }

}
