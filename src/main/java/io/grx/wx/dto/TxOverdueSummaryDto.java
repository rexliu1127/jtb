package io.grx.wx.dto;

import java.io.Serializable;

public class TxOverdueSummaryDto implements Serializable {
    private long amount;
    private long count;
    private long moreThanSevenDayAmount;
    private long moreThanSevenDayCount;
    private long currentAmount;
    private long currentCount;

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

    public long getMoreThanSevenDayCount() {
        return moreThanSevenDayCount;
    }

    public void setMoreThanSevenDayCount(final long moreThanSevenDayCount) {
        this.moreThanSevenDayCount = moreThanSevenDayCount;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(final long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public long getMoreThanSevenDayAmount() {
        return moreThanSevenDayAmount;
    }

    public void setMoreThanSevenDayAmount(final long moreThanSevenDayAmount) {
        this.moreThanSevenDayAmount = moreThanSevenDayAmount;
    }

    public long getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(final long currentCount) {
        this.currentCount = currentCount;
    }
}
