package io.grx.modules.tx.dto;

import java.util.Date;

public class TxComplainVo {
    private Long complainId;
    private Long txId;
    private String borrowerName;
    private Long borrowerUserId;
    private String borrowerMobile;
    private String borrowerIdNo;
    private String lenderName;
    private Long lenderUserId;
    private String lenderMobile;
    private String lenderIdNo;

    private int amount;
    private String beginDate;
    private String endDate;
    private String txStatusLabel;
    private String txUuid;

    private String complainTypeDesc;
    private String remark;
    private String[] imagePaths;
    private int complainResult;
    private String complainResultDesc;
    private String processorComment;

    private Date createTime;

    public Long getComplainId() {
        return complainId;
    }

    public void setComplainId(final Long complainId) {
        this.complainId = complainId;
    }

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

    public Long getBorrowerUserId() {
        return borrowerUserId;
    }

    public void setBorrowerUserId(final Long borrowerUserId) {
        this.borrowerUserId = borrowerUserId;
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

    public Long getLenderUserId() {
        return lenderUserId;
    }

    public void setLenderUserId(final Long lenderUserId) {
        this.lenderUserId = lenderUserId;
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

    public String getComplainTypeDesc() {
        return complainTypeDesc;
    }

    public void setComplainTypeDesc(final String complainTypeDesc) {
        this.complainTypeDesc = complainTypeDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(final String remark) {
        this.remark = remark;
    }

    public String[] getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(final String[] imagePaths) {
        this.imagePaths = imagePaths;
    }

    public String getComplainResultDesc() {
        return complainResultDesc;
    }

    public void setComplainResultDesc(final String complainResultDesc) {
        this.complainResultDesc = complainResultDesc;
    }

    public String getProcessorComment() {
        return processorComment;
    }

    public void setProcessorComment(final String processorComment) {
        this.processorComment = processorComment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
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

    public String getTxStatusLabel() {
        return txStatusLabel;
    }

    public void setTxStatusLabel(final String txStatusLabel) {
        this.txStatusLabel = txStatusLabel;
    }

    public String getTxUuid() {
        return txUuid;
    }

    public void setTxUuid(final String txUuid) {
        this.txUuid = txUuid;
    }

    public int getComplainResult() {
        return complainResult;
    }

    public void setComplainResult(final int complainResult) {
        this.complainResult = complainResult;
    }
}
