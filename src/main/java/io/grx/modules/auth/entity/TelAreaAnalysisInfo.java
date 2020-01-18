package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.math.BigDecimal;


/**
 *   运营商报告--通话地区分析
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class TelAreaAnalysisInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//通话地点
	private String area;
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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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
}
