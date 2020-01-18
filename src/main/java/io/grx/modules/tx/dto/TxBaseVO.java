package io.grx.modules.tx.dto;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import io.grx.modules.tx.enums.TxStatus;

public class TxBaseVO implements Serializable {
    private Long txId;
    private int amount;
    private int outstandingAmount;
    private int rate;
    private double interest;
    private double feeAmount;
    private String beginDate = StringUtils.EMPTY;
    private String endDate = StringUtils.EMPTY;
    private TxStatus status;
    //交易全局ID
    private String txUuid;
    private String borrowerName = StringUtils.EMPTY;
    private String borrowerMobile = StringUtils.EMPTY;
    private String borrowerIdNo = StringUtils.EMPTY;
    private String lenderName = StringUtils.EMPTY;
    private String lenderMobile = StringUtils.EMPTY;
    private String lenderIdNo = StringUtils.EMPTY;

    private String actualEndDate = StringUtils.EMPTY;
    private String repayDate = StringUtils.EMPTY;

    private double penaltyFee;
    private int extensionCount;
    private double extensionFee;

    public Long getTxId() {
        return txId;
    }

    public void setTxId(final Long txId) {
        this.txId = txId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(final int rate) {
        this.rate = rate;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(final double interest) {
        this.interest = interest;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(final double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(final String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(final TxStatus status) {
        this.status = status;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(final String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerMobile() {
        return borrowerMobile;
    }

    public void setBorrowerMobile(final String borrowerMobile) {
        this.borrowerMobile = borrowerMobile;
    }

    public String getBorrowerIdNo() {
        return borrowerIdNo;
    }

    public void setBorrowerIdNo(final String borrowerIdNo) {
        this.borrowerIdNo = borrowerIdNo;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(final String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLenderMobile() {
        return lenderMobile;
    }

    public void setLenderMobile(final String lenderMobile) {
        this.lenderMobile = lenderMobile;
    }

    public String getLenderIdNo() {
        return lenderIdNo;
    }

    public void setLenderIdNo(final String lenderIdNo) {
        this.lenderIdNo = lenderIdNo;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(final String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public double getPenaltyFee() {
        return penaltyFee;
    }

    public void setPenaltyFee(final double penaltyFee) {
        this.penaltyFee = penaltyFee;
    }

    public int getExtensionCount() {
        return extensionCount;
    }

    public void setExtensionCount(final int extensionCount) {
        this.extensionCount = extensionCount;
    }

    public double getExtensionFee() {
        return extensionFee;
    }

    public void setExtensionFee(final double extensionFee) {
        this.extensionFee = extensionFee;
    }

    public int getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(int outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    /**
     * 设置：交易全局ID
     */
    public void setTxUuid(String txUuid) {
        this.txUuid = txUuid;
    }
    /**
     * 获取：交易全局ID
     */
    public String getTxUuid() {
        return txUuid;
    }
}
