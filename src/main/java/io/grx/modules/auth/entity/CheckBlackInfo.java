package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--用户信息检测(用户黑名单信息)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class CheckBlackInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//直接联系人中黑名单人数
	private Integer contactsClass1BlacklistCnt;
	//直接联系人人数
	private Integer contactsclass1Cnt;

	public Integer getContactsClass1BlacklistCnt() {
		return contactsClass1BlacklistCnt;
	}

	public void setContactsClass1BlacklistCnt(Integer contactsClass1BlacklistCnt) {
		this.contactsClass1BlacklistCnt = contactsClass1BlacklistCnt;
	}

	public Integer getContactsclass1Cnt() {
		return contactsclass1Cnt;
	}

	public void setContactsclass1Cnt(Integer contactsclass1Cnt) {
		this.contactsclass1Cnt = contactsclass1Cnt;
	}
}
