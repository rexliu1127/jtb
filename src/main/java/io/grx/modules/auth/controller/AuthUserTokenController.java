package io.grx.modules.auth.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.auth.entity.AuthUserTokenEntity;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 认证用户Token
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:19
 */
@RestController
@RequestMapping("/auth/authusertoken")
public class AuthUserTokenController {
	@Autowired
	private AuthUserTokenService authUserTokenService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("auth:authusertoken:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<AuthUserTokenEntity> authUserTokenList = authUserTokenService.queryList(query);
		int total = authUserTokenService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(authUserTokenList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("auth:authusertoken:info")
	public R info(@PathVariable("userId") Long userId){
		AuthUserTokenEntity authUserToken = authUserTokenService.queryObject(userId);
		
		return R.ok().put("authUserToken", authUserToken);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("auth:authusertoken:save")
	public R save(@RequestBody AuthUserTokenEntity authUserToken){
		authUserTokenService.save(authUserToken);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("auth:authusertoken:update")
	public R update(@RequestBody AuthUserTokenEntity authUserToken){
		authUserTokenService.update(authUserToken);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("auth:authusertoken:delete")
	public R delete(@RequestBody Long[] userIds){
		authUserTokenService.deleteBatch(userIds);
		
		return R.ok();
	}
	
}
