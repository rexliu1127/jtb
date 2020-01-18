package io.grx.modules.auth.entity;

import java.io.Serializable;

public class AuthUserBankCard implements Serializable {

    private int id;
    private long userId;
    private String bankCardNo;
    private String mobile;
    private String bankCardName;
    private String bankOrgName;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankCardName() {
        return bankCardName;
    }

    public void setBankCardName(String bankCardName) {
        this.bankCardName = bankCardName;
    }

    public String getBankOrgName() {
        return bankOrgName;
    }

    public void setBankOrgName(String bankOrgName) {
        this.bankOrgName = bankOrgName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
