package io.grx.modules.auth.dto;

import java.io.Serializable;

import io.grx.modules.auth.enums.RequestStatus;

public class AuthRequestVO implements Serializable {
    private Long requestId;
    private String merchantNo;
    private String merchantName;
    private String requestUuid;
    private String channelName;
    private String name;
    private String headImageUrl;
    private String mobile;
    private String auditorUserName;
    private String auditorName;
    private String processorUserName;
    private String processorName;
    private String assigneeUserName;
    private String assigneeName;
    private String updateTime;
    private String createTime;
    private String deptName;
    private RequestStatus status;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(final Long requestId) {
        this.requestId = requestId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }

    public String getAuditorUserName() {
        return auditorUserName;
    }

    public void setAuditorUserName(final String auditorUserName) {
        this.auditorUserName = auditorUserName;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(final String auditorName) {
        this.auditorName = auditorName;
    }

    public String getProcessorUserName() {
        return processorUserName;
    }

    public void setProcessorUserName(final String processorUserName) {
        this.processorUserName = processorUserName;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(final String processorName) {
        this.processorName = processorName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final String createTime) {
        this.createTime = createTime;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(final RequestStatus status) {
        this.status = status;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public String getAssigneeUserName() {
        return assigneeUserName;
    }

    public void setAssigneeUserName(String assigneeUserName) {
        this.assigneeUserName = assigneeUserName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(final String deptName) {
        this.deptName = deptName;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
