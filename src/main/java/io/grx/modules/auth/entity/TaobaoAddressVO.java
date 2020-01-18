package io.grx.modules.auth.entity;



import java.io.Serializable;


/**
 *   全知-电商报告(淘宝收货地址)
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class TaobaoAddressVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//姓名
	private String name;
	//地址
	private String address;
	//联系方式
	private String phoneNo;
	//邮编
	private String postCode;
	//备注
	private String note;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
