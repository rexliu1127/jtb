package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-20 22:35:22
 */
public class AuthUserContactEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long userId;
	//联系人内容
	private String contact;

	/**
	 * 设置：
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：联系人内容
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * 获取：联系人内容
	 */
	public String getContact() {
		return contact;
	}
}
