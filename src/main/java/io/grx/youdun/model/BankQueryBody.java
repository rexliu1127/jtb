package io.grx.youdun.model;

public class BankQueryBody {

    private String card_name;
    private String bank_name;
    private String req_type;
    private String message;
    private String card_type;
    private String org_name;
    private String org_code;
    private String status;
    private String ud_order_no;

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_code() {
        return org_code;
    }

    public void setOrg_code(String org_code) {
        this.org_code = org_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUd_order_no() {
        return ud_order_no;
    }

    public void setUd_order_no(String ud_order_no) {
        this.ud_order_no = ud_order_no;
    }
}
