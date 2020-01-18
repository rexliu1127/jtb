package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--静默天数分析
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class SilentDaysInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//统计周期第一天
	private String startDay;
	//统计周期最后一天
	private String endDay;
	//最大连续静默天数
	private Integer maxInterval;
	//最大连续静默天数详情
	private List<String> maxDetail;
	private String maxDetailStr;
	//月均静默天数
	private Float monthlyAvgDays;

	public String getMaxDetailStr() {
		return maxDetailStr;
	}

	public void setMaxDetailStr(String maxDetailStr) {
		this.maxDetailStr = maxDetailStr;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public Integer getMaxInterval() {
		return maxInterval;
	}

	public void setMaxInterval(Integer maxInterval) {
		this.maxInterval = maxInterval;
	}

	public List<String> getMaxDetail() {
		return maxDetail;
	}

	public void setMaxDetail(List<String> maxDetail) {
		this.maxDetail = maxDetail;
	}

	public Float getMonthlyAvgDays() {
		return monthlyAvgDays;
	}

	public void setMonthlyAvgDays(Float monthlyAvgDays) {
		this.monthlyAvgDays = monthlyAvgDays;
	}
}
