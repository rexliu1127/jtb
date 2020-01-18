package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.tx.enums.ComplainResult;
import io.grx.modules.tx.enums.ComplainType;


/**
 * 借条申诉
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-13 18:10:01
 */
public class TxComplainEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long complainId;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	//申诉类型
	private ComplainType complainType;
	//申诉说明
	private String remark;
	//凭证照片路径
	private String imagePath;
	//状态
	private ComplainResult status;
	//处理人ID
	private Long processorId;
	//申诉说明
	private String processorComment;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setComplainId(Long complainId) {
		this.complainId = complainId;
	}
	/**
	 * 获取：
	 */
	public Long getComplainId() {
		return complainId;
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
	 * 设置：借条ID
	 */
	public void setTxId(Long txId) {
		this.txId = txId;
	}
	/**
	 * 获取：借条ID
	 */
	public Long getTxId() {
		return txId;
	}
	/**
	 * 设置：申诉类型
	 */
	public void setComplainType(ComplainType complainType) {
		this.complainType = complainType;
	}
	/**
	 * 获取：申诉类型
	 */
	public ComplainType getComplainType() {
		return complainType;
	}
	/**
	 * 设置：申诉说明
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：申诉说明
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：凭证照片路径
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	/**
	 * 获取：凭证照片路径
	 */
	public String getImagePath() {
		return imagePath;
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
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
