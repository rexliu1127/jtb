package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 充值类型表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-13 16:17:36
 */
public class SysFunTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//商家编号
	private String merchantNo;
	//充值类型
	private Integer funType;
	//单笔费用
	private BigDecimal singleFee;
	//创建时间
	private Date createTime;
	//公司名称
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
	 * 设置：充值类型
	 */
	public void setFunType(Integer funType) {
		this.funType = funType;
	}
	/**
	 * 获取：充值类型
	 */
	public Integer getFunType() {
		return funType;
	}
	/**
	 * 设置：单笔费用
	 */
	public void setSingleFee(BigDecimal singleFee) {
		this.singleFee = singleFee;
	}
	/**
	 * 获取：单笔费用
	 */
	public BigDecimal getSingleFee() {
		return singleFee;
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
	 * 获取：公司名称
	 */
	public String getMerchantName() {
		return merchantName;
	}
	/**
	 * 设置：公司名称
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
}
