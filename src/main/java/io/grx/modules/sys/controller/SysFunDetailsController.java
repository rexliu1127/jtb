package io.grx.modules.sys.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grx.common.utils.*;
import io.grx.modules.sys.enums.FunType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysFunDetailsEntity;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysFunDetailsService;
import io.grx.modules.sys.service.SysUserRoleService;

import javax.servlet.http.HttpServletResponse;

import static io.grx.common.utils.ShiroUtils.getMerchantNo;


/**
 * 用户费用明细表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 20:46:07
 */
@RestController
@RequestMapping("/sys/sysfundetails")
public class SysFunDetailsController extends AbstractController{
	@Autowired
	private SysFunDetailsService sysFunDetailsService;


	@Autowired
	private PoiUtils poiUtils;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sysfundetails:list")
	public R list(@RequestParam Map<String, Object> params){
		String merchantno = getMerchantNo();
		/*2019-04需求,除管理员外因此财务模块数据,为了防止授权问题,此处强制写死,不返回数据*/
		if(!Constant.DEFAULT_MERCHANT_NO.equals(merchantno)) {
			SysUserEntity currentUser = this.getUser();
			boolean isMerchantAdmin = currentUser.getUsername().equals(merchantno);
			if(!isMerchantAdmin) {
				return R.error("权限不足,非商户管理员禁止查看");
			}
		}
		
		//查询列表数据
		//商户号
		params.put("merchantno",merchantno);

        Query query = new Query(params);

		List<SysFunDetailsEntity> sysFunDetailsList = sysFunDetailsService.queryList(query);
		int total = sysFunDetailsService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysFunDetailsList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil).put("merchantno", merchantno);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:sysfundetails:info")
	public R info(@PathVariable("id") Long id){
		SysFunDetailsEntity sysFunDetails = sysFunDetailsService.queryObject(id);
		
		return R.ok().put("sysFunDetails", sysFunDetails);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sysfundetails:save")
	public R save(@RequestBody SysFunDetailsEntity sysFunDetails){
		sysFunDetailsService.save(sysFunDetails);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:sysfundetails:update")
	public R update(@RequestBody SysFunDetailsEntity sysFunDetails){
		sysFunDetailsService.update(sysFunDetails);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sysfundetails:delete")
	public R delete(@RequestBody Long[] ids){
		sysFunDetailsService.deleteBatch(ids);
		
		return R.ok();
	}

	/**
	 * 按照查询条件导出
	 * @param params
	 * @param response
	 */
	@RequestMapping("/export")
	@RequiresPermissions("sys:sysfundetails:save")
	public void exportList(@RequestParam Map<String, Object> params, HttpServletResponse response){

		String merchantno = getMerchantNo();
		params.put("merchantno",merchantno);

		Query query = new Query(params);

		List<SysFunDetailsEntity>  queryList = sysFunDetailsService.queryList(query);
		/*2019-04需求,除管理员外因此财务模块数据,为了防止授权问题,此处强制写死,不返回数据*/
		if(!Constant.DEFAULT_MERCHANT_NO.equals(merchantno)) {
			SysUserEntity currentUser = this.getUser();
			boolean isMerchantAdmin = currentUser.getUsername().equals(merchantno);
			if(!isMerchantAdmin) {
				return;
			}
		}
		exportAuthRequests(queryList,response);
	}

	/**
	 * 选择导出
	 * @param requestIds
	 * @param response
	 */
	@RequestMapping("/export_by_id")
	@RequiresPermissions("sys:sysfundetails:save")
	public void exportListById(String requestIds, HttpServletResponse response){
		Map<String, Object> params = new HashMap<>();
		String merchantno = getMerchantNo();
		params.put("merchantno",merchantno);
		params.put("requestIds", CharUtils.splitToArrayList(requestIds, ","));
		Query query = new Query(params);

		List<SysFunDetailsEntity>  queryList = sysFunDetailsService.queryList(query);
		exportAuthRequests(queryList,response);
	}


	private void exportAuthRequests(List<SysFunDetailsEntity>  funList ,  HttpServletResponse response)
	{
		String filename = "fun_details_list_" + System.currentTimeMillis();

		response.setCharacterEncoding("utf-8");
		response.setContentType("application/msexcel");// 设置为下载application/x-download
		response.setHeader("Content-Disposition", "inline;filename=\""
				+ filename + ".xls\"");

		List<Object[]> dataList = new ArrayList<>();
		Object[] header = new Object[] {"借款人手机", "用户名称", "费用类型", "费用(元)", "消费时间"};
		dataList.add(header);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for(SysFunDetailsEntity  fun : funList)
		{
			System.out.println(fun.getFunType());
			String typeName = FunType.valueOf(fun.getFunType().intValue()).getDisplayName();

			Object[] row = new String[] {
					fun.getBorrowerPhone(),
					fun.getUserName(),
					typeName,
					fun.getAmount().toString(),
					format.format(fun.getCreateTime())

			};
			dataList.add(row);
		}

		try {
			poiUtils.createExcel("商户消费列表", dataList, response.getOutputStream());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
