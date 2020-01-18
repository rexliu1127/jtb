package io.grx.modules.sys.controller;

import java.util.List;
import java.util.Map;

import io.grx.common.utils.Constant;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysFunService;
import io.grx.modules.sys.service.SysMerchantService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.annotation.SysLog;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.validator.Assert;
import io.grx.common.validator.ValidatorUtils;
import io.grx.common.validator.group.AddGroup;
import io.grx.common.validator.group.UpdateGroup;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserRoleService;
import io.grx.modules.sys.service.SysUserService;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private SysMerchantService sysMerchantService;

	@Autowired
	private SysFunService sysFunService;
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
//		if(getUserId() != Constant.SUPER_ADMIN){
//			params.put("createUserId", getUserId());
//		}

		params.put("isChannelUser", Boolean.FALSE);
		
		//查询列表数据
		Query query = new Query(params);
		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(Integer merchant){
		SysUserEntity user = getUser();
		SysMerchantEntity merchantEntity = null;
		SysFunEntity   funinfo = null;
		if (merchant != null && merchant > 0 && !StringUtils.equalsIgnoreCase(user.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)) {
			merchantEntity = sysMerchantService.queryObject(user.getMerchantNo());

			//获取商户资金信息
			funinfo = sysFunService.queryinfo(user.getMerchantNo());
		}
		return R.ok().put("user", getUser()).put("merchant", merchantEntity).put("funinfo",funinfo);
	}

	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		Assert.isBlank(newPassword, "新密码不为能空");
		
		//sha256加密
		password = new Sha256Hash(password, getUser().getSalt()).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();
				
		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(count == 0){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);
        SysMerchantEntity merchantEntity = null;
        if (user != null) {
            //获取用户所属的角色列表
            List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
            user.setRoleIdList(roleIdList);

            merchantEntity = sysMerchantService.queryObject(user.getMerchantNo());
        }
		return R.ok().put("user", user)
                .put("merchant", merchantEntity);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

		if (StringUtils.isBlank(user.getUsername()) && StringUtils.isBlank(user.getEmail())) {
			return R.error("用户名和邮箱不能同时为空");
		}

		if (StringUtils.isNotBlank(user.getUsername())
				&& sysUserService.queryByUserName(user.getUsername()) != null) {
			return R.error("该用户名已存在, 请使用其他用户名");
		}

		if (StringUtils.isNotBlank(user.getEmail())
				&& sysUserService.queryByEmail(user.getEmail()) != null) {
			return R.error("该Email已存在, 请使用其他Email地址");
		}

		user.setMerchantNo(getMerchantNo());
		user.setCreateUserId(getUserId());
		sysUserService.save(user);

		return R.ok();
	}

	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		if (StringUtils.isBlank(user.getUsername()) && StringUtils.isBlank(user.getEmail())) {
			return R.error("用户名和邮箱不能同时为空");
		}

		if (StringUtils.isNotBlank(user.getUsername())) {
			SysUserEntity userInDb = sysUserService.queryByUserName(user.getUsername());
			if (userInDb != null && !userInDb.getUserId().equals(user.getUserId())) {
				return R.error("该用户名已存在, 请使用其他用户名");
			}
		}

		if (StringUtils.isNotEmpty(user.getEmail())) {
			SysUserEntity userInDb = sysUserService.queryByEmail(user.getEmail());
			if (userInDb != null && !userInDb.getUserId().equals(user.getUserId())) {
				return R.error("该Email已存在, 请使用其他Email地址");
			}
		}

		user.setCreateUserId(getUserId());
		sysUserService.update(user);

		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}

		SysMerchantEntity merchantEntity = sysMerchantService.queryObject(getUser().getMerchantNo());
		if (ArrayUtils.contains(userIds, merchantEntity.getAdminUserId())) {
		    return R.error("系统管理员不能删除");
        }

		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}

}
