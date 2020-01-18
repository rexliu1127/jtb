package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 出借人
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-06 14:48:57
 */
public class TxLenderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//电话
	private String mobile;
	//姓名
	private String name;
	//商家编号
	private String merchantNo;
	//创建时间
	private Date createTime;
	//状态
	private Integer status;

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
	 * 设置：电话
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * 获取：电话
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * 设置：姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
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
}
