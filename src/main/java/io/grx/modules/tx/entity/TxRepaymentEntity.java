package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.tx.enums.RepaymentStatus;
import io.grx.modules.tx.enums.RepaymentType;


/**
 * 还款记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
public class TxRepaymentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long repaymentId;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	//还款方式
	private RepaymentType repaymentType;
	//状态
	private RepaymentStatus status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setRepaymentId(Long repaymentId) {
		this.repaymentId = repaymentId;
	}
	/**
	 * 获取：
	 */
	public Long getRepaymentId() {
		return repaymentId;
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

	public RepaymentType getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(final RepaymentType repaymentType) {
		this.repaymentType = repaymentType;
	}

	public RepaymentStatus getStatus() {
		return status;
	}

	public void setStatus(final RepaymentStatus status) {
		this.status = status;
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
