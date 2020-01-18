package io.grx.modules.auth.entity;



import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 *   运营商报告--运营商
 * 
 * @author randy.huang
 * @email jewian@gmail.com
 * @date 2018-03-21 22:32:58
 */
public class AuthTjMobileVO implements Serializable {
	private static final long serialVersionUID = 1L;

	//姓名
	private String name;

	//手机号
	private String phone;

	//身份证
	private String idCard;

	//紧急联系人
	private List<EmergencyInfo> emergency_info;

	//基本信息认证通过的手机号
	private String basePhone;
	//互联网标识
	private String phone_info;
	//类别标签
	private String phone_label;
	//运营商编码
	private String operator;
	//运营商中文
	private String operator_zh;
	//号码归属地
	private String phone_location;
	//注册时间
	private String reg_time;
	//运营商注册时的地址
	private String operator_addr;
	//运营商返回姓名
	private String operator_name;
	//运营商返回身份证
	private String operator_id_card;
	//姓名验证
	private Integer nameCheck;
	//身份证验证
	private Integer idCardCheck;
	//实名认证
	private Integer reliability;
	//性别
	private String gender;
	//年龄
	private Integer age;
	//省份
	private String province;
	//城市
	private String city;
	//区县
	private String region;
	//星座
	private String constellation;
	//当前余额
	private Float cur_balance;
	//月均消费
	private Float monthly_avg_consumption;
	//联系人逾期情况
	private List<ContactsOverdueInfo> contacts_overdue;
	//单人逾期情况
	private List<SingleOverdueInfo> singleOverdue;
	//金融服务类机构黑名单检查(身份证)
	private BlackCheckInfo financialBlacklistByIdCard;
	//金融服务类机构黑名单检查(手机号)
	private BlackCheckInfo financialBlacklistByPhone;
	//本人逾期数
	private Integer blacklistCnt;
	//机构TJ查询次数
	private Integer searchedCnt;
	//本人尝试申请次数
	private Integer applyCnt;
	//紧急联系人分析
	private List<EmergencyAnalysisInfo> emergencyAnalysis;
	//用户信息检测
	private CheckSearchInfo checkSearchInfo;

	//用户联系人黑名单信息
	private CheckBlackInfo checkBlackInfo;

	//用户画像
	//互通联系人个数
	private Integer bothCallCnt;
	//联系人地区分布
	private ContactsDistributionInfo contactsDistribution;

	//静默天数分析
	private SilentDaysInfo silentDays;
	//夜间活动情况
	private NightActivitiesInfo nightActivities;
	//特殊号码类别
	private List<SpecialCateInfo> specialCate;
	private List<SpecialCateInfo> specialCateEchart;
	//联系人地区分析
	private List<ContactAreaAnalysisInfo> contactAreaAnalysis;
	private List<ContactAreaAnalysisInfo> contactAreaAnalysisEchartTop10;
	//运营商月消费
	private List<MonthlyConsumptionInfo> monthlyConsumption;
	//通话地区分析
	private List<TelAreaAnalysisInfo> telAreaAnalysis;
	private List<TelAreaAnalysisInfo> telAreaAnalysisTop10;
	//通讯记录
	private List<CallLogInfo> callLog;
	//出行分析
	private List<TripInfo> tripInfo;
	//通话记录
	private List<AuthTjCallLogsVO> authTjCallLogsList;
	//通话记录-不考虑月份 加载所有的通话记录
	private List<AuthTjCallLogVO> callLogsList;


	public List<AuthTjCallLogVO> getCallLogsList() {
		return callLogsList;
	}

	public void setCallLogsList(List<AuthTjCallLogVO> callLogsList) {
		this.callLogsList = callLogsList;
	}

	public List<AuthTjCallLogsVO> getAuthTjCallLogsList() {
		return authTjCallLogsList;
	}

	public void setAuthTjCallLogsList(List<AuthTjCallLogsVO> authTjCallLogsList) {
		this.authTjCallLogsList = authTjCallLogsList;
	}

	public List<SpecialCateInfo> getSpecialCateEchart() {
		return specialCateEchart;
	}

