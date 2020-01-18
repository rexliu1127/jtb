package io.grx.modules.tx.entity;

import java.io.Serializable;


/**
 * 每日借条统计
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-01-27 22:50:18
 */
public class TxDailyReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//报告日期
	private String reportDate;
	//新增用户数
	private Integer newUserCount;
	//新增用户借条数
	private Integer newUserTxBount;
	//旧用户借条数
	private Integer oldUserTxBount;
	//总借条数
	private Integer totalTxBount;
	// 总展期次数
	private Integer totalExCount;

	/**
	 * 设置：报告日期
	 */
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * 获取：报告日期
	 */
	public String getReportDate() {
		return reportDate;
	}
	/**
	 * 设置：新增用户数
	 */
	public void setNewUserCount(Integer newUserCount) {
		this.newUserCount = newUserCount;
	}
	/**
	 * 获取：新增用户数
	 */
	public Integer getNewUserCount() {
		return newUserCount;
	}
	/**
	 * 设置：新增用户借条数
	 */
	public void setNewUserTxBount(Integer newUserTxBount) {
		this.newUserTxBount = newUserTxBount;
	}
	/**
	 * 获取：新增用户借条数
	 */
	public Integer getNewUserTxBount() {
		return newUserTxBount;
	}
	/**
	 * 设置：旧用户借条数
	 */
	public void setOldUserTxBount(Integer oldUserTxBount) {
		this.oldUserTxBount = oldUserTxBount;
	}
	/**
	 * 获取：旧用户借条数
	 */
	public Integer getOldUserTxBount() {
		return oldUserTxBount;
	}
	/**
	 * 设置：总借条数
	 */
	public void setTotalTxBount(Integer totalTxBount) {
		this.totalTxBount = totalTxBount;
	}
	/**
	 * 获取：总借条数
	 */
	public Integer getTotalTxBount() {
		return totalTxBount;
	}

	public Integer getTotalExCount() {
		return totalExCount;
	}

	public void setTotalExCount(final Integer totalExCount) {
		this.totalExCount = totalExCount;
	}
}
