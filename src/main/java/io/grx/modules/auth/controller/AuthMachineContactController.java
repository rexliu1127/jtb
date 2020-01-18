package io.grx.modules.auth.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.CharUtils;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserTokenService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.auth.entity.AuthMachineContactEntity;
import io.grx.modules.auth.service.AuthMachineContactService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * APP机器通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-10-22 14:27:10
 */
@RestController
@RequestMapping("/autha/machine_contact")
public class AuthMachineContactController {
	@Autowired
	private AuthMachineContactService authMachineContactService;

	@Autowired
    private AuthUserTokenService authUserTokenService;

	@Autowired
    private AuthUserContactService authUserContactService;

	/**
	 * 信息
	 */
	@RequestMapping("/info/{machineId}")
	public R info(@PathVariable("machineId") String machineId){
		AuthMachineContactEntity authMachineContact = authMachineContactService.queryObject(machineId);

		return R.ok().put("authMachineContact", authMachineContact);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/status/{machineId}")
	public R status(@PathVariable("machineId") String machineId){
		AuthMachineContactEntity authMachineContact = authMachineContactService.queryObject(machineId);
		int status = 0;
		if (authMachineContact == null || authMachineContact.getCreateTime() == null
			|| System.currentTimeMillis() - authMachineContact.getCreateTime().getTime() > 5 * 24 * 60 * 60 * 1000) {
			status = 1;
		}

		return R.ok().put("status", status);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody AuthMachineContactEntity authMachineContact){
		if (authMachineContact != null) {
			authMachineContact.setContact(CharUtils.toValid3ByteUTF8String(authMachineContact.getContact()));
		}
		AuthMachineContactEntity authMachineContactInDB = authMachineContactService.queryObject(authMachineContact.getMachineId());
		if (authMachineContactInDB == null) {
			authMachineContactService.save(authMachineContact);
		} else {
			authMachineContactInDB.setContact(authMachineContact.getContact());
			authMachineContactInDB.setCreateTime(new Date());
			authMachineContactService.update(authMachineContact);
		}

		return R.ok();
	}

    /**
     * 保存
     */
    @RequestMapping("/save_by_token/{token}")
    public R save(@PathVariable("token") String token, @RequestBody Map<String, String> contacts){
        Long userId = authUserTokenService.getUserIdByToken(token);

        if (userId != null) {
            authUserContactService.saveOrUpdate(userId, contacts);
        }

        return R.ok();
    }
}
