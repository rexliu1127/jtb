package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   灯塔-胡萝卜-借贷宝信息
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthJieDaiBaoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//是否命中借贷宝
	private String hit;
    //是否高风险用户
	private String isHighRiskUser;
	//最近一次访问日期
	private String lastDate;
	//30天以上逾期次数
	private String thirtydOverdueCnt;
	//历史逾期金额
	private String hisOverdueAmt;
	//最近一次逾期时间
	private String lastOverdueDt;
	//最近一次逾期金额
	private  String lastOverdueAmt;
	//当前逾期金额
	private String currOverdueAmt;
	//当前逾期最大天数
	private String currOverdueDays;
	//首次逾期时间
	private String firstOverdueDt;
	//首次逾期金额
	private String firstOverdueAmt;
	//最近一次还款时间
	private String lastRepayTm;
	//总还款次数
	private String repayTimes;
	//正在进行的贷款笔数
	private String currDebtProductCnt;
	//历史贷款笔数
	private String totalInOrderCnt;
	//历史总借款金额
	private String totalInOrderAmt;


	public String getHit() {
		return hit;
	}

	public void setHit(String hit) {
		this.hit = hit;
	}

	public String getIsHighRiskUser() {
		return isHighRiskUser;
	}

	public void setIsHighRiskUser(String isHighRiskUser) {
		this.isHighRiskUser = isHighRiskUser;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getThirtydOverdueCnt() {
		return thirtydOverdueCnt;
	}

	public void setThirtydOverdueCnt(String thirtydOverdueCnt) {
		this.thirtydOverdueCnt = thirtydOverdueCnt;
	}

	public String getHisOverdueAmt() {
		return hisOverdueAmt;
	}

	public void setHisOverdueAmt(String hisOverdueAmt) {
		this.hisOverdueAmt = hisOverdueAmt;
	}

	public String getLastOverdueDt() {
		return lastOverdueDt;
	}

	public void setLastOverdueDt(String lastOverdueDt) {
		this.lastOverdueDt = lastOverdueDt;
	}

	public String getLastOverdueAmt() {
		return lastOverdueAmt;
	}

	public void setLastOverdueAmt(String lastOverdueAmt) {
		this.lastOverdueAmt = lastOverdueAmt;
	}

	public String getCurrOverdueAmt() {
		return currOverdueAmt;
	}

	public void setCurrOverdueAmt(String currOverdueAmt) {
		this.currOverdueAmt = currOverdueAmt;
	}

	public String getCurrOverdueDays() {
		return currOverdueDays;
	}

	public void setCurrOverdueDays(String currOverdueDays) {
		this.currOverdueDays = currOverdueDays;
	}

	public String getFirstOverdueDt() {
		return firstOverdueDt;
	}

	public void setFirstOverdueDt(String firstOverdueDt) {
		this.firstOverdueDt = firstOverdueDt;
	}

	public String getFirstOverdueAmt() {
		return firstOverdueAmt;
	}

	public void setFirstOverdueAmt(String firstOverdueAmt) {
		this.firstOverdueAmt = firstOverdueAmt;
	}

	public String getLastRepayTm() {
		return lastRepayTm;
	}

	public void setLastRepayTm(String lastRepayTm) {
		this.lastRepayTm = lastRepayTm;
	}

	public String getRepayTimes() {
		return repayTimes;
	}

	public void setRepayTimes(String repayTimes) {
		this.repayTimes = repayTimes;
	}

	public String getCurrDebtProductCnt() {
		return currDebtProductCnt;
	}

	public void setCurrDebtProductCnt(String currDebtProductCnt) {
		this.currDebtProductCnt = currDebtProductCnt;
	}

	public String getTotalInOrderCnt() {
		return totalInOrderCnt;
	}

	public void setTotalInOrderCnt(String totalInOrderCnt) {
		this.totalInOrderCnt = totalInOrderCnt;
	}

	public String getTotalInOrderAmt() {
		return totalInOrderAmt;
	}

	public void setTotalInOrderAmt(String totalInOrderAmt) {
		this.totalInOrderAmt = totalInOrderAmt;
	}
}
