package io.grx.modules.opt.controller;

import com.google.gson.Gson;
import io.grx.auth.entity.AuthConfig;
import io.grx.modules.opt.service.ChannelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.annotation.SysLog;
import io.grx.common.utils.R;
import io.grx.modules.sys.service.SysConfigService;

/**
 * 认证配置
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
@RestController
@RequestMapping("/opt/auth_config")
public class AuthConfigController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ChannelService channelService;

    /**
     * 认证配置
     */
    @RequestMapping("/info")
    @RequiresPermissions("opt:auth_config:save")
    public R info(){
        AuthConfig config = channelService.getAuthConfig();

        return R.ok().put("config", config);
    }

    /**
     * 保存用户
     */
    @SysLog("保存认证配置")
    @RequestMapping("/save")
    @RequiresPermissions("opt:auth_config:save")
    public R save(@RequestBody AuthConfig config){
        channelService.updateAuthConfig(config);

        return R.ok();
    }
}
