package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--通话记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthTjCallLogVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//业务类型
	private String business_name;

	//通话时间
	private String call_time;

	//呼叫类型
	private String call_type;

	//费用
	private String fee;
	//套餐优惠
	private String special_offer;
	//通话地点
	private String trade_addr;
	//通话时长
	private String trade_time;
	//通信类型
	private String trade_type;
	//对方号码
	private String receive_phone;

	//姓名
	private String contactName;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getCall_time() {
		return call_time;
	}

	public void setCall_time(String call_time) {
		this.call_time = call_time;
	}

	public String getCall_type() {
		return call_type;
	}

	public void setCall_type(String call_type) {
		this.call_type = call_type;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getSpecial_offer() {
		return special_offer;
	}

	public void setSpecial_offer(String special_offer) {
		this.special_offer = special_offer;
	}

	public String getTrade_addr() {
		return trade_addr;
	}

	public void setTrade_addr(String trade_addr) {
		this.trade_addr = trade_addr;
	}

	public String getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getReceive_phone() {
		return receive_phone;
	}

	public void setReceive_phone(String receive_phone) {
		this.receive_phone = receive_phone;
	}
}
