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

import io.grx.modules.auth.entity.AuthRequestHistoryEntity;
import io.grx.modules.auth.service.AuthRequestHistoryService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 申请单历史
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
@RestController
@RequestMapping("/auth/authrequesthistory")
public class AuthRequestHistoryController {
	@Autowired
	private AuthRequestHistoryService authRequestHistoryService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("auth:authrequesthistory:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<AuthRequestHistoryEntity> authRequestHistoryList = authRequestHistoryService.queryList(query);
		int total = authRequestHistoryService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(authRequestHistoryList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("auth:authrequesthistory:info")
	public R info(@PathVariable("id") Long id){
		AuthRequestHistoryEntity authRequestHistory = authRequestHistoryService.queryObject(id);
		
		return R.ok().put("authRequestHistory", authRequestHistory);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("auth:authrequesthistory:save")
	public R save(@RequestBody AuthRequestHistoryEntity authRequestHistory){
		authRequestHistoryService.save(authRequestHistory);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("auth:authrequesthistory:update")
	public R update(@RequestBody AuthRequestHistoryEntity authRequestHistory){
		authRequestHistoryService.update(authRequestHistory);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("auth:authrequesthistory:delete")
	public R delete(@RequestBody Long[] ids){
		authRequestHistoryService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
