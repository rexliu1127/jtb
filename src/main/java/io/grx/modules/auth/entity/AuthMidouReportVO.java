package io.grx.modules.auth.entity;



import io.grx.modules.tx.entity.TxBaseEntity;

import java.io.Serializable;
import java.util.List;


/**
 *   米兜报告
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthMidouReportVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//借款次数
	private Integer appCounts;

	//最长逾期天数
	private Integer  overdueLongDay;

	//逾期次数
	private Integer overdueCounts;

	//最近一次逾期天数
	private Integer lastOverdueDay;

	//最近一次是否逾期
	private boolean ifOverdue;

	//最近一次逾期后正常还款次数
	private Integer repayCounts;

	//历史进件列表
	private List<TxBaseEntity>  baseInfoList;

	public Integer getAppCounts() {
		return appCounts;
	}

	public void setAppCounts(Integer appCounts) {
		this.appCounts = appCounts;
	}

	public Integer getOverdueLongDay() {
		return overdueLongDay;
	}

	public void setOverdueLongDay(Integer overdueLongDay) {
		this.overdueLongDay = overdueLongDay;
	}

	public Integer getOverdueCounts() {
		return overdueCounts;
	}

	public void setOverdueCounts(Integer overdueCounts) {
		this.overdueCounts = overdueCounts;
	}

	public Integer getLastOverdueDay() {
		return lastOverdueDay;
	}

	public void setLastOverdueDay(Integer lastOverdueDay) {
		this.lastOverdueDay = lastOverdueDay;
	}

	public boolean isIfOverdue() {
		return ifOverdue;
	}

	public void setIfOverdue(boolean ifOverdue) {
		this.ifOverdue = ifOverdue;
	}

	public Integer getRepayCounts() {
		return repayCounts;
	}

	public void setRepayCounts(Integer repayCounts) {
		this.repayCounts = repayCounts;
	}

	public List<TxBaseEntity> getBaseInfoList() {
		return baseInfoList;
	}

	public void setBaseInfoList(List<TxBaseEntity> baseInfoList) {
		this.baseInfoList = baseInfoList;
	}
}
