package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   全知-电商报告(商品订单信息)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class TrandeInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//商品名称
	private String productName;
	//交易状态
	private String tradeStatus;
	//订单金额
	private String amount;
	//成交时间
	private String tradeTime;
	//收货人姓名
	private String receiver;
	//收货人手机号
	private String  receivePhone;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}
}
