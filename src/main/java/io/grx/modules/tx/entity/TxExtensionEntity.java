package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.grx.modules.tx.enums.ExtensionStatus;


/**
 * 展期记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-03 00:42:11
 */
public class TxExtensionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long extensionId;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	private Integer extendAmount;
	private Integer oldRate;
	//展期利率
	private Integer rate;
	//新到期日期
	private Date newEndDate;
	private Date oldEndDate;
	//状态
	private ExtensionStatus status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;
	// 展期收费
	private BigDecimal feeAmount;

	/**
	 * 设置：
	 */
	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}
	/**
	 * 获取：
	 */
	public Long getExtensionId() {
		return extensionId;
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
	 * 设置：展期利率
	 */
	public void setRate(Integer rate) {
		this.rate = rate;
	}
	/**
	 * 获取：展期利率
	 */
	public Integer getRate() {
		return rate;
	}
	/**
	 * 设置：新到期日期
	 */
	public void setNewEndDate(Date newEndDate) {
		this.newEndDate = newEndDate;
	}
	/**
	 * 获取：新到期日期
	 */
	public Date getNewEndDate() {
		return newEndDate;
	}

	public ExtensionStatus getStatus() {
		return status;
	}

	public void setStatus(final ExtensionStatus status) {
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

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(final BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

	public Integer getOldRate() {
		return oldRate;
	}

	public void setOldRate(final Integer oldRate) {
		this.oldRate = oldRate;
	}

	public Date getOldEndDate() {
		return oldEndDate;
	}

	public void setOldEndDate(final Date oldEndDate) {
		this.oldEndDate = oldEndDate;
	}

	public Integer getExtendAmount() {
		return extendAmount;
	}

	public void setExtendAmount(final Integer extendAmount) {
		this.extendAmount = extendAmount;
	}
}
