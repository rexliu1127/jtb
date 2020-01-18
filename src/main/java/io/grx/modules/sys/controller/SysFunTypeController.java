package io.grx.modules.sys.controller;

import java.util.*;

import io.grx.modules.sys.entity.SysMerchantEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.enums.FunType;
import io.grx.modules.sys.service.SysMerchantService;
import io.grx.modules.tx.enums.UsageType;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysFunTypeEntity;
import io.grx.modules.sys.service.SysFunTypeService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 充值类型表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:36
 */
@RestController
@RequestMapping("/sys/sys_fun_type")
public class SysFunTypeController {
	@Autowired
	private SysFunTypeService sysFunTypeService;
	@Autowired
	private SysMerchantService sysMerchantService;
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sys_fun_type:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SysFunTypeEntity> sysFunTypeList = sysFunTypeService.queryList(query);
		int total = sysFunTypeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysFunTypeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:sys_fun_type:info")
	public R info(@PathVariable("id") Long id){
		SysFunTypeEntity sysFunType = sysFunTypeService.queryObject(id);
		
		return R.ok().put("sysFunType", sysFunType);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sys_fun_type:save")
	public R save(@RequestBody SysFunTypeEntity sysFunType){
		//如果商户类型编号为空，说明给所有商户都配置充值类型
		if(StringUtils.isEmpty(sysFunType.getMerchantNo())){
			//查询没有配置过充值类型的商户
			Map<String, Object> map = new HashMap<>();
			Query query = new Query(map);
			query.put("funType",sysFunType.getFunType());
			List<SysMerchantEntity>  merList = sysMerchantService.queryValidListByFunType(query);
			if(org.apache.commons.collections.CollectionUtils.isNotEmpty(merList)){
                List<SysFunTypeEntity>  sysFunTypeList =new ArrayList<SysFunTypeEntity>();
				for (SysMerchantEntity sysMerchantEntity : merList){
                    SysFunTypeEntity sysFunTypeParam=new SysFunTypeEntity();
                    sysFunTypeParam.setMerchantNo(sysMerchantEntity.getMerchantNo());
                    sysFunTypeParam.setCreateTime(new Date());
                    sysFunTypeParam.setFunType(sysFunType.getFunType());
                    sysFunTypeParam.setSingleFee(sysFunType.getSingleFee());
                    sysFunTypeList.add(sysFunTypeParam);
				}
                sysFunTypeService.saveFunTypeList(sysFunTypeList);
			}else{
				return R.error("所有商户已经配置该类型,如需修改,请单独操作");
			}
		}else{
			//查询改商户下对应类型是否有记录
			Map<String, Object> params=new HashMap<String, Object>();
			params.put("merchantNo",sysFunType.getMerchantNo());
			params.put("funType",sysFunType.getFunType());
			Query query = new Query(params);
			int count = sysFunTypeService.queryCountByConditions(query);
			if(count>0){//已经存在对应的类型不允许增加重复类型
				return R.error("该商户已经存在该类型");
			}
			sysFunType.setCreateTime(new Date());
			sysFunTypeService.save(sysFunType);
		}
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:sys_fun_type:update")
	public R update(@RequestBody SysFunTypeEntity sysFunType){
		//如果商户类型编号为空，说明给所有商户都配置充值类型
		if(StringUtils.isEmpty(sysFunType.getMerchantNo())){
			//查询没有配置过充值类型的商户
			Map<String, Object> map = new HashMap<>();
			Query query = new Query(map);
			query.put("funType",sysFunType.getFunType());
			List<SysMerchantEntity>  merList = sysMerchantService.queryValidListByFunType(query);
			if(org.apache.commons.collections.CollectionUtils.isNotEmpty(merList)){
				List<SysFunTypeEntity>  sysFunTypeList =new ArrayList<SysFunTypeEntity>();
				for (SysMerchantEntity sysMerchantEntity : merList){
					SysFunTypeEntity sysFunTypeParam=new SysFunTypeEntity();
					sysFunTypeParam.setMerchantNo(sysMerchantEntity.getMerchantNo());
					sysFunTypeParam.setCreateTime(new Date());
					sysFunTypeParam.setFunType(sysFunType.getFunType());
					sysFunTypeParam.setSingleFee(sysFunType.getSingleFee());
					sysFunTypeList.add(sysFunTypeParam);
				}
				sysFunTypeService.saveFunTypeList(sysFunTypeList);
			}else{
				return R.error("所有商户已经配置该类型,如需修改,请单独操作");
			}
		}else{
			//只修改金额
			SysFunTypeEntity param=new SysFunTypeEntity();
			param.setId(sysFunType.getId());
			param.setSingleFee(sysFunType.getSingleFee());
			sysFunTypeService.update(sysFunType);
		}
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sys_fun_type:delete")
	public R delete(@RequestBody Long[] ids){
		sysFunTypeService.deleteBatch(ids);
		
		return R.ok();
	}
	/**
	 * 获取有效商户列表审列表
	 */
	@RequestMapping("/mrchantList")
	public R select(){
		Map<String, Object> map = new HashMap<>();
		Query query = new Query(map);
		List<SysMerchantEntity>  merList = sysMerchantService.queryValidList(query);

		return R.ok().put("list", merList);
	}
}
