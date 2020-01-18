package io.grx.modules.auth.dto;

import java.io.Serializable;

public class AuthSummary implements Serializable {

    private long totalCount;
    private long processingCount;
    private long approvedCount;
    private long rejectedCount;
    private long completedCount;
    private long secondAuthCount;
    private long overdueCount;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(long processingCount) {
        this.processingCount = processingCount;
    }

    public long getApprovedCount() {
        return approvedCount;
    }

    public void setApprovedCount(long approvedCount) {
        this.approvedCount = approvedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(long overdueCount) {
        this.overdueCount = overdueCount;
    }

    public long getSecondAuthCount() {
        return secondAuthCount;
    }

    public void setSecondAuthCount(long secondAuthCount) {
        this.secondAuthCount = secondAuthCount;
    }
}
