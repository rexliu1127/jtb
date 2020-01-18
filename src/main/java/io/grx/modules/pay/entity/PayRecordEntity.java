package io.grx.modules.pay.entity;

import java.io.Serializable;
import java.util.Date;

import io.grx.modules.pay.enums.PayStatus;


/**
 * 支付记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-02-08 01:07:58
 */
public class PayRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//商家编号
	private String merchantNo;
	//借条ID
	private Long txId;
	//付款用户ID
	private Long payUserId;
	//借条ID
	private Long extensionId;
	//系统订单号
	private String orderNo;
	//支付金额
	private Integer amount;
	//支付人openId
	private String accountId;
	//收银宝平台的交易流水号
	private String trxId;
	//收银宝平台的交易流水号
	private String outTrxId;
	//例如微信,支付宝平台的交易单号
	private String chnlTrxId;
	//下单状态
	private String requestTrxStatus;
	//支付状态
	private String payTrxStatus;
	//下单时间
	private String finTime;
	//完成时间
	private String payTime;
	//错误信息
	private String errorMessage;
	//创建时间
	private Date createTime;

	private PayStatus status = PayStatus.NEW;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
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
	 * 设置：借条ID
	 */
	public void setTxId(Long txId) {
		this.txId = txId;
	}
	/**
	 * 获取：借条ID
	 */
	public Long getTxId() {
		return txId;
	}
	/**
	 * 设置：借条ID
	 */
	public void setExtensionId(Long extensionId) {
		this.extensionId = extensionId;
	}
	/**
	 * 获取：借条ID
	 */
	public Long getExtensionId() {
		return extensionId;
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
	 * 设置：支付金额
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * 获取：支付金额
	 */
	public Integer getAmount() {
		return amount;
	}
	/**
	 * 设置：支付人openId
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	/**
	 * 获取：支付人openId
	 */
	public String getAccountId() {
		return accountId;
	}
	/**
	 * 设置：收银宝平台的交易流水号
	 */
	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}
	/**
	 * 获取：收银宝平台的交易流水号
	 */
	public String getTrxId() {
		return trxId;
	}
	/**
	 * 设置：收银宝平台的交易流水号
	 */
	public void setOutTrxId(String outTrxId) {
		this.outTrxId = outTrxId;
	}
	/**
	 * 获取：收银宝平台的交易流水号
	 */
	public String getOutTrxId() {
		return outTrxId;
	}
	/**
	 * 设置：例如微信,支付宝平台的交易单号
	 */
	public void setChnlTrxId(String chnlTrxId) {
		this.chnlTrxId = chnlTrxId;
	}
	/**
	 * 获取：例如微信,支付宝平台的交易单号
	 */
	public String getChnlTrxId() {
		return chnlTrxId;
	}
	/**
	 * 设置：下单状态
	 */
	public void setRequestTrxStatus(String requestTrxStatus) {
		this.requestTrxStatus = requestTrxStatus;
	}
	/**
	 * 获取：下单状态
	 */
	public String getRequestTrxStatus() {
		return requestTrxStatus;
	}
	/**
	 * 设置：支付状态
	 */
	public void setPayTrxStatus(String payTrxStatus) {
		this.payTrxStatus = payTrxStatus;
	}
	/**
	 * 获取：支付状态
	 */
	public String getPayTrxStatus() {
		return payTrxStatus;
	}
	/**
	 * 设置：下单时间
	 */
	public void setFinTime(String finTime) {
		this.finTime = finTime;
	}
	/**
	 * 获取：下单时间
	 */
	public String getFinTime() {
		return finTime;
	}
	/**
	 * 设置：完成时间
	 */
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	/**
	 * 获取：完成时间
	 */
	public String getPayTime() {
		return payTime;
	}
	/**
	 * 设置：错误信息
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * 获取：错误信息
	 */
	public String getErrorMessage() {
		return errorMessage;
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

	public Long getPayUserId() {
		return payUserId;
	}

	public void setPayUserId(final Long payUserId) {
		this.payUserId = payUserId;
	}

    public PayStatus getStatus() {
        return status;
    }

    public void setStatus(final PayStatus status) {
        this.status = status;
    }
}
