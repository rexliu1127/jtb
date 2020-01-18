package io.grx.modules.contact.controller;


import io.grx.common.validator.Assert;
import io.grx.modules.contact.service.UserService;
import io.grx.common.utils.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-26 17:27
 */
@RestController
@RequestMapping("/app")
public class ApiRegisterController {
    @Autowired
    private UserService userService;

    /**
     * 注册
     */
    @PostMapping("register")
    public R register(String mobile, String password){
        Assert.isBlank(mobile, "手机号不能为空");
        Assert.isBlank(password, "密码不能为空");

        userService.save(mobile, password);

        return R.ok();
    }
}
