package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   灯塔-胡萝卜获取米房信息
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthMiFangVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//state 1 为当前逾期 2 为历史逾期已还清
	private String state;
	//逾期天数
	private String days;
	//金额
	private String money;
	//应还日期
	private String overtime;

	private String result;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getOvertime() {
		return overtime;
	}

	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
