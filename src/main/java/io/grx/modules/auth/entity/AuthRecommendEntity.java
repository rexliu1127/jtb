package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 申请单推荐记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-09-28 22:57:07
 */
public class AuthRecommendEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//用户手机
	private String mobile;
	//源用户ID
	private Long fromUserId;
	//目标用户ID
	private Long toUserId;
	//操作人用户ID
	private Long createUserId;
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
	 * 设置：用户手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：用户手机
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：源用户ID
	 */
	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}
	/**
	 * 获取：源用户ID
	 */
	public Long getFromUserId() {
		return fromUserId;
	}
	/**
	 * 设置：目标用户ID
	 */
	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}
	/**
	 * 获取：目标用户ID
	 */
	public Long getToUserId() {
		return toUserId;
	}
	/**
	 * 设置：操作人用户ID
	 */
	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}
	/**
	 * 获取：操作人用户ID
	 */
	public Long getCreateUserId() {
		return createUserId;
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
