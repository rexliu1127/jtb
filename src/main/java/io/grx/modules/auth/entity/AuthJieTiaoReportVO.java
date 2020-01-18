package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   米兜综合报告--借条
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthJieTiaoReportVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//借条平台有无 逾期
	private String isOverdue;

	//5大借条平台 申请记录
	private Integer effectApplyCnt;

	//联系人在本平台是否贷款
	private String isLoan;


	public String getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(String isOverdue) {
		this.isOverdue = isOverdue;
	}

	public Integer getEffectApplyCnt() {
		return effectApplyCnt;
	}

	public void setEffectApplyCnt(Integer effectApplyCnt) {
		this.effectApplyCnt = effectApplyCnt;
	}

	public String getIsLoan() {
		return isLoan;
	}

	public void setIsLoan(String isLoan) {
		this.isLoan = isLoan;
	}
}
