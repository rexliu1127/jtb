package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--紧急联系人
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class EmergencyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//姓名
	private String name;
	//手机号
	private String phone;
	//紧急联系人
	private String relation;
	//紧急联系人编号
	private Integer no;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}
}
