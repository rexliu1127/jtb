package io.grx.modules.sys.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.grx.modules.sys.entity.SysUserEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.modules.sys.entity.SysNoticeEntity;
import io.grx.modules.sys.service.SysNoticeService;
import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;




/**
 * 公告表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-20 10:13:19
 */
@RestController
@RequestMapping("/sys/sys_notice")
public class SysNoticeController extends AbstractController{
	@Autowired
	private SysNoticeService sysNoticeService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:sys_notice:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<SysNoticeEntity> sysNoticeList = sysNoticeService.queryList(query);
		int total = sysNoticeService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(sysNoticeList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 商户端查看列表
	 */
	@RequestMapping("/merchantList")
	@RequiresPermissions("sys:sys_notice:merchantList")
	public R merchantList(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		//查询已经发布的公告
		query.put("noticeStatus",1);
		List<SysNoticeEntity> sysNoticeList = sysNoticeService.queryList(query);
		int total = sysNoticeService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysNoticeList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{noticeId}")
	@RequiresPermissions("sys:sys_notice:info")
	public R info(@PathVariable("noticeId") Long noticeId){
		SysNoticeEntity sysNotice = sysNoticeService.queryObject(noticeId);
		
		return R.ok().put("sysNotice", sysNotice);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:sys_notice:save")
	public R save(@RequestBody SysNoticeEntity sysNotice){
		//获取当前登录人信息
		SysUserEntity user = getUser();
		sysNotice.setCreateBy(user.getUserId());
		sysNotice.setCreateTime(new Date());
		sysNoticeService.save(sysNotice);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:sys_notice:update")
	public R update(@RequestBody SysNoticeEntity sysNotice){
		//获取当前登录人信息
		SysUserEntity user = getUser();
		sysNotice.setCreateBy(user.getUserId());
		sysNotice.setCreateTime(new Date());
		sysNoticeService.update(sysNotice);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:sys_notice:delete")
	public R delete(@RequestBody Long[] noticeIds){
		sysNoticeService.deleteBatch(noticeIds);
		
		return R.ok();
	}
	
}
