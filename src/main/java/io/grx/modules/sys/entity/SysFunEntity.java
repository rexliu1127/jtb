package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 充值表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 20:46:07
 */
public class SysFunEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//商家编号
	private String merchantNo;
	//总额
	private BigDecimal totalAmount;
	//可用余额
	private BigDecimal remainingSum;
	//创建时间
	private Date createTime;
	//是否有风险
	private Integer isrist;
	//商户名称
	private String merchantName;

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
	 * 设置：总额
	 */
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	/**
	 * 获取：总额
	 */
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	/**
	 * 设置：可用余额
	 */
	public void setRemainingSum(BigDecimal remainingSum) {
		this.remainingSum = remainingSum;
	}
	/**
	 * 获取：可用余额
	 */
	public BigDecimal getRemainingSum() {
		return remainingSum;
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
	 * 设置：是否有风险
	 */
	public void setIsrist(Integer isrist) {
		this.isrist = isrist;
	}
	/**
	 * 获取：是否有风险
	 */
	public Integer getIsrist() {
		return isrist;
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
}
