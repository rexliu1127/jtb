package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.tx.enums.ComplainResult;


/**
 * 借条申诉处理结果
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
public class TxComplainResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//借条申诉ID
	private Long complainId;
	//状态
	private ComplainResult status;
	//处理人ID
	private Long processorId;
	//申诉说明
	private String processorComment;
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
	 * 设置：借条申诉ID
	 */
	public void setComplainId(Long complainId) {
		this.complainId = complainId;
	}
	/**
	 * 获取：借条申诉ID
	 */
	public Long getComplainId() {
		return complainId;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(ComplainResult status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public ComplainResult getStatus() {
		return status;
	}
	/**
	 * 设置：处理人ID
	 */
	public void setProcessorId(Long processorId) {
		this.processorId = processorId;
	}
	/**
	 * 获取：处理人ID
	 */
	public Long getProcessorId() {
		return processorId;
	}
	/**
	 * 设置：申诉说明
	 */
	public void setProcessorComment(String processorComment) {
		this.processorComment = processorComment;
	}
	/**
	 * 获取：申诉说明
	 */
	public String getProcessorComment() {
		return processorComment;
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
