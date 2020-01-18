package io.grx.auth.controller;

import io.grx.auth.service.AuthCreditService;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.enums.CreditType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/auth/credit")
public class AuthCreditSelectController {

    @Autowired
    private AuthCreditService authCreditService;

    /**
     * 跳转有盾认证
     */
    @GetMapping(value = "/page/{type}")
    public String page(@PathVariable("type") String type) {
        AuthUserEntity user = ShiroUtils.getAuthUser();

        CreditType creditType = CreditType.valueOf(StringUtils.upperCase(type));
        switch (creditType) {
            case BASIC:
                return "";

            case RELATION:
            case CONTACT:

        }

        return "redirect:";
    }

    /**
     * 获取认证h5 url
     */
    @GetMapping(value = "/h5/{type}")
    @ResponseBody
    public Map<String, Object> requestH5Url(@PathVariable("type") String type) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName()) && !user.isAuthStatus()) {
            return R.error("请先完善基本信息认证!");
        }
        if (StringUtils.isBlank(user.getContact1Mobile())) {
            return R.error("请先完善紧急联系人信息!");
        }

        return authCreditService.requestH5Url(CreditType.valueOf(StringUtils.upperCase(type)));
    }
}
