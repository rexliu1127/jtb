package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;


/**
 *   米兜综合报告--有盾
 *
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthYouDunReportVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//姓名
	private String name;

	//性别
	private String gender;

	//民族
	private String nation;

	//身份证地址
	private String address;

	//签发机构
	private String issuingAgency;

	//身份证号
	private String idNumberMask;

	//时间
	private String lastModifiedTime;

	//用户特征-多头借贷
	private String features0;
	//用户特征-羊毛党
	private String features2;
	//用户特征-作弊软件
	private String features5;
	//用户特征-法院失信
	private String features6;
	//用户特征-网贷失信
	private String features7;
	//用户特征-关联过多
	private String features8;
	//用户特征-曾使用可疑设备
	private String features10;
	//用户特征-安装极多借贷app
	private String features11;
	//用户特征-身份信息疑似泄漏
	private String features13;
	//用户特征-活体攻击设备
	private String features17;
	//用户特征-活体攻击行为
	private String features18;
	//用户特征-疑似欺诈团伙
	private String features21;
	//用户特征-疑似欺诈团伙
	private String features23;
	//用户特征-短期逾期
	private String features24;

	//风险模型得分
	private String score;
	//有盾贷前模型
	private String riskEvaluation;

	//关联用户数  link_user_count
	private Integer linkUserCount;

	//使用设备数  link_device_count
	private Integer linkDeviceCount;

	//申请借款平台数
	private Integer loanPlatformAllcount;
	//实际借款平台数 actual_loan_platform_count
	private Integer actualoanPlatformCount;

	//还款平台数    repayment_platform_count
	private Integer repaymentPlatformCount;

	//还款笔数  还款次数  repayment_times_count
	private  Integer repaymentTimesCount;

	//申请借款(总计,6个月,3个月,1个月)
	private List<Integer> allLoanInfo;

	//实际借款(总计,6个月,3个月,1个月)
	private List<Integer>  actualCountInfo;

	//还款总计,6个月,3个月,1个月)
	private List<Integer>  repaymentCcountInfo;


	//设备详情
	private List<AuthYouDunDevicesVO>   devicesList;
	//借款详情
	private List<AuthYouDunLoanInfoVO>    loanInfoList;

	///使用手机号个数
	private  Integer mobileCount;

	//关联用户数
	private String graphUserCount;

	//其他关联设备数
	private String otherDeviceCount;

	//本商户内用户数
	private String partnerUserCount;
	//名下银行卡数
	private Integer userHaveBankcardCount;
	//关联银行卡数
	private  String bankcardCount;
	//使用设备数
	private String deviceCount;
	//法院失信
	private String courtDishonestCount;
	//网贷失信
	private String onlineDishonestCount;
	//活体攻击行为
	private String livingAttackCount;
	//商户商户标记个数
	private String partnerMarkCount;
	//其他关联设详情-可疑设备
	private String otherFrandDeviceCount;
	//其他关联设详情 活体攻击设备
	private String otherLivingAttackDcount;
	//使用设备数详情-可疑设备
	private String  frandDeviceCount;
	//活体攻击设备
	private String livingAttackDcount;

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(String lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getFeatures0() {
		return features0;
	}

	public void setFeatures0(String features0) {
		this.features0 = features0;
	}

	public String getFeatures2() {
		return features2;
	}

	public void setFeatures2(String features2) {
		this.features2 = features2;
	}

	public String getFeatures5() {
		return features5;
	}

	public void setFeatures5(String features5) {
		this.features5 = features5;
	}

	public String getFeatures6() {
		return features6;
	}

	public void setFeatures6(String features6) {
		this.features6 = features6;
	}

	public String getFeatures7() {
		return features7;
	}

	public void setFeatures7(String features7) {
		this.features7 = features7;
	}

	public String getFeatures8() {
		return features8;
	}

	public void setFeatures8(String features8) {
		this.features8 = features8;
	}

	public String getFeatures10() {
		return features10;
	}

	public void setFeatures10(String features10) {
		this.features10 = features10;
	}

	public String getFeatures11() {
		return features11;
	}

	public void setFeatures11(String features11) {
		this.features11 = features11;
	}

	public String getFeatures13() {
		return features13;
	}

	public void setFeatures13(String features13) {
		this.features13 = features13;
	}

	public String getFeatures17() {
		return features17;
	}

	public void setFeatures17(String features17) {
		this.features17 = features17;
	}

	public String getFeatures18() {
		return features18;
	}

	public void setFeatures18(String features18) {
		this.features18 = features18;
	}

	public String getFeatures21() {
		return features21;
	}

	public void setFeatures21(String features21) {
		this.features21 = features21;
	}

	public String getFeatures23() {
		return features23;
	}

	public void setFeatures23(String features23) {
		this.features23 = features23;
	}

	public String getFeatures24() {
		return features24;
	}

	public void setFeatures24(String features24) {
		this.features24 = features24;
	}

	public Integer getLoanPlatformAllcount() {
		return loanPlatformAllcount;
	}

	public void setLoanPlatformAllcount(Integer loanPlatformAllcount) {
		this.loanPlatformAllcount = loanPlatformAllcount;
	}

	public String getIdNumberMask() {
		return idNumberMask;
	}

	public void setIdNumberMask(String idNumberMask) {
		this.idNumberMask = idNumberMask;
	}

	public Integer getMobileCount() {
		return mobileCount;
	}

	public void setMobileCount(Integer mobileCount) {
		this.mobileCount = mobileCount;
	}

	public String getGraphUserCount() {
		return graphUserCount;
	}

	public void setGraphUserCount(String graphUserCount) {
		this.graphUserCount = graphUserCount;
	}

	public String getOtherDeviceCount() {
		return otherDeviceCount;
	}

	public void setOtherDeviceCount(String otherDeviceCount) {
		this.otherDeviceCount = otherDeviceCount;
	}

	public String getPartnerUserCount() {
		return partnerUserCount;
	}

	public void setPartnerUserCount(String partnerUserCount) {
		this.partnerUserCount = partnerUserCount;
	}

	public Integer getUserHaveBankcardCount() {
		return userHaveBankcardCount;
	}

	public void setUserHaveBankcardCount(Integer userHaveBankcardCount) {
		this.userHaveBankcardCount = userHaveBankcardCount;
	}

	public String getBankcardCount() {
		return bankcardCount;
	}

	public void setBankcardCount(String bankcardCount) {
		this.bankcardCount = bankcardCount;
	}

	public String getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(String deviceCount) {
		this.deviceCount = deviceCount;
	}

	public String getCourtDishonestCount() {
		return courtDishonestCount;
	}

	public void setCourtDishonestCount(String courtDishonestCount) {
		this.courtDishonestCount = courtDishonestCount;
	}

	public String getOnlineDishonestCount() {
		return onlineDishonestCount;
	}

	public void setOnlineDishonestCount(String onlineDishonestCount) {
		this.onlineDishonestCount = onlineDishonestCount;
	}

	public String getLivingAttackCount() {
		return livingAttackCount;
	}

	public void setLivingAttackCount(String livingAttackCount) {
		this.livingAttackCount = livingAttackCount;
	}

	public String getPartnerMarkCount() {
		return partnerMarkCount;
	}

	public void setPartnerMarkCount(String partnerMarkCount) {
		this.partnerMarkCount = partnerMarkCount;
	}

	public String getOtherFrandDeviceCount() {
		return otherFrandDeviceCount;
	}

	public void setOtherFrandDeviceCount(String otherFrandDeviceCount) {
		this.otherFrandDeviceCount = otherFrandDeviceCount;
	}

	public String getOtherLivingAttackDcount() {
		return otherLivingAttackDcount;
	}

	public void setOtherLivingAttackDcount(String otherLivingAttackDcount) {
		this.otherLivingAttackDcount = otherLivingAttackDcount;
	}

	public String getFrandDeviceCount() {
		return frandDeviceCount;
	}

	public void setFrandDeviceCount(String frandDeviceCount) {
		this.frandDeviceCount = frandDeviceCount;
	}

	public String getLivingAttackDcount() {
		return livingAttackDcount;
	}

	public void setLivingAttackDcount(String livingAttackDcount) {
		this.livingAttackDcount = livingAttackDcount;
	}

	public List<AuthYouDunDevicesVO> getDevicesList() {
		return devicesList;
	}

	public void setDevicesList(List<AuthYouDunDevicesVO> devicesList) {
		this.devicesList = devicesList;
	}

	public List<AuthYouDunLoanInfoVO> getLoanInfoList() {
		return loanInfoList;
	}

	public void setLoanInfoList(List<AuthYouDunLoanInfoVO> loanInfoList) {
		this.loanInfoList = loanInfoList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIssuingAgency() {
		return issuingAgency;
	}

	public void setIssuingAgency(String issuingAgency) {
		this.issuingAgency = issuingAgency;
	}

	public List<Integer> getAllLoanInfo() {
		return allLoanInfo;
	}

	public void setAllLoanInfo(List<Integer> allLoanInfo) {
		this.allLoanInfo = allLoanInfo;
	}

	public List<Integer> getActualCountInfo() {
		return actualCountInfo;
	}

	public void setActualCountInfo(List<Integer> actualCountInfo) {
		this.actualCountInfo = actualCountInfo;
	}

	public List<Integer> getRepaymentCcountInfo() {
		return repaymentCcountInfo;
	}

	public void setRepaymentCcountInfo(List<Integer> repaymentCcountInfo) {
		this.repaymentCcountInfo = repaymentCcountInfo;
	}

	//是否有风险
	private Integer  riskList;

	public Integer getRiskList() {
		return riskList;
	}

	public void setRiskList(Integer riskList) {
		this.riskList = riskList;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRiskEvaluation() {
		return riskEvaluation;
	}

	public void setRiskEvaluation(String riskEvaluation) {
		this.riskEvaluation = riskEvaluation;
	}

	public Integer getLinkUserCount() {
		return linkUserCount;
	}

	public void setLinkUserCount(Integer linkUserCount) {
		this.linkUserCount = linkUserCount;
	}

	public Integer getLinkDeviceCount() {
		return linkDeviceCount;
	}

	public void setLinkDeviceCount(Integer linkDeviceCount) {
		this.linkDeviceCount = linkDeviceCount;
	}

	public Integer getActualoanPlatformCount() {
		return actualoanPlatformCount;
	}

	public void setActualoanPlatformCount(Integer actualoanPlatformCount) {
		this.actualoanPlatformCount = actualoanPlatformCount;
	}

	public Integer getRepaymentPlatformCount() {
		return repaymentPlatformCount;
	}

	public void setRepaymentPlatformCount(Integer repaymentPlatformCount) {
		this.repaymentPlatformCount = repaymentPlatformCount;
	}

	public Integer getRepaymentTimesCount() {
		return repaymentTimesCount;
	}

	public void setRepaymentTimesCount(Integer repaymentTimesCount) {
		this.repaymentTimesCount = repaymentTimesCount;
	}
}
