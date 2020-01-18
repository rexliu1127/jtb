package io.grx.modules.pay.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.pay.enums.PayScanStatus;


/**
 * 支付扫描记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-27 16:07:28
 */
public class PayScanRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String payId;
	//商家编号
	private String merchantNo;
	//系统订单号
	private String orderNo;
	private Long orderId;
	//订单类型
	private String payType;
	//订单用户ID
	private Long payUserId;
	//状态
	private PayScanStatus status = PayScanStatus.PROCESSING;
	//创建时间
	private Date createTime;

	/**
	 * 设置：
	 */
	public void setPayId(String payId) {
		this.payId = payId;
	}
	/**
	 * 获取：
	 */
	public String getPayId() {
		return payId;
	}
	/**
	 * 设置：商家编号
	 */
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	/**
	 * 获取：商家编号
	 */
	public String getMerchantNo() {
		return merchantNo;
	}
	/**
	 * 设置：系统订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：系统订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：订单类型
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}
	/**
	 * 获取：订单类型
	 */
	public String getPayType() {
		return payType;
	}
	/**
	 * 设置：订单用户ID
	 */
	public void setPayUserId(Long payUserId) {
		this.payUserId = payUserId;
	}
	/**
	 * 获取：订单用户ID
	 */
	public Long getPayUserId() {
		return payUserId;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(PayScanStatus status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public PayScanStatus getStatus() {
		return status;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }
}
