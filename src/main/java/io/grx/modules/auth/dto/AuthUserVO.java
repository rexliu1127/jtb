package io.grx.modules.auth.dto;

import io.grx.modules.auth.enums.RequestStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthUserVO implements Serializable {
    //
    private Long userId;
    //商家编号
    private String merchantNo;
    //菜单名称
    private String mobile;
    private String channelName;
    //用户名称
    private String name;
    private String idNo;

    private String sex;
    private int age;
    private int zhimaPoint;

    private String city;

    private Long latestRequestId;

    private RequestStatus latestRequestStatus;

    private List<String> basicAuthNames = new ArrayList<>();
    private List<String> loanAuthNames = new ArrayList<>();

    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getZhimaPoint() {
        return zhimaPoint;
    }

    public void setZhimaPoint(int zhimaPoint) {
        this.zhimaPoint = zhimaPoint;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getBasicAuthNames() {
        return basicAuthNames;
    }

    public void setBasicAuthNames(List<String> basicAuthNames) {
        this.basicAuthNames = basicAuthNames;
    }

    public List<String> getLoanAuthNames() {
        return loanAuthNames;
    }

    public void setLoanAuthNames(List<String> loanAuthNames) {
        this.loanAuthNames = loanAuthNames;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getLatestRequestId() {
        return latestRequestId;
    }

    public void setLatestRequestId(Long latestRequestId) {
        this.latestRequestId = latestRequestId;
    }

    public RequestStatus getLatestRequestStatus() {
        return latestRequestStatus;
    }

    public void setLatestRequestStatus(RequestStatus latestRequestStatus) {
        this.latestRequestStatus = latestRequestStatus;
    }
}
