package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 丢失记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-13 01:25:30
 */
public class AuthMissingRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//token
	private String verifyToken;
	//mobile
	private String mobile;
	//状态
	private Integer status;
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
	 * 设置：token
	 */
	public void setVerifyToken(String verifyToken) {
		this.verifyToken = verifyToken;
	}
	/**
	 * 获取：token
	 */
	public String getVerifyToken() {
		return verifyToken;
	}
	/**
	 * 设置：mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：mobile
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
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
