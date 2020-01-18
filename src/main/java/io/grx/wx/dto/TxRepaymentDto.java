package io.grx.wx.dto;

import java.io.Serializable;

public class TxRepaymentDto implements Serializable {
    private long repaymentId;
    private long txId;
    private String borrowerName;
    private String lenderName;
    private int amount;
    private double interest;
    private String beginDate;
    private String endDate;
    private String actualEndDate;
    private int repaymentStatus;
    private String repaymentStatusLabel;
    private int outstandingAmount;
    private double outstandingInterest;

    public long getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(final long repaymentId) {
        this.repaymentId = repaymentId;
    }

    public long getTxId() {
        return txId;
    }

    public void setTxId(final long txId) {
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

    public String getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(final String actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public int getRepaymentStatus() {
        return repaymentStatus;
    }

    public void setRepaymentStatus(final int repaymentStatus) {
        this.repaymentStatus = repaymentStatus;
    }

    public String getRepaymentStatusLabel() {
        return repaymentStatusLabel;
    }

    public void setRepaymentStatusLabel(final String repaymentStatusLabel) {
        this.repaymentStatusLabel = repaymentStatusLabel;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(final double interest) {
        this.interest = interest;
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
