package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.math.BigDecimal;


/**
 *   运营商报告--紧急联系人分析
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class EmergencyAnalysisInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//联系人号码
	private String phone;
	//互联网标识
	private String phoneInfo;
	//类别标签
	private String phoneLabel;
	//联系人姓名
	private String name;
	//首次联系时间
	private String firstContactDate;
	//最后联系时间
	private String lastContactDate;
	//通话时长
	private BigDecimal talkSeconds;
	//通话次数
	private Integer talkCnt;
	//主叫时长
	private Integer callSeconds;
	//主叫次数
	private Integer callCnt;
	//被叫时长
	private Integer calledSeconds;
	//被叫次数
	private Integer calledCnt;
	//短信总数
	private Integer msgCnt;
	//发送短信数
	private Integer sendCnt;
	//接收短信数
	private Integer receiveCnt;
	//最后联系日期
	private String lastContactDateDate;
	//最后联系时间
	private String lastContactDateTime;

	public String getLastContactDateDate() {
		return lastContactDateDate;
	}

	public void setLastContactDateDate(String lastContactDateDate) {
		this.lastContactDateDate = lastContactDateDate;
	}

	public String getLastContactDateTime() {
		return lastContactDateTime;
	}

	public void setLastContactDateTime(String lastContactDateTime) {
		this.lastContactDateTime = lastContactDateTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneInfo() {
		return phoneInfo;
	}

	public void setPhoneInfo(String phoneInfo) {
		this.phoneInfo = phoneInfo;
	}

	public String getPhoneLabel() {
		return phoneLabel;
	}

	public void setPhoneLabel(String phoneLabel) {
		this.phoneLabel = phoneLabel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstContactDate() {
		return firstContactDate;
	}

	public void setFirstContactDate(String firstContactDate) {
		this.firstContactDate = firstContactDate;
	}

	public String getLastContactDate() {
		return lastContactDate;
	}

	public void setLastContactDate(String lastContactDate) {
		this.lastContactDate = lastContactDate;
	}

	public BigDecimal getTalkSeconds() {
		return talkSeconds;
	}

	public void setTalkSeconds(BigDecimal talkSeconds) {
		this.talkSeconds = talkSeconds;
	}

	public Integer getTalkCnt() {
		return talkCnt;
	}

	public void setTalkCnt(Integer talkCnt) {
		this.talkCnt = talkCnt;
	}

	public Integer getCallSeconds() {
		return callSeconds;
	}

	public void setCallSeconds(Integer callSeconds) {
		this.callSeconds = callSeconds;
	}

	public Integer getCallCnt() {
		return callCnt;
	}

	public void setCallCnt(Integer callCnt) {
		this.callCnt = callCnt;
	}

	public Integer getCalledSeconds() {
		return calledSeconds;
	}

	public void setCalledSeconds(Integer calledSeconds) {
		this.calledSeconds = calledSeconds;
	}

	public Integer getCalledCnt() {
		return calledCnt;
	}

	public void setCalledCnt(Integer calledCnt) {
		this.calledCnt = calledCnt;
	}

	public Integer getMsgCnt() {
		return msgCnt;
	}

	public void setMsgCnt(Integer msgCnt) {
		this.msgCnt = msgCnt;
	}

	public Integer getSendCnt() {
		return sendCnt;
	}

	public void setSendCnt(Integer sendCnt) {
		this.sendCnt = sendCnt;
	}

	public Integer getReceiveCnt() {
		return receiveCnt;
	}

	public void setReceiveCnt(Integer receiveCnt) {
		this.receiveCnt = receiveCnt;
	}
}
