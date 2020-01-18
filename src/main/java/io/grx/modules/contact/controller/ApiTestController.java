package io.grx.modules.contact.controller;


import com.google.gson.Gson;
import io.grx.common.utils.R;
import io.grx.modules.contact.annotation.Login;
import io.grx.modules.contact.annotation.LoginUser;
import io.grx.modules.contact.entity.UserEntity;
import io.grx.modules.contact.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * APP测试接口
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/contact")
public class ApiTestController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     */
    @Login
    @GetMapping("user_info")
    public R userInfo(@LoginUser UserEntity user){
        return R.ok().put("user", user);
    }


    /**
     * 获取用户信息
     */
    @Login
    @PostMapping("upload")
    public R uploadContact(@LoginUser UserEntity user, String contact){

        if (StringUtils.isNotBlank(contact)) {

            try {
                new Gson().fromJson(contact, HashMap.class);
            } catch (Exception e) {
                return R.error("上传失败");
            }
            user.setContact(contact);
            userService.update(user);
        }

        return R.ok();
    }

    /**
     * 获取用户ID
     */
    @Login
    @GetMapping("user_id")
    public R userInfo(@RequestAttribute("userId") Integer userId){
        return R.ok().put("userId", userId);
    }

    /**
     * 忽略Token验证测试
     */
    @GetMapping("notToken")
    public R notToken(){
        return R.ok().put("msg", "无需token也能访问。。。");
    }

}
