package io.grx.modules.tx.dto;

import java.math.BigDecimal;

public class TxUserRewardSum {
    BigDecimal totalTxAmount;
    Long totalTxUserInvitee;
    Long validUserInvitee;

    public BigDecimal getTotalTxAmount() {
        return totalTxAmount;
    }

    public void setTotalTxAmount(BigDecimal totalTxAmount) {
        this.totalTxAmount = totalTxAmount;
    }

    public Long getTotalTxUserInvitee() {
        return totalTxUserInvitee;
    }

    public void setTotalTxUserInvitee(Long totalTxUserInvitee) {
        this.totalTxUserInvitee = totalTxUserInvitee;
    }

    public Long getValidUserInvitee() {
        return validUserInvitee;
    }

    public void setValidUserInvitee(Long validUserInvitee) {
        this.validUserInvitee = validUserInvitee;
    }
}
