package io.grx.modules.sys.controller;

import java.util.HashMap;
import java.util.List;

import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.sys.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.utils.Constant;
import io.grx.common.utils.R;
import io.grx.modules.sys.entity.SysDeptEntity;
import io.grx.modules.sys.service.SysDeptService;


/**
 * 部门管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
	@Autowired
	private SysDeptService sysDeptService;

	@Autowired
	private ChannelService channelService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public List<SysDeptEntity> list(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

		return deptList;
	}

	/**
	 * 选择部门(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:dept:select")
	public R select(){
		List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

		//添加一级部门
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级部门");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}

		return R.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public R info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			SysDeptEntity dept = sysDeptService.queryObject(getDeptId());
			if (dept != null && dept.getParentId() != null) {
				deptId = dept.getParentId();
			}
		}

		return R.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public R info(@PathVariable("deptId") Long deptId){
		SysDeptEntity dept = sysDeptService.queryObject(deptId);
		
		return R.ok().put("dept", dept);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sys:dept:save")
	public R save(@RequestBody SysDeptEntity dept){
		sysDeptService.save(dept);

		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sys:dept:update")
	public R update(@RequestBody SysDeptEntity dept){
		if (dept.getDeptId().equals(dept.getParentId())) {
			return R.error("上级部门不能为当前部门");
		}

		List<Long> subDeptIds = sysDeptService.queryDetpIdList(dept.getDeptId());
		if (subDeptIds.contains(dept.getParentId())) {
			return R.error("上级部门不能为自己的下属部门");
		}

		sysDeptService.update(dept);
		
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dept:delete")
	public R delete(long deptId){
		//判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return R.error("请先删除子部门!");
		}

		if (channelService.isDeptHasChannel(deptId)) {
			return R.error("该部门已有创建渠道，不能被删除!");
		}

		if (sysUserService.isDeptHasUser(deptId)) {
			return R.error("该部门已有创建管理员，不能被删除!");
		}

		sysDeptService.delete(deptId);
		
		return R.ok();
	}
	
}
