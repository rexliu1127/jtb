package io.grx.modules.tx.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TxUserVO implements Serializable {
    private Long userId;
    private String name;
    private String mobile;
    private String idNo;
    private Long totalBorrowedAmount;
    private Long totalBorrowedCount;
    private BigDecimal totalRepayAmount;
    private Long lenderCount;
    private int currentOverdueCount;
    private int currentOverdueAmount;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(final String idNo) {
        this.idNo = idNo;
    }

    public Long getTotalBorrowedAmount() {
        return totalBorrowedAmount;
    }

    public void setTotalBorrowedAmount(final Long totalBorrowedAmount) {
        this.totalBorrowedAmount = totalBorrowedAmount;
    }

    public Long getTotalBorrowedCount() {
        return totalBorrowedCount;
    }

    public void setTotalBorrowedCount(final Long totalBorrowedCount) {
        this.totalBorrowedCount = totalBorrowedCount;
    }

    public BigDecimal getTotalRepayAmount() {
        return totalRepayAmount;
    }

    public void setTotalRepayAmount(final BigDecimal totalRepayAmount) {
        this.totalRepayAmount = totalRepayAmount;
    }

    public Long getLenderCount() {
        return lenderCount;
    }

    public void setLenderCount(final Long lenderCount) {
        this.lenderCount = lenderCount;
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
