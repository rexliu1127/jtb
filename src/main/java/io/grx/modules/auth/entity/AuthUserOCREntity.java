package io.grx.modules.auth.entity;

import java.io.Serializable;

public class AuthUserOCREntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private long userId;
    private String idUrl1;
    private String idUrl2;
    private String idUrl3;
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

    public String getIdUrl1() {
        return idUrl1;
    }

    public void setIdUrl1(String idUrl1) {
        this.idUrl1 = idUrl1;
    }

    public String getIdUrl2() {
        return idUrl2;
    }

    public void setIdUrl2(String idUrl2) {
        this.idUrl2 = idUrl2;
    }

    public String getIdUrl3() {
        return idUrl3;
    }

    public void setIdUrl3(String idUrl3) {
        this.idUrl3 = idUrl3;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AuthUserOCREntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", idUrl1='" + idUrl1 + '\'' +
                ", idUrl2='" + idUrl2 + '\'' +
                ", idUrl3='" + idUrl3 + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
