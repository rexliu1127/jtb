package io.grx.modules.auth.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * APP机器通讯录
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-10-22 14:27:10
 */
public class AuthMachineContactEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private String machineId;
	//联系人内容
	private String contact;
	//创建时间
	private Date createTime = new Date();

	/**
	 * 设置：
	 */
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	/**
	 * 获取：
	 */
	public String getMachineId() {
		return machineId;
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
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
