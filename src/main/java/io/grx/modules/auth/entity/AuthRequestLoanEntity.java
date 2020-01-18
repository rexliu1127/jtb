package io.grx.modules.auth.entity;

import io.grx.modules.auth.enums.RequestStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 申请单-借款信息
 * 
 */
public class AuthRequestLoanEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long requestId;
	private BigDecimal amount;
	private Integer period;
	private Integer periodType;
	private BigDecimal interest;
	private BigDecimal serviceFee;
	private Date repayDate;
	private Date factRepayDate;
	private BigDecimal repayAmount;
	private BigDecimal factRepayAmount;
	private Integer overdueDay;
	private BigDecimal overdueFee;
	
	/**
	 * 借款人姓名
	 */
	private String name;

	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private Date createTime;
	private Integer status;
	private String statusStr;
	private String requestNo;
	
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusStr() {
		return RequestStatus.valueOf(this.status).getDisplayName();
	}


	public String getRequestNo() {
		return requestNo;
	}
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Integer getPeriodType() {
		return periodType;
	}
	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}
	public Date getRepayDate() {
		return repayDate;
	}
	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}
	public Date getFactRepayDate() {
		return factRepayDate;
	}
	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}
	public BigDecimal getRepayAmount() {
		return repayAmount;
	}
	public void setRepayAmount(BigDecimal repayAmount) {
		this.repayAmount = repayAmount;
	}
	public BigDecimal getFactRepayAmount() {
		return factRepayAmount;
	}
	public void setFactRepayAmount(BigDecimal factRepayAmount) {
		this.factRepayAmount = factRepayAmount;
	}
	public Integer getOverdueDay() {
		return overdueDay;
	}
	public void setOverdueDay(Integer overdueDay) {
		this.overdueDay = overdueDay;
	}
	public BigDecimal getOverdueFee() {
		return overdueFee;
	}
	public void setOverdueFee(BigDecimal overdueFee) {
		this.overdueFee = overdueFee;
	}
}
