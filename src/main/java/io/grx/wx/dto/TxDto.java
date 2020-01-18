package io.grx.wx.dto;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.tx.enums.TxStatus;

/**
 * 借条DTO
 */
public class TxDto implements Serializable {
    private Long txId;
    private String borrowerName;
    private String borrowerHeadImg;
    private String lenderName;
    private String lenderHeadImg;
    private int amount;
    private int rate;
    private Date beginDate;
    private Date endDate;
    private Date repaymentDate;
    private long daysBeforeInvalid;
    private long useDay;
    private TxStatus txStatus;
    private String statusLabel;
    private boolean createdByBorrower;
    private boolean createdByLender;

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

    public String getBorrowerHeadImg() {
        return borrowerHeadImg;
    }

    public void setBorrowerHeadImg(final String borrowerHeadImg) {
        this.borrowerHeadImg = borrowerHeadImg;
    }

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(final String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLenderHeadImg() {
        return lenderHeadImg;
    }

    public void setLenderHeadImg(final String lenderHeadImg) {
        this.lenderHeadImg = lenderHeadImg;
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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(final Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public long getDaysBeforeInvalid() {
        return daysBeforeInvalid;
    }

    public void setDaysBeforeInvalid(final long daysBeforeInvalid) {
        this.daysBeforeInvalid = daysBeforeInvalid;
    }

    public long getUseDay() {
        return useDay;
    }

    public void setUseDay(final long useDay) {
        this.useDay = useDay;
    }

    public TxStatus getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(final TxStatus txStatus) {
        this.txStatus = txStatus;
    }

    public boolean isCreatedByBorrower() {
        return createdByBorrower;
    }

    public void setCreatedByBorrower(final boolean createdByBorrower) {
        this.createdByBorrower = createdByBorrower;
    }

    public boolean isCreatedByLender() {
        return createdByLender;
    }

    public void setCreatedByLender(final boolean createdByLender) {
        this.createdByLender = createdByLender;
    }

    public Date getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(final Date repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(final String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
