package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 *   运营商报告--特殊号码
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class SpecialCateInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//类别标签
	private String cate;
	//通话时长
	private BigDecimal talkSeconds;
	//通话次数
	private Integer talkCnt;
	//主叫时长
	private BigDecimal callSeconds;
	//主叫次数
	private Integer callCnt;
	//被叫时长
	private BigDecimal calledSeconds;
	//被叫次数
	private Integer calledCnt;
	//短信总数
	private Integer msgCnt;
	//发送短信数
	private Integer sendCnt;
	//接收短信数
	private Integer receiveCnt;
	//电话详情
	private List<SpecialCateSonInfo> specialCateSonInfos;

	public List<SpecialCateSonInfo> getSpecialCateSonInfos() {
		return specialCateSonInfos;
	}

	public void setSpecialCateSonInfos(List<SpecialCateSonInfo> specialCateSonInfos) {
		this.specialCateSonInfos = specialCateSonInfos;
	}

	public String getCate() {
		return cate;
	}

	public void setCate(String cate) {
		this.cate = cate;
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
