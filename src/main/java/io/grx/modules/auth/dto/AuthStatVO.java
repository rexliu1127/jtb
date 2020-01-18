package io.grx.modules.auth.dto;

import java.io.Serializable;

public class AuthStatVO implements Serializable {
    private long totalPendingCount;
    private long totalProcessingCount;
    private long totalApprovedCount;
    private long totalRejectedCount;
    private long totalCompletedCount;
    private long totalOverdueCount;
    private long totalLostCount;
    private long totalCanceledCount;
    private long totalCount;

    public long getTotalPendingCount() {
        return totalPendingCount;
    }

    public void setTotalPendingCount(final long totalPendingCount) {
        this.totalPendingCount = totalPendingCount;
    }

    public long getTotalProcessingCount() {
        return totalProcessingCount;
    }

    public void setTotalProcessingCount(final long totalProcessingCount) {
        this.totalProcessingCount = totalProcessingCount;
    }

    public long getTotalApprovedCount() {
        return totalApprovedCount;
    }

    public void setTotalApprovedCount(final long totalApprovedCount) {
        this.totalApprovedCount = totalApprovedCount;
    }

    public long getTotalRejectedCount() {
        return totalRejectedCount;
    }

    public void setTotalRejectedCount(final long totalRejectedCount) {
        this.totalRejectedCount = totalRejectedCount;
    }

    public long getTotalCompletedCount() {
        return totalCompletedCount;
    }

    public void setTotalCompletedCount(final long totalCompletedCount) {
        this.totalCompletedCount = totalCompletedCount;
    }

    public long getTotalOverdueCount() {
        return totalOverdueCount;
    }

    public void setTotalOverdueCount(final long totalOverdueCount) {
        this.totalOverdueCount = totalOverdueCount;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalLostCount() {
        return totalLostCount;
    }

    public void setTotalLostCount(final long totalLostCount) {
        this.totalLostCount = totalLostCount;
    }

    public long getTotalCanceledCount() {
        return totalCanceledCount;
    }

    public void setTotalCanceledCount(final long totalCanceledCount) {
        this.totalCanceledCount = totalCanceledCount;
    }
}
