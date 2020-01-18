package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--夜间活动情况
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class NightActivitiesInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//月均夜间通话时长
	private Integer monthlyAvgSeconds;
	//月均夜间通话时长占比
	private Float monthlyAvgSecondsRatio;
	//月均夜间短信数
	private Float monthlyAvgMsg;
	//月均夜间短信数占比
	private Float monthlyAvgMsgRatio;
	//夜间通话时长占比
	private Float nightTalkSecondsRatio;
	//夜间短信占比
	private Float nightMsgRatio;

	public Integer getMonthlyAvgSeconds() {
		return monthlyAvgSeconds;
	}

	public void setMonthlyAvgSeconds(Integer monthlyAvgSeconds) {
		this.monthlyAvgSeconds = monthlyAvgSeconds;
	}

	public Float getMonthlyAvgSecondsRatio() {
		return monthlyAvgSecondsRatio;
	}

	public void setMonthlyAvgSecondsRatio(Float monthlyAvgSecondsRatio) {
		this.monthlyAvgSecondsRatio = monthlyAvgSecondsRatio;
	}

	public Float getMonthlyAvgMsg() {
		return monthlyAvgMsg;
	}

	public void setMonthlyAvgMsg(Float monthlyAvgMsg) {
		this.monthlyAvgMsg = monthlyAvgMsg;
	}

	public Float getMonthlyAvgMsgRatio() {
		return monthlyAvgMsgRatio;
	}

	public void setMonthlyAvgMsgRatio(Float monthlyAvgMsgRatio) {
		this.monthlyAvgMsgRatio = monthlyAvgMsgRatio;
	}

	public Float getNightTalkSecondsRatio() {
		return nightTalkSecondsRatio;
	}

	public void setNightTalkSecondsRatio(Float nightTalkSecondsRatio) {
		this.nightTalkSecondsRatio = nightTalkSecondsRatio;
	}

	public Float getNightMsgRatio() {
		return nightMsgRatio;
	}

	public void setNightMsgRatio(Float nightMsgRatio) {
		this.nightMsgRatio = nightMsgRatio;
	}
}
