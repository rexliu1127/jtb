package io.grx.wx.dto;

import java.io.Serializable;

public class TxRepayDto implements Serializable {

    private Long txId;
    private String borrowerName;
    private String lenderName;

    private int amount;
    private double interest;
    private int rate;
    private String beginDate;
    private String endDate;
    private int usageType;
    private String usageTypeLabel;
    private int txStatus;
    private String txStatusLabel;
    private int outstandingAmount;
    private double outstandingInterest;

    private int daysToPay;
    private int repaymentStatus;

    private String repaymentStatusLabel;

    private int extensionStatus;
    private String extensionStatusLabel;

    public Long getTxId() {
        return txId;
    }

    public void setTxId(final Long txId) {
        this.txId = txId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(final String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(final String lenderName) {
        this.lenderName = lenderName;
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

    public int getDaysToPay() {
        return daysToPay;
    }

    public void setDaysToPay(final int daysToPay) {
        this.daysToPay = daysToPay;
    }

    public int getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(final int repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public int getUsageType() {
        return usageType;
    }

    public void setUsageType(final int usageType) {
        this.usageType = usageType;
    }

    public String getRepaymentStatusLabel() {
        return repaymentStatusLabel;
    }

    public void setRepaymentStatusLabel(final String repaymentStatusLabel) {
        this.repaymentStatusLabel = repaymentStatusLabel;
    }

    public int getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(final int txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxStatusLabel() {
        return txStatusLabel;
    }

    public void setTxStatusLabel(final String txStatusLabel) {
        this.txStatusLabel = txStatusLabel;
    }


    public int getExtensionStatus() {
        return extensionStatus;
    }

    public void setExtensionStatus(final int extensionStatus) {
        this.extensionStatus = extensionStatus;
    }

    public String getExtensionStatusLabel() {
        return extensionStatusLabel;
    }

    public void setExtensionStatusLabel(final String extensionStatusLabel) {
        this.extensionStatusLabel = extensionStatusLabel;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(final double interest) {
        this.interest = interest;
    }

    public String getUsageTypeLabel() {
        return usageTypeLabel;
    }

    public void setUsageTypeLabel(final String usageTypeLabel) {
        this.usageTypeLabel = usageTypeLabel;
    }

    public int getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(final int outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public double getOutstandingInterest() {
        return outstandingInterest;
    }

    public void setOutstandingInterest(final double outstandingInterest) {
        this.outstandingInterest = outstandingInterest;
    }
}
