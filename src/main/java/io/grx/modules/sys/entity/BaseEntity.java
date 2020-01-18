package io.grx.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.common.utils.ShiroUtils;

public class BaseEntity implements Serializable {
    //商家编号
    private String merchantNo = ShiroUtils.getMerchantNo();
    //创建者ID
    private Long createUserId = ShiroUtils.getUserId();
    //创建时间
    private Date createTime = new Date();

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(final String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(final Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
}
