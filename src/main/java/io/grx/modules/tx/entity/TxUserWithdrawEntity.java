package io.grx.modules.tx.entity;

import io.grx.modules.tx.enums.WithdrawalStatus;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


/**
 * 用户提现记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-07-29 18:10:26
 */
public class TxUserWithdrawEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//
	private Long userId;
	//商家编号
	private String merchantNo;
	//提现金额(含手续费)
	private BigDecimal amount;
	//提现手续费
	private BigDecimal feeAmount;
	//提现状态
	private WithdrawalStatus status = WithdrawalStatus.NEW;
	//提交时间
	private Date createTime;
	//
	private Long approvalUserId;
	//审核时间
	private Date approvalTime;
	//放款时间
	private Date completeTime;

	private String bankAccount;

	private String bankName;

	private String tranxId;

	private String tranxCode;

	private String tranxMessage;

	private long actualAmount;

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
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：提现金额(含手续费)
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	/**
	 * 获取：提现金额(含手续费)
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * 设置：提现手续费
	 */
	public void setFeeAmount(BigDecimal feeAmount) {
		this.feeAmount = feeAmount;
	}
	/**
	 * 获取：提现手续费
	 */
	public BigDecimal getFeeAmount() {
		return feeAmount;
	}
	/**
	 * 设置：提现状态
	 */
	public void setStatus(WithdrawalStatus status) {
		this.status = status;
	}
	/**
	 * 获取：提现状态
	 */
	public WithdrawalStatus getStatus() {
		return status;
	}
	/**
	 * 设置：提交时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：提交时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：审核时间
	 */
	public void setApprovalTime(Date approvalTime) {
		this.approvalTime = approvalTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getApprovalTime() {
		return approvalTime;
	}
	/**
	 * 设置：放款时间
	 */
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	/**
	 * 获取：放款时间
	 */
	public Date getCompleteTime() {
		return completeTime;
	}

	public Long getApprovalUserId() {
		return approvalUserId;
	}

	public void setApprovalUserId(Long approvalUserId) {
		this.approvalUserId = approvalUserId;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String getTranxCode() {
		return tranxCode;
	}

	public void setTranxCode(String tranxCode) {
		this.tranxCode = tranxCode;
	}

	public String getTranxMessage() {
		return tranxMessage;
	}

	public void setTranxMessage(String tranxMessage) {
		this.tranxMessage = tranxMessage;
	}

    public long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(long actualAmount) {
        this.actualAmount = actualAmount;
    }
}
