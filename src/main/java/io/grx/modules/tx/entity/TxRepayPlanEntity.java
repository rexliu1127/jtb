package io.grx.modules.tx.entity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 还款计划
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-05-25 22:23:48
 */
public class TxRepayPlanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long planId;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	//到期日期
	private Date beginDate;
	//到期日期
	private Date endDate;
	//借款金额
	private Integer plannedAmount;
	//预计利息
	private BigDecimal plannedInterest;
	//还款金额
	private Integer actualAmount;
	//还款利息
	private BigDecimal actualInterest;
	//还期日期
	private Date repayDate;
	//更新时间
	private Date updateTime;

	/**
	 * 设置：
	 */
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	/**
	 * 获取：
	 */
	public Long getPlanId() {
		return planId;
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
	 * 设置：到期日期
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	/**
	 * 获取：到期日期
	 */
	public Date getBeginDate() {
		return beginDate;
	}
	/**
	 * 设置：到期日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 获取：到期日期
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * 设置：借款金额
	 */
	public void setPlannedAmount(Integer plannedAmount) {
		this.plannedAmount = plannedAmount;
	}
	/**
	 * 获取：借款金额
	 */
	public Integer getPlannedAmount() {
		return plannedAmount;
	}
	/**
	 * 设置：预计利息
	 */
	public void setPlannedInterest(BigDecimal plannedInterest) {
		this.plannedInterest = plannedInterest;
	}
	/**
	 * 获取：预计利息
	 */
	public BigDecimal getPlannedInterest() {
		return plannedInterest;
	}
	/**
	 * 设置：还款金额
	 */
	public void setActualAmount(Integer actualAmount) {
		this.actualAmount = actualAmount;
	}
	/**
	 * 获取：还款金额
	 */
	public Integer getActualAmount() {
		return actualAmount;
	}
	/**
	 * 设置：还款利息
	 */
	public void setActualInterest(BigDecimal actualInterest) {
		this.actualInterest = actualInterest;
	}
	/**
	 * 获取：还款利息
	 */
	public BigDecimal getActualInterest() {
		return actualInterest;
	}
	/**
	 * 设置：还期日期
	 */
	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}
	/**
	 * 获取：还期日期
	 */
	public Date getRepayDate() {
		return repayDate;
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
}
