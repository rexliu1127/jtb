package io.grx.modules.tx.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TxBaseSummary implements Serializable {
    private long totalTxCount;
    private long totalExCount;
    private BigDecimal totalBorrowedAmount;
    private BigDecimal totalExtendedAmount;
    private BigDecimal totalRepaidAmount;
    private BigDecimal totalOverdueAmount;

    public long getTotalTxCount() {
        return totalTxCount;
    }

    public void setTotalTxCount(long totalTxCount) {
        this.totalTxCount = totalTxCount;
    }

    public long getTotalExCount() {
        return totalExCount;
    }

    public void setTotalExCount(long totalExCount) {
        this.totalExCount = totalExCount;
    }

    public BigDecimal getTotalBorrowedAmount() {
        return totalBorrowedAmount;
    }

    public void setTotalBorrowedAmount(BigDecimal totalBorrowedAmount) {
        this.totalBorrowedAmount = totalBorrowedAmount;
    }

    public BigDecimal getTotalExtendedAmount() {
        return totalExtendedAmount;
    }

    public void setTotalExtendedAmount(BigDecimal totalExtendedAmount) {
        this.totalExtendedAmount = totalExtendedAmount;
    }

    public BigDecimal getTotalRepaidAmount() {
        return totalRepaidAmount;
    }

    public void setTotalRepaidAmount(BigDecimal totalRepaidAmount) {
        this.totalRepaidAmount = totalRepaidAmount;
    }

    public BigDecimal getTotalOverdueAmount() {
        return totalOverdueAmount;
    }

    public void setTotalOverdueAmount(BigDecimal totalOverdueAmount) {
        this.totalOverdueAmount = totalOverdueAmount;
    }
}
