package io.grx.youdun.model;

public class BankQueryHeader {

    private String resp_time;
    private String ret_msg;
    private String version;
    private String ret_code;
    private String req_time;

    public String getResp_time() {
        return resp_time;
    }

    public void setResp_time(String resp_time) {
        this.resp_time = resp_time;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getReq_time() {
        return req_time;
    }

    public void setReq_time(String req_time) {
        this.req_time = req_time;
    }
}
