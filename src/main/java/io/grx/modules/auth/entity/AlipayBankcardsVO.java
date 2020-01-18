package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   全知-电商报告(支付宝绑定银行卡)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AlipayBankcardsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//银行名称
	private String bankName;
	//	银行卡号、
	private String cardNum;
	//卡类型
	private String cardType;
	//卡户名
	private String cardName;
	//绑定手机号
	private String phoneNum;
	//开卡时间
	private String openDate;
	//开通快捷支付
	private String quickPayment;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getQuickPayment() {
		return quickPayment;
	}

	public void setQuickPayment(String quickPayment) {
		this.quickPayment = quickPayment;
	}
}
