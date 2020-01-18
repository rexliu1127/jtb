package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 用户奖励
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-22 21:24:07
 */
public class TxUserRewardEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//用户ID
	private Long userId;
	private Long inviteeUserId;
	//借条ID
	private Long txId;
	//展期ID
	private Long extensionId;
	//推广级别
	private Integer level;
	//奖励
	private BigDecimal reward;
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
	 * 设置：展期ID
	 */
	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}
	/**
	 * 获取：展期ID
	 */
	public Long getExtensionId() {
		return extensionId;
	}
	/**
	 * 设置：推广级别
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	/**
	 * 获取：推广级别
	 */
	public Integer getLevel() {
		return level;
	}
	/**
	 * 设置：奖励
	 */
	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
	/**
	 * 获取：奖励
	 */
	public BigDecimal getReward() {
		return reward;
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

	public Long getInviteeUserId() {
		return inviteeUserId;
	}

	public void setInviteeUserId(Long inviteeUserId) {
		this.inviteeUserId = inviteeUserId;
	}
}
