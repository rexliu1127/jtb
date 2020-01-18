package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 用户余额
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
public class TxUserBalanceEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long userId;
	//商家编号
	private String merchantNo;
	//用户余额
	private BigDecimal balance;

	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：用户余额
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	/**
	 * 获取：用户余额
	 */
	public BigDecimal getBalance() {
		return balance;
	}
}
