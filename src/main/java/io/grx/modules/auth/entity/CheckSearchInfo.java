package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   运营商报告--黑名单检查
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class CheckSearchInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	//是否出现
	private Integer searchedOrgCnt;
	//查询过该用户的相关企业类型
	private List<String> searchedOrgType;
	//身份证组合过的其他姓名
	private List<String> idcardWithOtherNames;
	private String idcardWithOtherNamesStr;
	//身份证组合过其他电话
	private List<String> idcardWithOtherPhones;
	private String idcardWithOtherPhonesStr;
	//电话号码组合过其他姓名
	private List<String> phoneWithOtherNames;
	private String phoneWithOtherNamesStr;
	//电话号码组合过其他身份证
	private List<String> phoneWithOtherIdcards;
	private String phoneWithOtherIdcardsStr;

	public String getIdcardWithOtherNamesStr() {
		return idcardWithOtherNamesStr;
	}

	public void setIdcardWithOtherNamesStr(String idcardWithOtherNamesStr) {
		this.idcardWithOtherNamesStr = idcardWithOtherNamesStr;
	}

	public String getIdcardWithOtherPhonesStr() {
		return idcardWithOtherPhonesStr;
	}

	public void setIdcardWithOtherPhonesStr(String idcardWithOtherPhonesStr) {
		this.idcardWithOtherPhonesStr = idcardWithOtherPhonesStr;
	}

	public String getPhoneWithOtherNamesStr() {
		return phoneWithOtherNamesStr;
	}

	public void setPhoneWithOtherNamesStr(String phoneWithOtherNamesStr) {
		this.phoneWithOtherNamesStr = phoneWithOtherNamesStr;
	}

	public String getPhoneWithOtherIdcardsStr() {
		return phoneWithOtherIdcardsStr;
	}

	public void setPhoneWithOtherIdcardsStr(String phoneWithOtherIdcardsStr) {
		this.phoneWithOtherIdcardsStr = phoneWithOtherIdcardsStr;
	}

	public Integer getSearchedOrgCnt() {
		return searchedOrgCnt;
	}

	public void setSearchedOrgCnt(Integer searchedOrgCnt) {
		this.searchedOrgCnt = searchedOrgCnt;
	}

	public List<String> getSearchedOrgType() {
		return searchedOrgType;
	}

	public void setSearchedOrgType(List<String> searchedOrgType) {
		this.searchedOrgType = searchedOrgType;
	}

	public List<String> getIdcardWithOtherNames() {
		return idcardWithOtherNames;
	}

	public void setIdcardWithOtherNames(List<String> idcardWithOtherNames) {
		this.idcardWithOtherNames = idcardWithOtherNames;
	}

	public List<String> getIdcardWithOtherPhones() {
		return idcardWithOtherPhones;
	}

	public void setIdcardWithOtherPhones(List<String> idcardWithOtherPhones) {
		this.idcardWithOtherPhones = idcardWithOtherPhones;
	}

	public List<String> getPhoneWithOtherNames() {
		return phoneWithOtherNames;
	}

	public void setPhoneWithOtherNames(List<String> phoneWithOtherNames) {
		this.phoneWithOtherNames = phoneWithOtherNames;
	}

	public List<String> getPhoneWithOtherIdcards() {
		return phoneWithOtherIdcards;
	}

	public void setPhoneWithOtherIdcards(List<String> phoneWithOtherIdcards) {
		this.phoneWithOtherIdcards = phoneWithOtherIdcards;
	}
}
