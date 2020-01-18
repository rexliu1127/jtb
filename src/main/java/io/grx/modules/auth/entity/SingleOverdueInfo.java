package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   运营商报告--单人逾期情况
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class SingleOverdueInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//手机号维度的逾期次数（命中个数）
	private Integer hitCnt;
	//是否电话联系
	private Integer ifTel;
	//是否短信联系
	private Integer if_msg;
	//单人类别
	private Integer no;
	//通话记录
	private String telRecord;
	//短信记录
	private String msgRecord;

	public String getTelRecord() {
		return telRecord;
	}

	public void setTelRecord(String telRecord) {
		this.telRecord = telRecord;
	}

	public String getMsgRecord() {
		return msgRecord;
	}

	public void setMsgRecord(String msgRecord) {
		this.msgRecord = msgRecord;
	}

	public Integer getHitCnt() {
		return hitCnt;
	}

	public void setHitCnt(Integer hitCnt) {
		this.hitCnt = hitCnt;
	}

	public Integer getIfTel() {
		return ifTel;
	}

	public void setIfTel(Integer ifTel) {
		this.ifTel = ifTel;
	}

	public Integer getIf_msg() {
		return if_msg;
	}

	public void setIf_msg(Integer if_msg) {
		this.if_msg = if_msg;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}
}
