package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   灯塔-胡萝卜获取今借到逾期列表
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthJinJieDaoOverDueVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//还款日期
	private String tRepayTm;
	//计息日期
	private String ctm;
	//金额
	private String namt;
	//逾期天数
	private String ncount;
	//处理结果0 未处理，1 已处理
	private String bend;

	public String gettRepayTm() {
		return tRepayTm;
	}

	public void settRepayTm(String tRepayTm) {
		this.tRepayTm = tRepayTm;
	}

	public String getCtm() {
		return ctm;
	}

	public void setCtm(String ctm) {
		this.ctm = ctm;
	}

	public String getNamt() {
		return namt;
	}

	public void setNamt(String namt) {
		this.namt = namt;
	}

	public String getNcount() {
		return ncount;
	}

	public void setNcount(String ncount) {
		this.ncount = ncount;
	}

	public String getBend() {
		return bend;
	}

	public void setBend(String bend) {
		this.bend = bend;
	}
}
