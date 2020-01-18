package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 充值记录表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 20:46:07
 */
public class SysFunRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//充值金额
	private BigDecimal funAmount;
	//创建时间
	private Date createTime;
	//商家编号
	private String merchantNo;
	//创建人
	private Long createBy;
	//商户名称
	private String merchantName;
	//可用金额(充值前)
	private BigDecimal availableAmount;
	//当前可用金额(充值后)
	private BigDecimal currentAvailableAmount;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：充值金额
	 */
	public void setFunAmount(BigDecimal funAmount) {
		this.funAmount = funAmount;
	}
	/**
	 * 获取：充值金额
	 */
	public BigDecimal getFunAmount() {
		return funAmount;
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
	 * 设置：创建人
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：商户名称
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * 获取：商户名称
	 */
	public String getMerchantName() {
		return merchantName;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getCurrentAvailableAmount() {
		return currentAvailableAmount;
	}

	public void setCurrentAvailableAmount(BigDecimal currentAvailableAmount) {
		this.currentAvailableAmount = currentAvailableAmount;
	}
}
