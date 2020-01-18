package io.grx.auth.entity.requestModel;

public class RequestLoginModel {


    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 验证码
     */
    private String verifyCode;

    private String channel;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
