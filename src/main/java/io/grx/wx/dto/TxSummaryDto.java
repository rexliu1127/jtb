package io.grx.wx.dto;

import java.io.Serializable;

public class TxSummaryDto implements Serializable {
    private long amount;
    private long count;
    private long userCount;
    private long inDayTransactionCount;
    private long currentAmount;

    public long getAmount() {
        return amount;
    }

    public void setAmount(final long amount) {
        this.amount = amount;
    }

    public long getCount() {
        return count;
    }

    public void setCount(final long count) {
        this.count = count;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(final long userCount) {
        this.userCount = userCount;
    }

    public long getInDayTransactionCount() {
        return inDayTransactionCount;
    }

    public void setInDayTransactionCount(final long inDayTransactionCount) {
        this.inDayTransactionCount = inDayTransactionCount;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(final long currentAmount) {
        this.currentAmount = currentAmount;
    }
}
