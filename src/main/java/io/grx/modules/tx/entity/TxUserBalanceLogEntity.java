package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户余额记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-12 11:08:17
 */
public class TxUserBalanceLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//用户ID
	private Long userId;
	//借条ID
	private Long txId;
	//展期ID
	private Long extensionId;
	//商家编号
	private String merchantNo;
	//余额收入
	private BigDecimal income;
	//余额支出
	private BigDecimal expense;
	//用户余额
	private BigDecimal balance;

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
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：余额收入
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * 获取：余额收入
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * 设置：余额支出
	 */
	public void setExpense(BigDecimal expense) {
		this.expense = expense;
	}
	/**
	 * 获取：余额支出
	 */
	public BigDecimal getExpense() {
		return expense;
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

	public Long getExtensionId() {
		return extensionId;
	}

	public void setExtensionId(final Long extensionId) {
		this.extensionId = extensionId;
	}
}
