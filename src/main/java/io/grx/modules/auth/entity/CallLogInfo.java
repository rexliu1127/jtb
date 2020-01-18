package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.math.BigDecimal;


/**
 *   运营商报告--通讯记录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class CallLogInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//号码
	private String phone;
	//互联网标识
	private String phoneInfo;
	//类别标签
	private String phoneLabel;
	//号码归属地
	private String phoneLocation;
    //主叫时长
    private BigDecimal callSeconds;
    //主叫次数
    private Integer callCnt;
	//通话时长
	private BigDecimal talkSeconds;
	//通话次数
	private Integer talkCnt;
	//被叫时长
	private BigDecimal calledSeconds;
	//被叫次数
	private Integer calledCnt;
	//发送短信数
	private Integer sendCnt;
	//接收短信数
	private Integer receiveCnt;
	//和通讯录匹配姓名
	private String personName;
	//是不是紧急联系人
	private String jinjiPerson;

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

    public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getJinjiPerson() {
		return jinjiPerson;
	}

	public void setJinjiPerson(String jinjiPerson) {
		this.jinjiPerson = jinjiPerson;
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

	public String getPhoneLocation() {
		return phoneLocation;
	}

	public void setPhoneLocation(String phoneLocation) {
		this.phoneLocation = phoneLocation;
	}

	public BigDecimal getCallSeconds() {
		return callSeconds;
	}

	public void setCallSeconds(BigDecimal callSeconds) {
		this.callSeconds = callSeconds;
	}

	public Integer getCallCnt() {
		return callCnt;
	}

	public void setCallCnt(Integer callCnt) {
		this.callCnt = callCnt;
	}

	public BigDecimal getCalledSeconds() {
		return calledSeconds;
	}

	public void setCalledSeconds(BigDecimal calledSeconds) {
		this.calledSeconds = calledSeconds;
	}

	public Integer getCalledCnt() {
		return calledCnt;
	}

	public void setCalledCnt(Integer calledCnt) {
		this.calledCnt = calledCnt;
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
