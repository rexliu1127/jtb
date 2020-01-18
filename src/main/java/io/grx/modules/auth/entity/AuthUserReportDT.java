package io.grx.modules.auth.entity;

import java.io.Serializable;

public class AuthUserReportDT implements Serializable {

    private long id;
    private long userId;
    private String respStr;
    private int status;
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRespStr() {
        return respStr;
    }

    public void setRespStr(String respStr) {
        this.respStr = respStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
