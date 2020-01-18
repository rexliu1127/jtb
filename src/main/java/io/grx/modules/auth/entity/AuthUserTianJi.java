package io.grx.modules.auth.entity;

import java.io.Serializable;

public class AuthUserTianJi implements Serializable {

    private long id;
    private long userId;
    private String outUniqueId;
    private String searchId;
    private String dataStr;
    private String detailHtml;
    private String detailJson;
    private int dataStatus;
    private int detailStatus;
    private String createTime;
    private String updateTime;

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

    public String getOutUniqueId() {
        return outUniqueId;
    }

    public void setOutUniqueId(String outUniqueId) {
        this.outUniqueId = outUniqueId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getDetailHtml() {
        return detailHtml;
    }

    public void setDetailHtml(String detailHtml) {
        this.detailHtml = detailHtml;
    }

    public String getDetailJson() {
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(int detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
