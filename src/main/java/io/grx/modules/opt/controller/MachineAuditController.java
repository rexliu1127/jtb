package io.grx.modules.opt.controller;

import io.grx.auth.entity.AuthConfig;
import io.grx.auth.entity.MachineAuthConfig;
import io.grx.common.annotation.SysLog;
import io.grx.common.utils.R;
import io.grx.modules.opt.service.ChannelService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机审条件配置
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-22 20:19:17
 */
@RestController
@RequestMapping("/opt/machine_auth_config")
public class MachineAuditController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ChannelService channelService;

    /**
     * 机审条件配置
     */
    @RequestMapping("/info")
    @RequiresPermissions("opt:machine_auth_config:save")
    public R info(){
        MachineAuthConfig config = channelService.getMachineAuthConfig();

        return R.ok().put("config", config);
    }

    /**
     * 保存机审条件配置
     */
    @SysLog("保存认证配置")
    @RequestMapping("/save")
    @RequiresPermissions("opt:machine_auth_config:save")
    public R save(@RequestBody MachineAuthConfig config){
        channelService.updateMachineAuthConfig(config);

        return R.ok();
    }
}
