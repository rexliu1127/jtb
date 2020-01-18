package io.grx.modules.sys.controller;

import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.modules.sys.entity.SysLogEntity;
import io.grx.modules.sys.service.SysLogService;
import io.grx.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import static io.grx.common.utils.ShiroUtils.getMerchantNo;


/**
 * 系统日志
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-08 10:40:56
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<SysLogEntity> sysLogList = sysLogService.queryList(query);
		int total = sysLogService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);

	}
	/**
	 * 商户日志列表
	 */
	@ResponseBody
	@RequestMapping("/merchantLogList")
	@RequiresPermissions("sys:log:merchantLogList")
	public R merchantLogList(@RequestParam Map<String, Object> params){
		//获取当前登陆人的商户号
		String merchantNo = getMerchantNo();
		//查询列表数据
		Query query = new Query(params);
		query.put("merchantNo",merchantNo);
		List<SysLogEntity> sysLogList = sysLogService.queryListByMerchantNo(query);
		int total = sysLogService.queryTotalByMerchantNo(query);

		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);

	}
}
