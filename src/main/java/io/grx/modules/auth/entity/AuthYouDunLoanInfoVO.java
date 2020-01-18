package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   有盾-借款详情
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthYouDunLoanInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//设备名称
	private String loanName;

	//实际借款平台数
	private String actualloanPlatformCounts;

	//申请借款平台数
	private String loanPlatformCounts;

	//还款次数
	private String repaymentTimesCounts;

	//还款平台数
	private String repaymentPlatformCounts;

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public String getActualloanPlatformCounts() {
		return actualloanPlatformCounts;
	}

	public void setActualloanPlatformCounts(String actualloanPlatformCounts) {
		this.actualloanPlatformCounts = actualloanPlatformCounts;
	}

	public String getLoanPlatformCounts() {
		return loanPlatformCounts;
	}

	public void setLoanPlatformCounts(String loanPlatformCounts) {
		this.loanPlatformCounts = loanPlatformCounts;
	}

	public String getRepaymentTimesCounts() {
		return repaymentTimesCounts;
	}

	public void setRepaymentTimesCounts(String repaymentTimesCounts) {
		this.repaymentTimesCounts = repaymentTimesCounts;
	}

	public String getRepaymentPlatformCounts() {
		return repaymentPlatformCounts;
	}

	public void setRepaymentPlatformCounts(String repaymentPlatformCounts) {
		this.repaymentPlatformCounts = repaymentPlatformCounts;
	}
}
