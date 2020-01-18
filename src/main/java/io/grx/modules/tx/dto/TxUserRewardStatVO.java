package io.grx.modules.tx.dto;

import java.math.BigDecimal;

public class TxUserRewardStatVO {
    private String period;
    private Long userId;
    private String name;
    private Integer level2InviteeCount;
    private Integer level3InviteeCount;
    private Integer validInviteeCount;
    private BigDecimal reward;
    private BigDecimal balance;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel2InviteeCount() {
        return level2InviteeCount;
    }

    public void setLevel2InviteeCount(Integer level2InviteeCount) {
        this.level2InviteeCount = level2InviteeCount;
    }

    public Integer getLevel3InviteeCount() {
        return level3InviteeCount;
    }

    public void setLevel3InviteeCount(Integer level3InviteeCount) {
        this.level3InviteeCount = level3InviteeCount;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getValidInviteeCount() {
        return validInviteeCount;
    }

    public void setValidInviteeCount(Integer validInviteeCount) {
        this.validInviteeCount = validInviteeCount;
    }
}
