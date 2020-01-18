package io.grx.modules.auth.dto;

import java.util.Date;

public class AuthRequestIntegrationDTO {

    private static final long serialVersionUID = 1L;

    //
    private Long requestId;
    private String requestUuid;
    //商家编号
    private String merchantNo;
    //申请用户ID
    private Long userId;
    //处理系统用户ID
    private Long processorId;
    //客服系统用户ID
    private Long assigneeId;
    //渠道ID
    private Long channelId;
    //用户名称
    private String name;
    private String mobile;
    //身份证号码
    private String idNo;
    //身份证号码
    private String qqNo;
    //手机服务密码
    private String mobilePass;
    //联系人1类型
    private String contact1TypeName;
    //联系人1名字
    private String contact1Name;
    //联系人1电话
    private String contact1Mobile;
    //联系人2类型
    private String contact2TypeName;
    //联系人2名字
    private String contact2Name;
    //联系人2电话
    private String contact2Mobile;
    //身份证正面url
    private String idUrl1;
    //身份证反面url
    private String idUrl2;
    //手持身份证url
    private String idUrl3;
    //状态
    private String statusLabel;
    //认证token
    private String verifyToken;
    // 认证 status
    private String verifyStatusLabel;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    // 身份证是否跟运营商匹配
    private boolean idNoMatched;
    // 名字是否跟运营商匹配
    private boolean nameMatched;

    private Integer contact1CallCount;

    private Integer contact2CallCount;

    private String isMobileReliable;

    private String isMobileAgeOver3Month;

    private String accountStatus;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(final Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(final String requestUuid) {
        this.requestUuid = requestUuid;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(final String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getProcessorId() {
        return processorId;
    }

    public void setProcessorId(final Long processorId) {
        this.processorId = processorId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(final Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(final Long channelId) {
        this.channelId = channelId;
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

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(final String idNo) {
        this.idNo = idNo;
    }

    public String getQqNo() {
        return qqNo;
    }

    public void setQqNo(final String qqNo) {
        this.qqNo = qqNo;
    }

    public String getMobilePass() {
        return mobilePass;
    }

    public void setMobilePass(final String mobilePass) {
        this.mobilePass = mobilePass;
    }

    public String getContact1Name() {
        return contact1Name;
    }

    public void setContact1Name(final String contact1Name) {
        this.contact1Name = contact1Name;
    }

    public String getContact1Mobile() {
        return contact1Mobile;
    }

    public void setContact1Mobile(final String contact1Mobile) {
        this.contact1Mobile = contact1Mobile;
    }

    public String getContact2Name() {
        return contact2Name;
    }

    public void setContact2Name(final String contact2Name) {
        this.contact2Name = contact2Name;
    }

    public String getContact2Mobile() {
        return contact2Mobile;
    }

    public void setContact2Mobile(final String contact2Mobile) {
        this.contact2Mobile = contact2Mobile;
    }

    public String getIdUrl1() {
        return idUrl1;
    }

    public void setIdUrl1(final String idUrl1) {
        this.idUrl1 = idUrl1;
    }

    public String getIdUrl2() {
        return idUrl2;
    }

    public void setIdUrl2(final String idUrl2) {
        this.idUrl2 = idUrl2;
    }

    public String getIdUrl3() {
        return idUrl3;
    }

    public void setIdUrl3(final String idUrl3) {
        this.idUrl3 = idUrl3;
    }

    public String getVerifyToken() {
        return verifyToken;
    }

    public void setVerifyToken(final String verifyToken) {
        this.verifyToken = verifyToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(final Date updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isIdNoMatched() {
        return idNoMatched;
    }

    public void setIdNoMatched(final boolean idNoMatched) {
        this.idNoMatched = idNoMatched;
    }

    public boolean isNameMatched() {
        return nameMatched;
    }

    public void setNameMatched(final boolean nameMatched) {
        this.nameMatched = nameMatched;
    }

    public Integer getContact1CallCount() {
        return contact1CallCount;
    }

    public void setContact1CallCount(final Integer contact1CallCount) {
        this.contact1CallCount = contact1CallCount;
    }

    public Integer getContact2CallCount() {
        return contact2CallCount;
    }

    public void setContact2CallCount(final Integer contact2CallCount) {
        this.contact2CallCount = contact2CallCount;
    }

    public String getContact1TypeName() {
        return contact1TypeName;
    }

    public void setContact1TypeName(final String contact1TypeName) {
        this.contact1TypeName = contact1TypeName;
    }

    public String getContact2TypeName() {
        return contact2TypeName;
    }

    public void setContact2TypeName(final String contact2TypeName) {
        this.contact2TypeName = contact2TypeName;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(final String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getVerifyStatusLabel() {
        return verifyStatusLabel;
    }

    public void setVerifyStatusLabel(final String verifyStatusLabel) {
        this.verifyStatusLabel = verifyStatusLabel;
    }

    public String getIsMobileReliable() {
        return isMobileReliable;
    }

    public void setIsMobileReliable(final String isMobileReliable) {
        this.isMobileReliable = isMobileReliable;
    }

    public String getIsMobileAgeOver3Month() {
        return isMobileAgeOver3Month;
    }

    public void setIsMobileAgeOver3Month(final String isMobileAgeOver3Month) {
        this.isMobileAgeOver3Month = isMobileAgeOver3Month;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(final String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