	public void setSpecialCateEchart(List<SpecialCateInfo> specialCateEchart) {
		this.specialCateEchart = specialCateEchart;
	}

	public List<TelAreaAnalysisInfo> getTelAreaAnalysisTop10() {
		return telAreaAnalysisTop10;
	}

	public void setTelAreaAnalysisTop10(List<TelAreaAnalysisInfo> telAreaAnalysisTop10) {
		this.telAreaAnalysisTop10 = telAreaAnalysisTop10;
	}

	public List<ContactAreaAnalysisInfo> getContactAreaAnalysisEchartTop10() {
		return contactAreaAnalysisEchartTop10;
	}

	public void setContactAreaAnalysisEchartTop10(List<ContactAreaAnalysisInfo> contactAreaAnalysisEchartTop10) {
		this.contactAreaAnalysisEchartTop10 = contactAreaAnalysisEchartTop10;
	}

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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public List<EmergencyInfo> getEmergency_info() {
		return emergency_info;
	}

	public void setEmergency_info(List<EmergencyInfo> emergency_info) {
		this.emergency_info = emergency_info;
	}

	public String getBasePhone() {
		return basePhone;
	}

	public void setBasePhone(String basePhone) {
		this.basePhone = basePhone;
	}

	public String getPhone_info() {
		return phone_info;
	}

	public void setPhone_info(String phone_info) {
		this.phone_info = phone_info;
	}

	public String getPhone_label() {
		return phone_label;
	}

