package io.grx.modules.sys.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.*;
import io.grx.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.service.SysMerchantService;


/**
 * 商家
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-06-09 23:41:29
 */
@RestController
@RequestMapping("/sys/sysmerchant")
public class SysMerchantController {
	@Autowired
	private SysMerchantService sysMerchantService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sysmerchant:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SysMerchantEntity> sysMerchantList = sysMerchantService.queryList(query);
		int total = sysMerchantService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysMerchantList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{merchantNo}")
	@RequiresPermissions("sys:sysmerchant:info")
	public R info(@PathVariable("merchantNo") String merchantNo){
		SysMerchantEntity sysMerchant = sysMerchantService.queryObject(merchantNo);
		
		return R.ok().put("sysMerchant", sysMerchant);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sysmerchant:save")
	public R save(@RequestBody SysMerchantEntity sysMerchant){
		sysMerchantService.save(sysMerchant);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody SysMerchantEntity sysMerchant){
        SysUserEntity userEntity = ShiroUtils.getUserEntity();
        if (!StringUtils.equalsIgnoreCase(userEntity.getMerchantNo(), Constant.DEFAULT_MERCHANT_NO)
                && !StringUtils.equalsIgnoreCase(userEntity.getMerchantNo(), sysMerchant.getMerchantNo())) {
            return R.error("非法请求");
        }
		sysMerchant.setUpdateTime(new Date());
		sysMerchantService.update(sysMerchant);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sysmerchant:delete")
	public R delete(@RequestBody String[] merchantNos){
		sysMerchantService.deleteBatch(merchantNos);
		
		return R.ok();
	}
	
}
