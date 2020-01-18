package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   米兜综合报告--运营商报告
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthMobileReportVO implements Serializable {
	private static final long serialVersionUID = 1L;


	//姓名是否一致  name_check
	private String nameCheck;

	//身份证是否一致  id_card_check
	private String  idCardCheck;

	//注册时间
	private String regTime;

	//账户余额 cur_balance
	private String  curBalance;

	//身份证号命中黑名单情况
	private Integer cardhitCount;

	//手机号码命中黑名单情况
	private Integer phoneHitCount;

	//紧急联系人通话 记录
	private Integer emergencyCallCount;

	//机构Tj查询次数  searched_cnt
	private String   searchedCnt;

	//号码静默情况  max_interval
	private String maxInterval;

	//本人逾期数 blacklist_cnt
	private Integer blacklistCnt;

	//夜间通话占比  monthly_avg_seconds_ratio
	private String  monthlyAvgSecondsRatio;

	//用户黑名单比例   check_black_info.contacts_class1_blacklist_cnt/check_black_info.contacts_class1_cnt
	private String contactsBlacklistRatio;

	//互通电话数量  both_call_cnt
	private String bothCallCnt;

	//紧急联系人1号 码命中逾期次数  single_overdue
	private List<SingleOverdueVO> singleOverdue;

	//110号码通话次数  special_cate
	private Integer callCount110;

	//贷款类通话次数  special_cate
	private Integer callCount005;

	//互金公司通话次数  special_cate
	private  Integer callCount008;

	//通讯录有无
	private  String  Maillist;

	//有效联系人数  contacts_class1_cnt
	private Integer contactsClass1Cnt;

	//通讯录借贷标签cate 5:贷款类  8:互金公司
	private Integer  cateCnt;

	//白骑士身份信息验证(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer bqsHighRiskList;

	//运营商信息验证(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer  mnoBaseInfoRiskList;

	//通讯录(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer mobileRiskList;

	//多头借贷风险(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer duotouRiskList;

	//借条平台(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer  jietiaoRiskList;

	//社交健康(查询成功:0 / 疑似风险:1/ 获取失败:2)
	private Integer  socialRiskList;

	//性别
	private String gender;

	//年龄
	private String age;
	//户籍  出生地
	private String birthAddress;

	//信贷行业-P2P黑名单级别
	private String p2pRiskGrade;
	//多头名单级别
	private String duotouRiskGrade;
	//法院失信名单级别
	private String courtRiskGrade;

	//是否实名
	private Boolean passRealName;
	//手机号
	private String mobile;

	//信用分-总分
	private Integer totalScore;
	//紧急联系人个数
	private Integer  emergencyContacts;
	//号码静默天数 -全天未使用通话和短信功能天数
	private Integer notCallAndSmsDayCount;
	//夜间通话次数(nightCallCount)
	private Integer nightCallCount;
	//互通电话号码数（exchangeCallMobileCount）
	private Integer exchangeCallMobileCount;


	//是否多头申请
	private Integer partnerCount ;
	//手机号关联身份证个数
	private Integer idcCount;
	//身份证关联手机号个数
	private  Integer phoneCount;


	public Integer getPhoneHitCount() {
		return phoneHitCount;
	}

	public void setPhoneHitCount(Integer phoneHitCount) {
		this.phoneHitCount = phoneHitCount;
	}

	public Integer getPartnerCount() {
		return partnerCount;
	}

	public void setPartnerCount(Integer partnerCount) {
		this.partnerCount = partnerCount;
	}

	public Integer getIdcCount() {
		return idcCount;
	}

	public void setIdcCount(Integer idcCount) {
		this.idcCount = idcCount;
	}

	public Integer getPhoneCount() {
		return phoneCount;
	}

	public void setPhoneCount(Integer phoneCount) {
		this.phoneCount = phoneCount;
	}




	public Boolean getPassRealName() {
		return passRealName;
	}

	public void setPassRealName(Boolean passRealName) {
		this.passRealName = passRealName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Integer getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getEmergencyContacts() {
		return emergencyContacts;
	}

	public void setEmergencyContacts(Integer emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}

	public Integer getNotCallAndSmsDayCount() {
		return notCallAndSmsDayCount;
	}

	public void setNotCallAndSmsDayCount(Integer notCallAndSmsDayCount) {
		this.notCallAndSmsDayCount = notCallAndSmsDayCount;
	}

	public Integer getNightCallCount() {
		return nightCallCount;
	}

	public void setNightCallCount(Integer nightCallCount) {
		this.nightCallCount = nightCallCount;
	}

	public Integer getExchangeCallMobileCount() {
		return exchangeCallMobileCount;
	}

	public void setExchangeCallMobileCount(Integer exchangeCallMobileCount) {
		this.exchangeCallMobileCount = exchangeCallMobileCount;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBirthAddress() {
		return birthAddress;
	}

	public void setBirthAddress(String birthAddress) {
		this.birthAddress = birthAddress;
	}

	public String getP2pRiskGrade() {
		return p2pRiskGrade;
	}

	public void setP2pRiskGrade(String p2pRiskGrade) {
		this.p2pRiskGrade = p2pRiskGrade;
	}

	public String getDuotouRiskGrade() {
		return duotouRiskGrade;
	}

	public void setDuotouRiskGrade(String duotouRiskGrade) {
		this.duotouRiskGrade = duotouRiskGrade;
	}

	public String getCourtRiskGrade() {
		return courtRiskGrade;
	}

	public void setCourtRiskGrade(String courtRiskGrade) {
		this.courtRiskGrade = courtRiskGrade;
	}

	public Integer getBqsHighRiskList() {
		return bqsHighRiskList;
	}

	public void setBqsHighRiskList(Integer bqsHighRiskList) {
		this.bqsHighRiskList = bqsHighRiskList;
	}

	public Integer getMnoBaseInfoRiskList() {
		return mnoBaseInfoRiskList;
	}

	public void setMnoBaseInfoRiskList(Integer mnoBaseInfoRiskList) {
		this.mnoBaseInfoRiskList = mnoBaseInfoRiskList;
	}

	public Integer getMobileRiskList() {
		return mobileRiskList;
	}

	public void setMobileRiskList(Integer mobileRiskList) {
		this.mobileRiskList = mobileRiskList;
	}

	public Integer getDuotouRiskList() {
		return duotouRiskList;
	}

	public void setDuotouRiskList(Integer duotouRiskList) {
		this.duotouRiskList = duotouRiskList;
	}

	public Integer getJietiaoRiskList() {
		return jietiaoRiskList;
	}

	public void setJietiaoRiskList(Integer jietiaoRiskList) {
		this.jietiaoRiskList = jietiaoRiskList;
	}

	public Integer getSocialRiskList() {
		return socialRiskList;
	}

	public void setSocialRiskList(Integer socialRiskList) {
		this.socialRiskList = socialRiskList;
	}

	public String getIdCardCheck() {
		return idCardCheck;
	}

	public void setIdCardCheck(String idCardCheck) {
		this.idCardCheck = idCardCheck;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getCurBalance() {
		return curBalance;
	}

	public void setCurBalance(String curBalance) {
		this.curBalance = curBalance;
	}

	public Integer getCardhitCount() {
		return cardhitCount;
	}

	public void setCardhitCount(Integer cardhitCount) {
		this.cardhitCount = cardhitCount;
	}

	public Integer getEmergencyCallCount() {
		return emergencyCallCount;
	}

	public void setEmergencyCallCount(Integer emergencyCallCount) {
		this.emergencyCallCount = emergencyCallCount;
	}

	public String getSearchedCnt() {
		return searchedCnt;
	}

	public void setSearchedCnt(String searchedCnt) {
		this.searchedCnt = searchedCnt;
	}

	public String getMaxInterval() {
		return maxInterval;
	}

	public void setMaxInterval(String maxInterval) {
		this.maxInterval = maxInterval;
	}

	public Integer getBlacklistCnt() {
		return blacklistCnt;
	}

	public void setBlacklistCnt(Integer blacklistCnt) {
		this.blacklistCnt = blacklistCnt;
	}

	public String getMonthlyAvgSecondsRatio() {
		return monthlyAvgSecondsRatio;
	}

	public void setMonthlyAvgSecondsRatio(String monthlyAvgSecondsRatio) {
		this.monthlyAvgSecondsRatio = monthlyAvgSecondsRatio;
	}

	public String getContactsBlacklistRatio() {
		return contactsBlacklistRatio;
	}

	public void setContactsBlacklistRatio(String contactsBlacklistRatio) {
		this.contactsBlacklistRatio = contactsBlacklistRatio;
	}

	public String getBothCallCnt() {
		return bothCallCnt;
	}

	public void setBothCallCnt(String bothCallCnt) {
		this.bothCallCnt = bothCallCnt;
	}

	public List<SingleOverdueVO> getSingleOverdue() {
		return singleOverdue;
	}

	public void setSingleOverdue( List<SingleOverdueVO> singleOverdue) {
		this.singleOverdue = singleOverdue;
	}

	public Integer getCallCount110() {
		return callCount110;
	}

	public void setCallCount110(Integer callCount110) {
		this.callCount110 = callCount110;
	}

	public Integer getCallCount005() {
		return callCount005;
	}

	public void setCallCount005(Integer callCount005) {
		this.callCount005 = callCount005;
	}

	public Integer getCallCount008() {
		return callCount008;
	}

	public void setCallCount008(Integer callCount008) {
		this.callCount008 = callCount008;
	}


	public String getNameCheck() {
		return nameCheck;
	}

	public void setNameCheck(String nameCheck) {
		this.nameCheck = nameCheck;
	}

	public Integer getContactsClass1Cnt() {
		return contactsClass1Cnt;
	}

	public void setContactsClass1Cnt(Integer contactsClass1Cnt) {
		this.contactsClass1Cnt = contactsClass1Cnt;
	}

	public Integer getCateCnt() {
		return cateCnt;
	}

	public void setCateCnt(Integer cateCnt) {
		this.cateCnt = cateCnt;
	}

	public String getMaillist() {
		return Maillist;
	}

	public void setMaillist(String maillist) {
		Maillist = maillist;
	}
}
