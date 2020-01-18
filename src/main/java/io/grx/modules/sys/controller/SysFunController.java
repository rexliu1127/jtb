package io.grx.modules.sys.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.service.SysFunService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 充值表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 20:46:07
 */
@RestController
@RequestMapping("/sys/sysfun")
public class SysFunController extends AbstractController{
	@Autowired
	private SysFunService sysFunService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sysfun:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SysFunEntity> sysFunList = sysFunService.queryList(query);
		int total = sysFunService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysFunList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:sysfun:info")
	public R info(@PathVariable("id") Long id){
		SysFunEntity sysFun = sysFunService.queryObject(id);
		
		return R.ok().put("sysFun", sysFun);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sysfun:save")
	public R save(@RequestBody SysFunEntity sysFun){
		sysFunService.save(sysFun);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:sysfun:update")
	public R update(@RequestBody SysFunEntity sysFun){
		sysFunService.update(sysFun);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sysfun:delete")
	public R delete(@RequestBody Long[] ids){
		sysFunService.deleteBatch(ids);
		
		return R.ok();
	}


	/**
	 * 设置商户为风险商户
	 * @param ids
	 * @return
	 */
	@RequestMapping("/updateRist")
	public R updateRist(@RequestBody Long[] ids){

		sysFunService.updateRistBatch(ids);
		return R.ok();
	}
	
}
