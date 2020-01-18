package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   运营商报告--联系人逾期情况
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class ContactsOverdueInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//存在逾期行为的人数
	private Integer hitCnt;
	//与此类人群的总通话时长（单位秒）
	private Integer seconds;
	//与此类人群的总通话次数
	private Integer cnt;
	//联系人类别
	private Integer type;

	public Integer getHitCnt() {
		return hitCnt;
	}

	public void setHitCnt(Integer hitCnt) {
		this.hitCnt = hitCnt;
	}

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
