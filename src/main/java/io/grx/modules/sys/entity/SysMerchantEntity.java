package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.sys.enums.AccountStatus;


/**
 * 商家
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-06-09 23:41:29
 */
public class SysMerchantEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//商家编号
	private String merchantNo;
	//商家名称
	private String name;
	//注册手机
	private String mobile;
	//管理员用户ID
	private Long adminUserId;
	//状态  0：禁用   1：正常
	private AccountStatus status;
	//创建时间
	private Date createTime;
	//更新时间
	private Date updateTime;

	private String logo;

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
	 * 设置：商家名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：商家名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：注册手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：注册手机
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：管理员用户ID
	 */
	public void setAdminUserId(Long adminUserId) {
		this.adminUserId = adminUserId;
	}
	/**
	 * 获取：管理员用户ID
	 */
	public Long getAdminUserId() {
		return adminUserId;
	}
	/**
	 * 设置：状态  0：禁用   1：正常
	 */
	public void setStatus(AccountStatus status) {
		this.status = status;
	}
	/**
	 * 获取：状态  0：禁用   1：正常
	 */
	public AccountStatus getStatus() {
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
	/**
	 * 设置：更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
}
