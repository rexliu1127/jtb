package io.grx.modules.auth.entity;

import io.grx.modules.auth.enums.RequestStatus;

import java.io.Serializable;
import java.util.Date;


/**
 * 申请单历史
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-20 21:46:18
 */
public class AuthRequestHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//申请单ID
	private Long requestId;
	//处理系统用户ID
	private Long processorId;
	//处理状态
	private RequestStatus status;
	//审核备注
	private String userRemark;
	//跟踪备注
	private String adminRemark;
	//操作人ID
	private Long createUserId;
	//创建时间
	private Date createTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：商家编号
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	/**
	 * 获取：商家编号
	 */
	public String getMerchantNo() {
		return merchantNo;
	}
	/**
	 * 设置：申请单ID
	 */
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	/**
	 * 获取：申请单ID
	 */
	public Long getRequestId() {
		return requestId;
	}
	/**
	 * 设置：处理系统用户ID
	 */
	public void setProcessorId(Long processorId) {
		this.processorId = processorId;
	}
	/**
	 * 获取：处理系统用户ID
	 */
	public Long getProcessorId() {
		return processorId;
	}
	/**
	 * 设置：处理状态
	 */
	public void setStatus(RequestStatus status) {
		this.status = status;
	}
	/**
	 * 获取：处理状态
	 */
	public RequestStatus getStatus() {
		return status;
	}
	/**
	 * 设置：审核备注
	 */
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	/**
	 * 获取：审核备注
	 */
	public String getUserRemark() {
		return userRemark;
	}
	/**
	 * 设置：跟踪备注
	 */
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}
	/**
	 * 获取：跟踪备注
	 */
	public String getAdminRemark() {
		return adminRemark;
	}
	/**
	 * 设置：操作人ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：操作人ID
	 */
	public Long getCreateUserId() {
		return createUserId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
