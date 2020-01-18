package io.grx.modules.tx.dto;

public class TxStatSum {
    private int newUserCount;
    private int newUserTxCount;
    private int oldUserTxCount;
    private int totalTxCount;
    private int totalExCount;

    public int getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(final int newUserCount) {
        this.newUserCount = newUserCount;
    }

    public int getNewUserTxCount() {
        return newUserTxCount;
    }

    public void setNewUserTxCount(final int newUserTxCount) {
        this.newUserTxCount = newUserTxCount;
    }

    public int getOldUserTxCount() {
        return oldUserTxCount;
    }

    public void setOldUserTxCount(final int oldUserTxCount) {
        this.oldUserTxCount = oldUserTxCount;
    }

    public int getTotalTxCount() {
        return totalTxCount;
    }

    public void setTotalTxCount(final int totalTxCount) {
        this.totalTxCount = totalTxCount;
    }

    public int getTotalExCount() {
        return totalExCount;
    }

    public void setTotalExCount(final int totalExCount) {
        this.totalExCount = totalExCount;
    }
}
