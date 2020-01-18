package io.grx.modules.tx.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TxMonthSummary implements Serializable {
    private String period;
    private BigDecimal borrowedAmount;
    private BigDecimal repaidAmount;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public BigDecimal getBorrowedAmount() {
        return borrowedAmount;
    }

    public void setBorrowedAmount(BigDecimal borrowedAmount) {
        this.borrowedAmount = borrowedAmount;
    }

    public BigDecimal getRepaidAmount() {
        return repaidAmount;
    }

    public void setRepaidAmount(BigDecimal repaidAmount) {
        this.repaidAmount = repaidAmount;
    }
}
