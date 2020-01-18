package io.grx.auth.entity.requestModel;

public class RequestBindBankModel {

    private String realName;
    private String idNo;
    private String bankCardNo;
    private String mobile;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
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
}
