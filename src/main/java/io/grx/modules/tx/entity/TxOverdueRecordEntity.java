package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 逾期记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-11 11:36:11
 */
public class TxOverdueRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	//逾期开始日期
	private Date overdueDate;
	//逾期结束日期
	private Date overdueEndDate;
	//逾期天数
	private Integer overduedays;
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
	 * 设置：逾期开始日期
	 */
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}
	/**
	 * 获取：逾期开始日期
	 */
	public Date getOverdueDate() {
		return overdueDate;
	}
	/**
	 * 设置：逾期结束日期
	 */
	public void setOverdueEndDate(Date overdueEndDate) {
		this.overdueEndDate = overdueEndDate;
	}
	/**
	 * 获取：逾期结束日期
	 */
	public Date getOverdueEndDate() {
		return overdueEndDate;
	}
	/**
	 * 设置：逾期天数
	 */
	public void setOverduedays(Integer overduedays) {
		this.overduedays = overduedays;
	}
	/**
	 * 获取：逾期天数
	 */
	public Integer getOverduedays() {
		return overduedays;
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
