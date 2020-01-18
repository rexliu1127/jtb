package io.grx.modules.tx.dto;

public class TxUserInviteVO {
    private String name;
    private String createDate;
    private Integer txCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getTxCount() {
        return txCount;
    }

    public void setTxCount(Integer txCount) {
        this.txCount = txCount;
    }
}
