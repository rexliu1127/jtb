package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 交易用户关系
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2017-12-01 22:44:07
 */
public class TxUserRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long relationId;
	//商家编号
	private String merchantNo;
	//用户ID
	private Long userId;
	//朋友ID
	private Long friendUserId;
	//创建时间
	private Date createTime;

	/**
	 * 设置：
	 */
	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}
	/**
	 * 获取：
	 */
	public Long getRelationId() {
		return relationId;
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
	 * 设置：朋友ID
	 */
	public void setFriendUserId(Long friendUserId) {
		this.friendUserId = friendUserId;
	}
	/**
	 * 获取：朋友ID
	 */
	public Long getFriendUserId() {
		return friendUserId;
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
