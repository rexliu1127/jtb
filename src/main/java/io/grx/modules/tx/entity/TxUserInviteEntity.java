package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户邀请记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 14:34:31
 */
public class TxUserInviteEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//用户ID
	private Long userId;
	//邀请人用户ID
	private Long inviterUserId;
	//邀请人渠道商ID
	private String inviterAgentNo;
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
	 * 设置：邀请人用户ID
	 */
	public void setInviterUserId(Long inviterUserId) {
		this.inviterUserId = inviterUserId;
	}
	/**
	 * 获取：邀请人用户ID
	 */
	public Long getInviterUserId() {
		return inviterUserId;
	}
	/**
	 * 设置：邀请人渠道商ID
	 */
	public void setInviterAgentNo(String inviterAgentNo) {
		this.inviterAgentNo = inviterAgentNo;
	}
	/**
	 * 获取：邀请人渠道商ID
	 */
	public String getInviterAgentNo() {
		return inviterAgentNo;
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
