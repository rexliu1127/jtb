package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 用户费用明细表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-12-17 14:52:21
 */
public class SysFunDetailsEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//关联申请用户id
	private Long userId;
	//借款人手机
	private String borrowerPhone;
	//关联费用类型
	private Long funType;
	//费用
	private BigDecimal amount;
	//创建时间
	private Date createTime;
	//商户名称
	private String merchantName;
	//商家编号
	private String merchantNo;
	//用户名称
	private String userName;
	//交易编号
	private String taskId;

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
	 * 设置：关联申请用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：关联申请用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：借款人手机
	 */
	public void setBorrowerPhone(String borrowerPhone) {
		this.borrowerPhone = borrowerPhone;
	}
	/**
	 * 获取：借款人手机
	 */
	public String getBorrowerPhone() {
		return borrowerPhone;
	}
	/**
	 * 设置：关联费用类型
	 */
	public void setFunType(Long funType) {
		this.funType = funType;
	}
	/**
	 * 获取：关联费用类型
	 */
	public Long getFunType() {
		return funType;
	}
	/**
	 * 设置：费用
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取：费用
	 */
	public BigDecimal getAmount() {
		return amount;
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
	 * 设置：商户名称
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * 获取：商户名称
	 */
	public String getMerchantName() {
		return merchantName;
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
	 * 设置：用户名称
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：用户名称
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：交易编号
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	/**
	 * 获取：交易编号
	 */
	public String getTaskId() {
		return taskId;
	}
}