	public void setPhone_label(String phone_label) {
		this.phone_label = phone_label;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperator_zh() {
		return operator_zh;
	}

	public void setOperator_zh(String operator_zh) {
		this.operator_zh = operator_zh;
	}

	public String getPhone_location() {
		return phone_location;
	}

	public void setPhone_location(String phone_location) {
		this.phone_location = phone_location;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getOperator_addr() {
		return operator_addr;
	}

	public void setOperator_addr(String operator_addr) {
		this.operator_addr = operator_addr;
	}

	public String getOperator_name() {
		return operator_name;
	}

	public void setOperator_name(String operator_name) {
		this.operator_name = operator_name;
	}

	public String getOperator_id_card() {
		return operator_id_card;
	}

	public void setOperator_id_card(String operator_id_card) {
		this.operator_id_card = operator_id_card;
	}

	public Integer getReliability() {
		return reliability;
	}

	public void setReliability(Integer reliability) {
		this.reliability = reliability;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getConstellation() {
		return constellation;
	}

	public void setConstellation(String constellation) {
		this.constellation = constellation;
	}

	public Float getCur_balance() {
		return cur_balance;
	}

	public void setCur_balance(Float cur_balance) {
		this.cur_balance = cur_balance;
	}

	public Float getMonthly_avg_consumption() {
		return monthly_avg_consumption;
	}

	public void setMonthly_avg_consumption(Float monthly_avg_consumption) {
		this.monthly_avg_consumption = monthly_avg_consumption;
	}

	public List<ContactsOverdueInfo> getContacts_overdue() {
		return contacts_overdue;
	}

	public void setContacts_overdue(List<ContactsOverdueInfo> contacts_overdue) {
		this.contacts_overdue = contacts_overdue;
	}

	public List<SingleOverdueInfo> getSingleOverdue() {
		return singleOverdue;
	}

	public void setSingleOverdue(List<SingleOverdueInfo> singleOverdue) {
		this.singleOverdue = singleOverdue;
	}

	public BlackCheckInfo getFinancialBlacklistByIdCard() {
		return financialBlacklistByIdCard;
	}

	public void setFinancialBlacklistByIdCard(BlackCheckInfo financialBlacklistByIdCard) {
		this.financialBlacklistByIdCard = financialBlacklistByIdCard;
	}

	public BlackCheckInfo getFinancialBlacklistByPhone() {
		return financialBlacklistByPhone;
	}

	public void setFinancialBlacklistByPhone(BlackCheckInfo financialBlacklistByPhone) {
		this.financialBlacklistByPhone = financialBlacklistByPhone;
	}

	public Integer getBlacklistCnt() {
		return blacklistCnt;
	}

	public void setBlacklistCnt(Integer blacklistCnt) {
		this.blacklistCnt = blacklistCnt;
	}

	public Integer getSearchedCnt() {
		return searchedCnt;
	}

	public void setSearchedCnt(Integer searchedCnt) {
		this.searchedCnt = searchedCnt;
	}

	public Integer getApplyCnt() {
		return applyCnt;
	}

	public void setApplyCnt(Integer applyCnt) {
		this.applyCnt = applyCnt;
	}

	public List<EmergencyAnalysisInfo> getEmergencyAnalysis() {
		return emergencyAnalysis;
	}

	public void setEmergencyAnalysis(List<EmergencyAnalysisInfo> emergencyAnalysis) {
		this.emergencyAnalysis = emergencyAnalysis;
	}

	public CheckSearchInfo getCheckSearchInfo() {
		return checkSearchInfo;
	}

	public void setCheckSearchInfo(CheckSearchInfo checkSearchInfo) {
		this.checkSearchInfo = checkSearchInfo;
	}

	public CheckBlackInfo getCheckBlackInfo() {
		return checkBlackInfo;
	}

	public void setCheckBlackInfo(CheckBlackInfo checkBlackInfo) {
		this.checkBlackInfo = checkBlackInfo;
	}

	public Integer getBothCallCnt() {
		return bothCallCnt;
	}

	public void setBothCallCnt(Integer bothCallCnt) {
		this.bothCallCnt = bothCallCnt;
	}

	public ContactsDistributionInfo getContactsDistribution() {
		return contactsDistribution;
	}

	public void setContactsDistribution(ContactsDistributionInfo contactsDistribution) {
		this.contactsDistribution = contactsDistribution;
	}

	public SilentDaysInfo getSilentDays() {
		return silentDays;
	}

	public void setSilentDays(SilentDaysInfo silentDays) {
		this.silentDays = silentDays;
	}

	public NightActivitiesInfo getNightActivities() {
		return nightActivities;
	}

	public void setNightActivities(NightActivitiesInfo nightActivities) {
		this.nightActivities = nightActivities;
	}

	public List<SpecialCateInfo> getSpecialCate() {
		return specialCate;
	}

	public void setSpecialCate(List<SpecialCateInfo> specialCate) {
		this.specialCate = specialCate;
	}

	public List<ContactAreaAnalysisInfo> getContactAreaAnalysis() {
		return contactAreaAnalysis;
	}

	public void setContactAreaAnalysis(List<ContactAreaAnalysisInfo> contactAreaAnalysis) {
		this.contactAreaAnalysis = contactAreaAnalysis;
	}

	public List<MonthlyConsumptionInfo> getMonthlyConsumption() {
		return monthlyConsumption;
	}

	public void setMonthlyConsumption(List<MonthlyConsumptionInfo> monthlyConsumption) {
		this.monthlyConsumption = monthlyConsumption;
	}

	public List<TelAreaAnalysisInfo> getTelAreaAnalysis() {
		return telAreaAnalysis;
	}

	public void setTelAreaAnalysis(List<TelAreaAnalysisInfo> telAreaAnalysis) {
		this.telAreaAnalysis = telAreaAnalysis;
	}

	public List<CallLogInfo> getCallLog() {
		return callLog;
	}

	public void setCallLog(List<CallLogInfo> callLog) {
		this.callLog = callLog;
	}

	public List<TripInfo> getTripInfo() {
		return tripInfo;
	}

	public void setTripInfo(List<TripInfo> tripInfo) {
		this.tripInfo = tripInfo;
	}

	public Integer getNameCheck() {
		return nameCheck;
	}

	public void setNameCheck(Integer nameCheck) {
		this.nameCheck = nameCheck;
	}

	public Integer getIdCardCheck() {
		return idCardCheck;
	}

	public void setIdCardCheck(Integer idCardCheck) {
		this.idCardCheck = idCardCheck;
	}
}
