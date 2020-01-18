package io.grx.modules.tx.dto;

import java.math.BigDecimal;

public class TxUserRewardVO {
    private int level;
    private String inviteeName;
    private String createDate;
    private BigDecimal amount;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getInviteeName() {
        return inviteeName;
    }

    public void setInviteeName(String inviteeName) {
        this.inviteeName = inviteeName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
