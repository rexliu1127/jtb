package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   米兜综合报告--运营商报告(联系人的黑名单命中情况)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class SingleOverdueVO implements Serializable {
	private static final long serialVersionUID = 1L;


	//紧急联系人
	private String no;

	//命中个数
	private String  hitCnt;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getHitCnt() {
		return hitCnt;
	}

	public void setHitCnt(String hitCnt) {
		this.hitCnt = hitCnt;
	}
}
