package io.grx.modules.sys.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysMerchantService;
import io.grx.modules.sys.service.SysUserRoleService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysFunRecordEntity;
import io.grx.modules.sys.service.SysFunRecordService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;




/**
 * 充值记录表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 17:55:46
 */
@RestController
@RequestMapping("/sys/sysfunrecord")
public class SysFunRecordController extends AbstractController {
	@Autowired
	private SysFunRecordService sysFunRecordService;

	@Autowired
	private SysMerchantService sysMerchantService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sysfunrecord:list")
	public R list(@RequestParam Map<String, Object> params){
		String merchantNo = getMerchantNo();
		/*2019-04需求,除管理员外因此财务模块数据,为了防止授权问题,此处强制写死,不返回数据*/
		if(!Constant.DEFAULT_MERCHANT_NO.equals(merchantNo)) {
			//非平台商户-查询当前用户角色是否包含管理员-即roleId=2
			SysUserEntity currentUser = this.getUser();
			boolean isMerchantAdmin = currentUser.getUsername().equals(merchantNo);
			if(!isMerchantAdmin) {
				return R.error("权限不足,非商户管理员禁止查看");
			}
		}
		
		//商户号
		
		if(!"00".equals(merchantNo)){
			params.put("merchantNo",merchantNo);
		}

		//查询列表数据
        Query query = new Query(params);

		List<SysFunRecordEntity> sysFunRecordList = sysFunRecordService.queryList(query);
		int total = sysFunRecordService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysFunRecordList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil).put("merchantNo", merchantNo);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:sysfunrecord:info")
	public R info(@PathVariable("id") Long id){
		SysFunRecordEntity sysFunRecord = sysFunRecordService.queryObject(id);
		
		return R.ok().put("sysFunRecord", sysFunRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sysfunrecord:save")
	public R save(@RequestBody SysFunRecordEntity sysFunRecord){

		SysUserEntity sysuser = getUser();

		sysFunRecord.setCreateBy(sysuser.getUserId());
		sysFunRecord.setCreateTime(new Date());
		sysFunRecordService.save(sysFunRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:sysfunrecord:update")
	public R update(@RequestBody SysFunRecordEntity sysFunRecord){
		sysFunRecordService.update(sysFunRecord);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sysfunrecord:delete")
	public R delete(@RequestBody Long[] ids){
		sysFunRecordService.deleteBatch(ids);
		
		return R.ok();
	}


	/**
	 * 查询有效商户
	 * @return
	 */
	@RequestMapping("/merchantList")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		Query query = new Query(map);
		List<SysMerchantEntity>  merList = sysMerchantService.queryValidList(query);

		return R.ok().put("list", merList);

	}
}
