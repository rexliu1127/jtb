package io.grx.modules.tx.entity;

import java.io.Serializable;


/**
 * 交易用户密码
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 10:41:32
 */
public class TxUserPasswordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long userId;
	//商家编号
	private String merchantNo;
	//交易密码
	private String password;
	//盐
	private String salt;

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
	 * 设置：交易密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：交易密码
	 */
	public String getPassword() {
		return password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(final String salt) {
		this.salt = salt;
	}
}
