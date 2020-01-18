package io.grx.youdun.model;

public class BankQueryModel {

    private String sign;
    private BankQueryHeader header;
    private BankQueryBody body;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public BankQueryHeader getHeader() {
        return header;
    }

    public void setHeader(BankQueryHeader header) {
        this.header = header;
    }

    public BankQueryBody getBody() {
        return body;
    }

    public void setBody(BankQueryBody body) {
        this.body = body;
    }
}
