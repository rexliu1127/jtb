package io.grx.modules.flow.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



public class FlowSettingEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 商户号
	 */
	private String merchantNo;
	/**
	 * 购买流量数
	 */
	private Integer flowCount;
	/**
	 * 已完成分配的流量数
	 */
	private Integer completeCount;
	/**
	 * 状态
	 * 0:推送中
	 * 1:推送完成
	 * -1:停止推送(如欠费、认为停止)
	 */
	private Integer status;
	
	/**
	 * 目标渠道
	 */
	private Long channelId;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 创建人
	 */
	private Long createBy;
	
	/**
	 * 采购之后的余额
	 */
	private BigDecimal afterAmount;
	
	
	public BigDecimal getAfterAmount() {
		return afterAmount;
	}
	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public Integer getFlowCount() {
		return flowCount;
	}
	public void setFlowCount(Integer flowCount) {
		this.flowCount = flowCount;
	}
	public Integer getCompleteCount() {
		return completeCount;
	}
	public void setCompleteCount(Integer completeCount) {
		this.completeCount = completeCount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
}
