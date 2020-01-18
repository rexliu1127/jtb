package io.grx.modules.auth.controller;

import java.util.List;
import java.util.Map;

import io.grx.common.annotation.SysLog;
import io.grx.modules.auth.enums.StaffType;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.auth.entity.AuthStaffEntity;
import io.grx.modules.auth.service.AuthStaffService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 客服人员
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-01 00:36:13
 */
@RestController
@RequestMapping("/autha/staff")
public class AuthStaffController {
	@Autowired
	private AuthStaffService authStaffService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("auth:staff:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<AuthStaffEntity> authStaffList = authStaffService.queryList(query);
		int total = authStaffService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(authStaffList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{staffId}")
	@RequiresPermissions("auth:staff:info")
	public R info(@PathVariable("staffId") Long staffId){
		AuthStaffEntity authStaff = authStaffService.queryObject(staffId);
		
		return R.ok().put("authStaff", authStaff);
	}
	
	/**
	 * 保存
	 */
	@SysLog("新增客服")
	@RequestMapping("/save")
	@RequiresPermissions("auth:staff:save")
	public R save(@RequestBody AuthStaffEntity authStaff){
		if (authStaff.getStaffType() == null) {
			authStaff.setStaffType(StaffType.WX);
		}

		if (StringUtils.isBlank(authStaff.getStaffNo()) || StringUtils.isBlank(authStaff.getStaffBarcode())) {
			return R.error("微信码和二维码为必填项!");
		}
		authStaffService.save(authStaff);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改客服")
	@RequestMapping("/update")
	@RequiresPermissions("auth:staff:update")
	public R update(@RequestBody AuthStaffEntity authStaff){
		authStaffService.update(authStaff);

		if (StringUtils.isBlank(authStaff.getStaffNo()) || StringUtils.isBlank(authStaff.getStaffBarcode())) {
			return R.error("微信码和二维码为必填项!");
		}
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除客服")
	@RequestMapping("/delete")
	@RequiresPermissions("auth:staff:delete")
	public R delete(@RequestBody Long[] staffIds){
		authStaffService.deleteBatch(staffIds);
		
		return R.ok();
	}
	
}
