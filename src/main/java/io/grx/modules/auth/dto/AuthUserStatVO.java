package io.grx.modules.auth.dto;

import java.io.Serializable;

public class AuthUserStatVO implements Serializable {
    private Long userId;
    private String name;
    private String username;
    private int pendingCount;
    private int processingCount;
    private int approvedCount;
    private int rejectedCount;
    private int completedCount;
    private int overdueCount;
    private int lostCount;
    private int canceledCount;
    private int totalCount;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public int getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(final int pendingCount) {
        this.pendingCount = pendingCount;
    }

    public int getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(final int processingCount) {
        this.processingCount = processingCount;
    }

    public int getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(final int approvedCount) {
        this.approvedCount = approvedCount;
    }

    public int getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(final int rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(final int completedCount) {
        this.completedCount = completedCount;
    }

    public int getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(final int overdueCount) {
        this.overdueCount = overdueCount;
    }

    public int getLostCount() {
        return lostCount;
    }

    public void setLostCount(final int lostCount) {
        this.lostCount = lostCount;
    }

    public int getCanceledCount() {
        return canceledCount;
    }

    public void setCanceledCount(final int canceledCount) {
        this.canceledCount = canceledCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }
}
