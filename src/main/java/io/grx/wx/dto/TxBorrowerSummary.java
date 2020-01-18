package io.grx.wx.dto;

import java.io.Serializable;

public class TxBorrowerSummary implements Serializable {
    private String name;
    private String idNo;
    private long totalBorrowedAmount;
    private long totalRepaidAmount;
    private long totalBorrowedCount;
    private int totalLenderCount;
    private int currentOverdueCount;
    private int currentOverdueAmount;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(final String idNo) {
        this.idNo = idNo;
    }

    public long getTotalBorrowedAmount() {
        return totalBorrowedAmount;
    }

    public void setTotalBorrowedAmount(final long totalBorrowedAmount) {
        this.totalBorrowedAmount = totalBorrowedAmount;
    }

    public long getTotalRepaidAmount() {
        return totalRepaidAmount;
    }

    public void setTotalRepaidAmount(final long totalRepaidAmount) {
        this.totalRepaidAmount = totalRepaidAmount;
    }

    public long getTotalBorrowedCount() {
        return totalBorrowedCount;
    }

    public void setTotalBorrowedCount(final long totalBorrowedCount) {
        this.totalBorrowedCount = totalBorrowedCount;
    }

    public int getTotalLenderCount() {
        return totalLenderCount;
    }

    public void setTotalLenderCount(final int totalLenderCount) {
        this.totalLenderCount = totalLenderCount;
    }

    public int getCurrentOverdueCount() {
        return currentOverdueCount;
    }

    public void setCurrentOverdueCount(final int currentOverdueCount) {
        this.currentOverdueCount = currentOverdueCount;
    }

    public int getCurrentOverdueAmount() {
        return currentOverdueAmount;
    }

    public void setCurrentOverdueAmount(final int currentOverdueAmount) {
        this.currentOverdueAmount = currentOverdueAmount;
    }
}
