package io.grx.modules.flow.entity;

import java.io.Serializable;
import java.util.Date;

public class FlowAllocationRecordEntity implements Serializable{

	private static final long serialVersionUID = 2082460619551825266L;
	
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 流量分配设置的ID  FlowSettingEntity.java getId
	 */
	private Long settingId;
	/**
	 * 原始流量ID,即从AuthRequest表中复制过来的原始ID
	 */
	private Long sourceRequestId;
	/**
	 * 新的申请表
	 */
	private Long newRequestId;
	
	/**
	 * 分发时的状态 0:未提交 1: 已提交 -1:不操作
	 * 如果是0:未提交,那么需要后续跟踪
	 */
	private Integer reqStatus;
	
	public Integer getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(Integer reqStatus) {
		this.reqStatus = reqStatus;
	}
	public Long getNewRequestId() {
		return newRequestId;
	}
	public void setNewRequestId(Long newRequestId) {
		this.newRequestId = newRequestId;
	}
	/**
	 * 创建时间
	 */
	private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSettingId() {
		return settingId;
	}
	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}
	public Long getSourceRequestId() {
		return sourceRequestId;
	}
	public void setSourceRequestId(Long sourceRequestId) {
		this.sourceRequestId = sourceRequestId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
