package io.grx.modules.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.annotation.SysLog;
import io.grx.common.utils.Constant;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.validator.ValidatorUtils;
import io.grx.common.validator.group.AddGroup;
import io.grx.common.validator.group.UpdateGroup;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysUserChannelService;
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
@RequestMapping("/sys/channel_user")
public class ChannelUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
    private SysUserChannelService sysUserChannelService;
	@Autowired
    private ChannelService channelService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:channel_user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
//		if(getUserId() != Constant.SUPER_ADMIN){
//			params.put("createUserId", getUserId());
//		}

		params.put("isChannelUser", Boolean.TRUE);

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
	public R info(){
		return R.ok().put("user", getUser());
	}

	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:channel_user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);

		//获取用户所属的角色列表
//		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
//		user.setRoleIdList(roleIdList);

        // 获取用户所属渠道列表
        List<Long> channelIdList = sysUserChannelService.queryUserChannelIdList(userId);
        user.setChannelIdList(channelIdList);
		
		return R.ok().put("user", user);
	}

	/**
	 * 保存用户
	 */
	@SysLog("保存渠道用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:channel_user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);

		if (sysUserService.queryByUserName(user.getUsername()) != null) {
			return R.error("该用户名已存在, 请使用其他用户名");
		}
		user.setMerchantNo(getMerchantNo());
		user.setCreateUserId(getUserId());
        user.setChannelUser(Boolean.TRUE);
		sysUserService.save(user);
		
		return R.ok();
	}

    /**
     * 渠道列表
     */
    @RequestMapping("/select_channel")
    @RequiresPermissions(value={"sys:channel_user:info", "sys:channel_user:update", "sys:channel_user:save"},
            logical=Logical.OR)
    public R select(){
        Map<String, Object> map = new HashMap<>();

        List<ChannelEntity> list = channelService.queryList(map);

        return R.ok().put("list", list);
    }

	/**
	 * 修改用户
	 */
	@SysLog("修改渠道用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:channel_user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

        SysUserEntity userInDb = sysUserService.queryByUserName(user.getUsername());
        if (userInDb != null && !userInDb.getUserId().equals(user.getUserId())) {
            return R.error("该用户名已存在, 请使用其他用户名");
        }

		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return R.ok();
	}

	/**
	 * 删除用户
	 */
	@SysLog("删除渠道用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:channel_user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}

}
