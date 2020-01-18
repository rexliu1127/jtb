package io.grx.modules.flow.controller;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;

import io.grx.common.utils.Constant;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.flow.entity.FlowSettingEntity;
import io.grx.modules.flow.service.FlowSettingService;
import io.grx.modules.job.task.FlowAllocationTask;
import io.grx.modules.sys.controller.AbstractController;
import io.grx.modules.sys.entity.SysFunDetailsEntity;
import io.grx.modules.sys.entity.SysFunEntity;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysFunDetailsService;
import io.grx.modules.sys.service.SysFunService;

@RestController
@RequestMapping("/flow")
public class FlowSettingController extends AbstractController {
	
	@Autowired
	private FlowSettingService flowSettingService;
	
	@Autowired
	private SysFunService sysFunService;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private SysFunDetailsService fundDetailsService;

	@Autowired
	private FlowAllocationTask task;
	
	@PostMapping("/testTask")
	public R testTask() {
		task.runAllocationTask();
		return R.ok();
	}
	
	/**
	 * 查询最后一个设置项,如果状态为0 则前端禁用按钮等操作
	 * @return
	 */
	@PostMapping("/queryLastSetting")
	public R queryLastSetting() {
		String merchantNo = ShiroUtils.getMerchantNo();
		if(Constant.DEFAULT_MERCHANT_NO.equals(merchantNo)) {
			return R.error("平台管理员商户不能设置");
		}
		SysFunEntity fun = sysFunService.queryinfo(merchantNo);
		
		String cpa = sysConfigService.getValue(Constant.KEY_FLOW_CPA);
		if(StringUtils.isEmpty(cpa)||!NumberUtils.isNumber(cpa)) {
			return R.error("CPA单价配置有误,请联系管理员");
		}
		FlowSettingEntity entity = this.flowSettingService.queryLastSettingByMerchantNo(merchantNo);
		return R.ok().put("setting", entity).put("remainingSum", fun.getRemainingSum()).put("cpa", cpa);
	}
	
	@PostMapping("/saveFlowSetting")
	public R saveFlowSetting(@RequestParam Integer flowCount,@RequestParam Long channelId) {
		if(flowCount<100) {
			return R.error("100个流量起步");
		}
		synchronized (FlowSettingController.class) {
			String merchantNo = ShiroUtils.getMerchantNo();
			//扣款
			String cpa = sysConfigService.getValue(Constant.KEY_FLOW_CPA);
			BigDecimal cpaAmt = new BigDecimal(cpa);
			BigDecimal totalAmt = cpaAmt.multiply(new BigDecimal(flowCount));
			SysFunEntity sysFun = sysFunService.queryinfo(merchantNo);
			
			BigDecimal remainingSum = sysFun.getRemainingSum();
			remainingSum = remainingSum.subtract(totalAmt);
			sysFun.setRemainingSum(remainingSum);
			sysFunService.update(sysFun);
			
			//插流水
			SysFunDetailsEntity sysFunDetails = new SysFunDetailsEntity();
			sysFunDetails.setUserId(ShiroUtils.getUserId());
			sysFunDetails.setBorrowerPhone("");
			sysFunDetails.setFunType(3L);//流量购买
			sysFunDetails.setAmount(totalAmt);
			sysFunDetails.setCreateTime(new Date());
			sysFunDetails.setMerchantNo(merchantNo);
			fundDetailsService.save(sysFunDetails);
			
			//插流量购买配置计划
			FlowSettingEntity entity = new FlowSettingEntity();
			entity.setMerchantNo(merchantNo);
			entity.setCompleteCount(0);
			entity.setCreateBy(ShiroUtils.getUserId());
			entity.setCreateTime(new Date());
			entity.setStatus(0);
			entity.setAfterAmount(remainingSum);
			entity.setFlowCount(flowCount);
			entity.setChannelId(channelId);
			this.flowSettingService.save(entity);
		}
		
		return R.ok();
	}
	
}
