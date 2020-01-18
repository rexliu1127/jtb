package io.grx.modules.tx.controller;

import java.util.List;
import java.util.Map;

import io.grx.modules.tx.converter.TxUserVOConverter;
import io.grx.modules.tx.dto.TxUserVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.grx.common.utils.PageUtils;
import io.grx.common.utils.Query;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;
import io.grx.common.utils.R;




/**
 * 系统用户
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-11-26 00:49:35
 */
@RestController
@RequestMapping("/tx/user")
public class TxUserController {
	@Autowired
	private TxUserService txUserService;

	@Autowired
	private TxUserVOConverter txUserVOConverter;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("tx:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<TxUserVO> txUserList = txUserVOConverter.convert(txUserService.queryList(query));
		int total = txUserService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(txUserList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("tx:user:info")
	public R info(@PathVariable("userId") Long userId){
		TxUserEntity txUser = txUserService.queryObject(userId);
		
		return R.ok().put("txUser", txUser);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("tx:user:save")
	public R save(@RequestBody TxUserEntity txUser){
		txUserService.save(txUser);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("tx:user:update")
	public R update(@RequestBody TxUserEntity txUser){
		txUserService.update(txUser);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("tx:user:delete")
	public R delete(@RequestBody Long[] userIds){
		txUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
}
