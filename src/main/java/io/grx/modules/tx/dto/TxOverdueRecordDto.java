package io.grx.modules.tx.dto;

public class TxOverdueRecordDto {
    private Long txId;
    private int amount;
    private String beginDate;
    private String endDate;
    private String overdueDate;
    private String overdueEndDate;
    private int overdueDays;

    public Long getTxId() {
        return txId;
    }

    public void setTxId(final Long txId) {
        this.txId = txId;
    }

    public String getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(final String overdueDate) {
        this.overdueDate = overdueDate;
    }

    public String getOverdueEndDate() {
        return overdueEndDate;
    }

    public void setOverdueEndDate(final String overdueEndDate) {
        this.overdueEndDate = overdueEndDate;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(final int overdueDays) {
        this.overdueDays = overdueDays;
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
}
