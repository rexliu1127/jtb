package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   灯塔-胡萝卜获取今借到信息
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthJinJieDaoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//是否命中今借到
	private String jinJieDaoHit;
	//今借到
	private String jinJieDaoNote;

	//注册日期
	private String registerTime;
	//累积借入人数
	private String nBorrowPeopleNum;
	//累积借出次数
	private String nLendCnt;
	//累积借款金额
	private String nBorrowAmt;
	//当前逾期金额
	private String nCurrentOverdueAmt;
	//>=7 天逾期次数
	private String nOverdue7daysCnt;
	//当前逾期笔数
	private String nCurrentOverdueCnt;
	//>=7 天逾期金额
	private String nOverdue7daysAmt;
	//累积逾期总额
	private String nOverdueAmt;
	//历史逾期次数
	private String nOverdueCnt;
	//最大逾期金额
	private String nToOverdueMaxAmt;
	//最近 7 天逾期次数
	private String nCurrentOverdue7daysCnt;
	//是否超过 90 天的逾期
	private String nOverude90Days;
	//累积借入笔数
	private String nBorrowCnt;
	//>=30 天逾期金额
	private String nOverdue30daysAmt;
	//>=30 天逾期次数
	private String nOverdue30daysCnt;
	//逾期列表
	private List<AuthJinJieDaoOverDueVO>   overduelist;

	public String getJinJieDaoHit() {
		return jinJieDaoHit;
	}

	public void setJinJieDaoHit(String jinJieDaoHit) {
		this.jinJieDaoHit = jinJieDaoHit;
	}

	public String getJinJieDaoNote() {
		return jinJieDaoNote;
	}

	public void setJinJieDaoNote(String jinJieDaoNote) {
		this.jinJieDaoNote = jinJieDaoNote;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getnBorrowPeopleNum() {
		return nBorrowPeopleNum;
	}

	public void setnBorrowPeopleNum(String nBorrowPeopleNum) {
		this.nBorrowPeopleNum = nBorrowPeopleNum;
	}

	public String getnLendCnt() {
		return nLendCnt;
	}

	public void setnLendCnt(String nLendCnt) {
		this.nLendCnt = nLendCnt;
	}

	public String getnBorrowAmt() {
		return nBorrowAmt;
	}

	public void setnBorrowAmt(String nBorrowAmt) {
		this.nBorrowAmt = nBorrowAmt;
	}

	public String getnCurrentOverdueAmt() {
		return nCurrentOverdueAmt;
	}

	public void setnCurrentOverdueAmt(String nCurrentOverdueAmt) {
		this.nCurrentOverdueAmt = nCurrentOverdueAmt;
	}

	public String getnOverdue7daysCnt() {
		return nOverdue7daysCnt;
	}

	public void setnOverdue7daysCnt(String nOverdue7daysCnt) {
		this.nOverdue7daysCnt = nOverdue7daysCnt;
	}

	public String getnCurrentOverdueCnt() {
		return nCurrentOverdueCnt;
	}

	public void setnCurrentOverdueCnt(String nCurrentOverdueCnt) {
		this.nCurrentOverdueCnt = nCurrentOverdueCnt;
	}

	public String getnOverdue7daysAmt() {
		return nOverdue7daysAmt;
	}

	public void setnOverdue7daysAmt(String nOverdue7daysAmt) {
		this.nOverdue7daysAmt = nOverdue7daysAmt;
	}

	public String getnOverdueAmt() {
		return nOverdueAmt;
	}

	public void setnOverdueAmt(String nOverdueAmt) {
		this.nOverdueAmt = nOverdueAmt;
	}

	public String getnOverdueCnt() {
		return nOverdueCnt;
	}

	public void setnOverdueCnt(String nOverdueCnt) {
		this.nOverdueCnt = nOverdueCnt;
	}

	public String getnToOverdueMaxAmt() {
		return nToOverdueMaxAmt;
	}

	public void setnToOverdueMaxAmt(String nToOverdueMaxAmt) {
		this.nToOverdueMaxAmt = nToOverdueMaxAmt;
	}

	public String getnCurrentOverdue7daysCnt() {
		return nCurrentOverdue7daysCnt;
	}

	public void setnCurrentOverdue7daysCnt(String nCurrentOverdue7daysCnt) {
		this.nCurrentOverdue7daysCnt = nCurrentOverdue7daysCnt;
	}

	public String getnOverude90Days() {
		return nOverude90Days;
	}

	public void setnOverude90Days(String nOverude90Days) {
		this.nOverude90Days = nOverude90Days;
	}

	public String getnBorrowCnt() {
		return nBorrowCnt;
	}

	public void setnBorrowCnt(String nBorrowCnt) {
		this.nBorrowCnt = nBorrowCnt;
	}

	public String getnOverdue30daysAmt() {
		return nOverdue30daysAmt;
	}

	public void setnOverdue30daysAmt(String nOverdue30daysAmt) {
		this.nOverdue30daysAmt = nOverdue30daysAmt;
	}

	public String getnOverdue30daysCnt() {
		return nOverdue30daysCnt;
	}

	public void setnOverdue30daysCnt(String nOverdue30daysCnt) {
		this.nOverdue30daysCnt = nOverdue30daysCnt;
	}

	public List<AuthJinJieDaoOverDueVO> getOverduelist() {
		return overduelist;
	}

	public void setOverduelist(List<AuthJinJieDaoOverDueVO> overduelist) {
		this.overduelist = overduelist;
	}
}
