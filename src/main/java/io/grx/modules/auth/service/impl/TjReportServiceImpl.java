package io.grx.modules.auth.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.grx.modules.auth.dao.AuthUserContactDao;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.contact.entity.UserEntity;
import io.grx.modules.contact.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.grx.auth.entity.AuthConfig;
import io.grx.modules.auth.dao.TjReportDao;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.auth.util.AuthConstants;
import io.grx.modules.sys.service.SysConfigService;


@Service("tjReportService")
public class TjReportServiceImpl implements TjReportService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TjReportDao tjReportDao;
	@Autowired
	private AuthUserContactDao authUserContactDao;

	@Autowired
	private UserService userService;

	@Autowired
    private SysConfigService sysConfigService;

    private static final Map<String, TianjiType> TJ_TYPE_MAP = Arrays.stream(TianjiType.values())
            .collect(Collectors.toMap(TianjiType::name, x -> x));
	
	@Override
	public TjReportEntity queryObject(Long id){
		return tjReportDao.queryObject(id);
	}
	
	@Override
	public List<TjReportEntity> queryList(Map<String, Object> map){
		return tjReportDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return tjReportDao.queryTotal(map);
	}
	
	@Override
	@Transactional
	public void save(TjReportEntity tjReport){
		tjReportDao.save(tjReport);
	}
	
	@Override
	@Transactional
	public void update(TjReportEntity tjReport){
		tjReportDao.update(tjReport);
	}
	
	@Override
	@Transactional
	public void delete(Long id){
		tjReportDao.delete(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids){
		tjReportDao.deleteBatch(ids);
	}

	@Override
	public TjReportEntity queryByUniqueId(final String uniqueId) {
		return tjReportDao.queryByUniqueId(uniqueId);
	}

	@Override
	public TjReportEntity queryLatestByUserId(final Long userId, final TianjiType tianjiType) {
		return tjReportDao.queryLatestByUserId(userId, tianjiType);
	}

    @Override
    @Transactional
    public void expireReports() {
//        String configStr = sysConfigService.getValue(AuthConstants.AUTH_TYPE_CONFIG);
//        if (StringUtils.isNotBlank(configStr)) {
//            List<AuthConfig> results = new Gson().fromJson(configStr,
//                    new TypeToken<ArrayList<AuthConfig>>(){}.getType());
//            for (AuthConfig config : results) {
//                if (config.getEnabled() == null || config.getEnabled()) {
//                    TianjiType type = TJ_TYPE_MAP.get(config.getAuthTypeName().toUpperCase());
//                    if (type != null) {
//                        int total = tjReportDao.expireReports(type, config.getExpiredDay());
//                        logger.info("Expire TJ report ({}) created before {} days, {} updated",
//                                config.getAuthTypeName(), config.getExpiredDay(), total);
//                    }
//                }
//            }
//        } else {
//            logger.info("No AUTH_TYPE_CONFIG defined");
//        }
    }

	/**
	 * 封装实体
	 * @param jsoninfo
	 * @return
	 */
	public AuthTjMobileVO getTjMobileVo(String jsoninfo){
		AuthTjMobileVO authTjMobileVO=new AuthTjMobileVO();
		JSONObject resp  = JSONObject.parseObject(jsoninfo);
		//1、用户输入信息
		JSONObject  inputinfoObject = resp.getJSONObject("input_info");
		String name = inputinfoObject.getString("name");
		String idCard = inputinfoObject.getString("id_card");
		String phone = inputinfoObject.getString("phone");
		authTjMobileVO.setName(name);
		authTjMobileVO.setIdCard(idCard);
		authTjMobileVO.setPhone(phone);
		//2、紧急联系人
		JSONArray emrgencyInfoArray=inputinfoObject.getJSONArray("emergency_info");
		if(emrgencyInfoArray!=null&&!emrgencyInfoArray.isEmpty()){
			List<EmergencyInfo> emergencyInfos=new ArrayList<EmergencyInfo>();
			for(int i=0;i<emrgencyInfoArray.size();i++) {
				EmergencyInfo emergencyInfo=new EmergencyInfo();
				JSONObject insertObj = emrgencyInfoArray.getJSONObject(i);
				Integer no=insertObj.getInteger("no");
				String namejinji = insertObj.getString("name");
				String phonejinji = insertObj.getString("phone");
				String relation = insertObj.getString("relation");
				if("father".equals(relation)){
					emergencyInfo.setRelation("父亲");
				}else if("mother".equals(relation)){
					emergencyInfo.setRelation("母亲");
				}else if("spouse".equals(relation)){
					emergencyInfo.setRelation("配偶");
				}else if("brother".equals(relation)){
					emergencyInfo.setRelation("兄弟");
				}else if("sister".equals(relation)){
					emergencyInfo.setRelation("姐妹");
				}else if("children".equals(relation)){
					emergencyInfo.setRelation("子女");
				}else if("friend".equals(relation)){
					emergencyInfo.setRelation("朋友");
				}else if("colleague".equals(relation)){
					emergencyInfo.setRelation("同事");
				}else if("other".equals(relation)){
					emergencyInfo.setRelation("其他");
				}
				emergencyInfo.setNo(no);
				emergencyInfo.setName(namejinji);
				emergencyInfo.setPhone(phonejinji);
				emergencyInfos.add(emergencyInfo);
			}
			authTjMobileVO.setEmergency_info(emergencyInfos);
		}
		//基本信息
		JSONObject  basicinfoObject = resp.getJSONObject("basic_info");
		String phoneBasic = basicinfoObject.getString("phone");
		String phoneInfo = basicinfoObject.getString("phone_info");
		String phoneLabel = basicinfoObject.getString("phone_label");
		String operatorZh = basicinfoObject.getString("operator_zh");
		String phoneLocation = basicinfoObject.getString("phone_location");
		String regTime = basicinfoObject.getString("reg_time");
		String operatorAddr = basicinfoObject.getString("operator_addr");
		String operatorName = basicinfoObject.getString("operator_name");
		String operatorIdCard = basicinfoObject.getString("operator_id_card");
		Integer nameCheck = basicinfoObject.getInteger("name_check");
		Integer idCardCheck = basicinfoObject.getInteger("id_card_check");
		Integer reliability = basicinfoObject.getInteger("reliability");
		String gender = basicinfoObject.getString("gender");
		Integer age = basicinfoObject.getInteger("age");
		String province = basicinfoObject.getString("province");
		String city = basicinfoObject.getString("city");
		String region = basicinfoObject.getString("region");
		String constellation = basicinfoObject.getString("constellation");
		Float curBalance = basicinfoObject.getFloat("cur_balance");
		Float monthlyAvgConsumption = basicinfoObject.getFloat("monthly_avg_consumption");
		authTjMobileVO.setBasePhone(phoneBasic);
		authTjMobileVO.setPhone_info(phoneInfo);
		authTjMobileVO.setPhone_label(phoneLabel);
		authTjMobileVO.setOperator_zh(operatorZh);
		authTjMobileVO.setPhone_location(phoneLocation);
		authTjMobileVO.setReg_time(regTime);
		authTjMobileVO.setOperator_addr(operatorAddr);
		authTjMobileVO.setOperator_name(operatorName);
		authTjMobileVO.setOperator_id_card(operatorIdCard);
		authTjMobileVO.setNameCheck(nameCheck);
		authTjMobileVO.setIdCardCheck(idCardCheck);
		authTjMobileVO.setReliability(reliability);
		authTjMobileVO.setGender(gender);
		authTjMobileVO.setAge(age);
		authTjMobileVO.setProvince(province);
		authTjMobileVO.setCity(city);
		authTjMobileVO.setRegion(region);
		authTjMobileVO.setConstellation(constellation);
		authTjMobileVO.setCur_balance(curBalance);
		authTjMobileVO.setMonthly_avg_consumption(monthlyAvgConsumption);
		//风险检测
		JSONObject  riskAnalysisObject = resp.getJSONObject("risk_analysis");
		//联系人逾期情况
		JSONArray contactsOverdueArray=riskAnalysisObject.getJSONArray("contacts_overdue");
		if(contactsOverdueArray!=null&&!contactsOverdueArray.isEmpty()){
			List<ContactsOverdueInfo> contactsOverdues=new ArrayList<ContactsOverdueInfo>();
			for(int i=0;i<contactsOverdueArray.size();i++) {
				ContactsOverdueInfo contactsOverdueInfo=new ContactsOverdueInfo();
				JSONObject insertObj = contactsOverdueArray.getJSONObject(i);
				Integer type=insertObj.getInteger("type");
				Integer hit_cnt = insertObj.getInteger("hit_cnt");
				Integer seconds = insertObj.getInteger("seconds");
				Integer cnt = insertObj.getInteger("cnt");
				contactsOverdueInfo.setType(type);
				contactsOverdueInfo.setHitCnt(hit_cnt);
				contactsOverdueInfo.setCnt(cnt);
				contactsOverdueInfo.setSeconds(seconds);
				contactsOverdues.add(contactsOverdueInfo);
			}
			authTjMobileVO.setContacts_overdue(contactsOverdues);
		}
		//紧急联系人逾期情况
		JSONArray singleOverdueArray=riskAnalysisObject.getJSONArray("single_overdue");
		if(singleOverdueArray!=null&&!singleOverdueArray.isEmpty()){
			List<SingleOverdueInfo> singleOverdue=new ArrayList<SingleOverdueInfo>();
			for(int i=0;i<singleOverdueArray.size();i++) {
				SingleOverdueInfo singleOverdueInfo=new SingleOverdueInfo();
				JSONObject insertObj = singleOverdueArray.getJSONObject(i);
				Integer no=insertObj.getInteger("no");
				Integer hit_cnt = insertObj.getInteger("hit_cnt");
				Integer if_tel = insertObj.getInteger("if_tel");
				Integer if_msg = insertObj.getInteger("if_msg");
				singleOverdueInfo.setHitCnt(hit_cnt);
				singleOverdueInfo.setNo(no);
				singleOverdueInfo.setIf_msg(if_msg);
				singleOverdueInfo.setIfTel(if_tel);
				if(if_tel==null||if_tel==0){
					singleOverdueInfo.setTelRecord("无通话记录");
				}else{
					singleOverdueInfo.setTelRecord("有通话记录");
				}
				if(if_msg==null||if_msg==0){
					singleOverdueInfo.setMsgRecord("无短信记录");
				}else{
					singleOverdueInfo.setMsgRecord("有短信记录");
				}
				singleOverdue.add(singleOverdueInfo);
			}
			authTjMobileVO.setSingleOverdue(singleOverdue);
		}
		BlackCheckInfo blackCheckInfo=new BlackCheckInfo();
		//金融服务类机构黑名单检查(身份证)
		JSONObject financialBlacklistByIdCardObject=riskAnalysisObject.getJSONObject("financial_blacklist_by_id_card");
		if(financialBlacklistByIdCardObject!=null){
			boolean arised=financialBlacklistByIdCardObject.getBoolean("arised");
			blackCheckInfo.setArised(arised);
			JSONArray blackTypeArray=financialBlacklistByIdCardObject.getJSONArray("black_type");
			blackCheckInfo.setNumber(blackTypeArray.size());
			if(blackTypeArray!=null&&!blackTypeArray.isEmpty()) {
				List<String> blackTypes=new ArrayList<String>();
				String str="";
				for(int i=0;i<blackTypeArray.size();i++) {
					String insertObj = blackTypeArray.getString(i);
					blackTypes.add(insertObj);
					if(i==0){
						str=str+insertObj;
					}else{
						str=str+","+insertObj;
					}
				}
				blackCheckInfo.setBlackTypeStr(str);
				blackCheckInfo.setBlackType(blackTypes);
			}
			authTjMobileVO.setFinancialBlacklistByIdCard(blackCheckInfo);
		}
		//金融服务类机构黑名单检查(手机号)
		BlackCheckInfo blackCheckInfoPhone=new BlackCheckInfo();
		JSONObject financialBlacklistByPhoneObject=riskAnalysisObject.getJSONObject("financial_blacklist_by_phone");
		if(financialBlacklistByPhoneObject!=null){
			boolean arisedPhone=financialBlacklistByPhoneObject.getBoolean("arised");
			blackCheckInfoPhone.setArised(arisedPhone);
			JSONArray blackTypeArrayPhone=financialBlacklistByPhoneObject.getJSONArray("black_type");
			blackCheckInfoPhone.setNumber(blackTypeArrayPhone.size());
			if(blackTypeArrayPhone!=null&&!blackTypeArrayPhone.isEmpty()) {
				List<String> blackTypes=new ArrayList<String>();
				String str="";
				for(int i=0;i<blackTypeArrayPhone.size();i++) {
					String insertObj = blackTypeArrayPhone.getString(i);
					blackTypes.add(insertObj);
					if(i==0){
						str=str+insertObj;
					}else{
						str=str+","+insertObj;
					}
				}
				blackCheckInfoPhone.setBlackTypeStr(str);
				blackCheckInfoPhone.setBlackType(blackTypes);
			}
			authTjMobileVO.setFinancialBlacklistByPhone(blackCheckInfoPhone);
		}
		//本人逾期数
		Integer blacklistCnt=riskAnalysisObject.getInteger("blacklist_cnt");
		//本人TJ查询次数
		Integer searchedCnt=riskAnalysisObject.getInteger("searched_cnt");
		//本人尝试申请次数
		Integer applyCnt=riskAnalysisObject.getInteger("apply_cnt");
		authTjMobileVO.setBlacklistCnt(blacklistCnt);
		authTjMobileVO.setSearchedCnt(searchedCnt);
		authTjMobileVO.setApplyCnt(applyCnt);
		//紧急联系人分析
		JSONArray  emergencyAnalysisArray = resp.getJSONArray("emergency_analysis");
		if(emergencyAnalysisArray!=null&&!emergencyAnalysisArray.isEmpty()) {
			List<EmergencyAnalysisInfo> emergencyAnalysis=new ArrayList<EmergencyAnalysisInfo>();
			for(int i=0;i<emergencyAnalysisArray.size();i++) {
				EmergencyAnalysisInfo emergencyAnalysisInfo=new EmergencyAnalysisInfo();
				JSONObject insertObj = emergencyAnalysisArray.getJSONObject(i);
				String phoneEmergency=insertObj.getString("phone");
				String phone_info=insertObj.getString("phone_info");
				String phone_label=insertObj.getString("phone_label");
				String nameEmergency=insertObj.getString("name");
				String last_contact_date=insertObj.getString("last_contact_date");
				if(last_contact_date!=null&&!"".equals(last_contact_date)){
					//截取日期和时间
					String date=last_contact_date.substring(0,10);
					String time=last_contact_date.substring(10);
					emergencyAnalysisInfo.setLastContactDateDate(date);
					emergencyAnalysisInfo.setLastContactDateTime(time);
				}
				Integer talk_seconds = insertObj.getInteger("talk_seconds");
				BigDecimal talk_seconds_decimal = new BigDecimal(Integer.toString(talk_seconds));
				BigDecimal call_seconds_decimal60 = new BigDecimal(60);
				BigDecimal talk_secondsFen=talk_seconds_decimal.divide(call_seconds_decimal60,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt = insertObj.getInteger("call_cnt");
				Integer called_cnt = insertObj.getInteger("called_cnt");
				Integer msg_cnt = insertObj.getInteger("msg_cnt");
				emergencyAnalysisInfo.setPhone(phoneEmergency);
				emergencyAnalysisInfo.setPhoneInfo(phone_info);
				emergencyAnalysisInfo.setPhoneLabel(phone_label);
				emergencyAnalysisInfo.setName(nameEmergency);
				emergencyAnalysisInfo.setLastContactDate(last_contact_date);
				emergencyAnalysisInfo.setTalkSeconds(talk_secondsFen);
				emergencyAnalysisInfo.setCallCnt(call_cnt);
				emergencyAnalysisInfo.setCalledCnt(called_cnt);
				emergencyAnalysisInfo.setMsgCnt(msg_cnt);
				emergencyAnalysis.add(emergencyAnalysisInfo);
			}
			authTjMobileVO.setEmergencyAnalysis(emergencyAnalysis);
		}
		//用户信息检测
		JSONObject  userInfoCheckObject = resp.getJSONObject("user_info_check");
		JSONObject  checkSearchInfoObject = userInfoCheckObject.getJSONObject("check_search_info");
		CheckSearchInfo checkSearchInfo=new CheckSearchInfo();
		Integer searched_org_cnt = checkSearchInfoObject.getInteger("searched_org_cnt");
		checkSearchInfo.setSearchedOrgCnt(searched_org_cnt);
		//查询过该用户的相关企业数量
		JSONArray searchedOrgTypeArray=checkSearchInfoObject.getJSONArray("searched_org_type");
		if(searchedOrgTypeArray!=null&&!searchedOrgTypeArray.isEmpty()) {
			List<String> searchedOrgTypes=new ArrayList<String>();
			for(int i=0;i<searchedOrgTypeArray.size();i++) {
				String insertObj = searchedOrgTypeArray.getString(i);
				searchedOrgTypes.add(insertObj);
			}
			checkSearchInfo.setSearchedOrgType(searchedOrgTypes);
		}
		//身份证组合过的其他姓名
		JSONArray idcardWithOtherNamesArray=checkSearchInfoObject.getJSONArray("idcard_with_other_names");
		if(idcardWithOtherNamesArray!=null&&!idcardWithOtherNamesArray.isEmpty()) {
			List<String> idcardWithOtherNames=new ArrayList<String>();
			String str="";
			for(int i=0;i<idcardWithOtherNamesArray.size();i++) {
				String insertObj = idcardWithOtherNamesArray.getString(i);
				idcardWithOtherNames.add(insertObj);
				if(i==0){
					str=str+insertObj;
				}else{
					str=str+","+insertObj;
				}
			}
			checkSearchInfo.setIdcardWithOtherNamesStr(str);
			checkSearchInfo.setIdcardWithOtherNames(idcardWithOtherNames);
		}
		//身份证组合过其他电话
		JSONArray idcardWithOtherPhonesArray=checkSearchInfoObject.getJSONArray("idcard_with_other_phones");
		if(idcardWithOtherPhonesArray!=null&&!idcardWithOtherPhonesArray.isEmpty()) {
			List<String> idcardWithOtherPhones=new ArrayList<String>();
			String str="";
			for(int i=0;i<idcardWithOtherPhonesArray.size();i++) {
				String insertObj = idcardWithOtherPhonesArray.getString(i);
				idcardWithOtherPhones.add(insertObj);
				if(i==0){
					str=str+insertObj;
				}else{
					str=str+","+insertObj;
				}
			}
			checkSearchInfo.setIdcardWithOtherPhonesStr(str);
			checkSearchInfo.setIdcardWithOtherPhones(idcardWithOtherPhones);
		}
		//电话号码组合过其他姓名
		JSONArray phoneWithOtherNamesArray=checkSearchInfoObject.getJSONArray("phone_with_other_names");
		if(phoneWithOtherNamesArray!=null&&!phoneWithOtherNamesArray.isEmpty()) {
			List<String> phoneWithOtherNames=new ArrayList<String>();
			String str="";
			for(int i=0;i<phoneWithOtherNamesArray.size();i++) {
				String insertObj = phoneWithOtherNamesArray.getString(i);
				phoneWithOtherNames.add(insertObj);
				if(i==0){
					str=str+insertObj;
				}else{
					str=str+","+insertObj;
				}
			}
			checkSearchInfo.setPhoneWithOtherNamesStr(str);
			checkSearchInfo.setPhoneWithOtherNames(phoneWithOtherNames);
		}
		//电话号码组合过其他身份证
		JSONArray phoneWithOtherIdcardsArray=checkSearchInfoObject.getJSONArray("phone_with_other_idcards");
		if(phoneWithOtherIdcardsArray!=null&&!phoneWithOtherIdcardsArray.isEmpty()) {
			List<String> phoneWithOtherIdcards=new ArrayList<String>();
			String str="";
			for(int i=0;i<phoneWithOtherIdcardsArray.size();i++) {
				String insertObj = phoneWithOtherIdcardsArray.getString(i);
				phoneWithOtherIdcards.add(insertObj);
				if(i==0){
					str=str+insertObj;
				}else{
					str=str+","+insertObj;
				}
			}
			checkSearchInfo.setPhoneWithOtherIdcardsStr(str);
			checkSearchInfo.setPhoneWithOtherIdcards(phoneWithOtherIdcards);
		}
		authTjMobileVO.setCheckSearchInfo(checkSearchInfo);
		//用户联系人黑名单信息
		JSONObject  checkBlackInfoObject = userInfoCheckObject.getJSONObject("check_black_info");
		CheckBlackInfo checkBlackInfo=new CheckBlackInfo();
		Integer contacts_class1_blacklist_cnt=checkBlackInfoObject.getInteger("contacts_class1_blacklist_cnt");
		Integer contacts_class1_cnt=checkBlackInfoObject.getInteger("contacts_class1_cnt");
		checkBlackInfo.setContactsClass1BlacklistCnt(contacts_class1_blacklist_cnt);
		checkBlackInfo.setContactsclass1Cnt(contacts_class1_cnt);
		authTjMobileVO.setCheckBlackInfo(checkBlackInfo);
		//用户画像
		JSONObject  userPortraitObject = resp.getJSONObject("user_portrait");
		Integer both_call_cnt=userPortraitObject.getInteger("both_call_cnt");
		JSONObject  contactsDistributionObject = userPortraitObject.getJSONObject("contacts_distribution");
		String area=contactsDistributionObject.getString("area");
		Float ratio=contactsDistributionObject.getFloat("ratio");
		BigDecimal b = new BigDecimal(Float.toString(ratio));
		BigDecimal c = new BigDecimal(Float.toString(100f));
		Float ratioDs=b.multiply(c).floatValue();
		ContactsDistributionInfo contactsDistributionInfo=new ContactsDistributionInfo();
		contactsDistributionInfo.setArea(area);
		contactsDistributionInfo.setRatio(ratioDs);
		authTjMobileVO.setBothCallCnt(both_call_cnt);
		authTjMobileVO.setContactsDistribution(contactsDistributionInfo);
		//静默天数分析
		JSONObject  silentDaysObject = userPortraitObject.getJSONObject("silent_days");
		SilentDaysInfo silentDaysInfo=new SilentDaysInfo();
		Integer max_interval=silentDaysObject.getInteger("max_interval");
		Float monthly_avg_days=silentDaysObject.getFloat("monthly_avg_days");
		JSONArray max_detail=silentDaysObject.getJSONArray("max_detail");
		if(max_detail!=null&&!max_detail.isEmpty()) {
			String str="";
			List<String> max_details=new ArrayList<String>();
			for(int i=0;i<max_detail.size();i++) {
				String insertObj = max_detail.getString(i);
				max_details.add(insertObj);
				if(i==0){
					str=str+insertObj;
				}else{
					str=str+","+insertObj;
				}
			}
			silentDaysInfo.setMaxDetailStr(str);
			silentDaysInfo.setMaxDetail(max_details);
		}
		silentDaysInfo.setMaxInterval(max_interval);
		silentDaysInfo.setMonthlyAvgDays(monthly_avg_days);
		authTjMobileVO.setSilentDays(silentDaysInfo);
		//夜间活动情况
		JSONObject  nightActivitiesObject = userPortraitObject.getJSONObject("night_activities");
		NightActivitiesInfo nightActivitiesInfo=new NightActivitiesInfo();
		Float monthly_avg_seconds=nightActivitiesObject.getFloat("monthly_avg_seconds");
		BigDecimal monthly_avg_seconds_decimal = new BigDecimal(Float.toString(monthly_avg_seconds));
		BigDecimal monthly_avg_seconds_decimal60 = new BigDecimal(Float.toString(60f));
		Integer monthly_avg_seconds_fen=monthly_avg_seconds_decimal.divide(monthly_avg_seconds_decimal60,0,BigDecimal.ROUND_HALF_UP).intValue();

		Float monthly_avg_seconds_ratio=nightActivitiesObject.getFloat("monthly_avg_seconds_ratio");
		BigDecimal monthly_avg_seconds_ratio_decimal = new BigDecimal(Float.toString(monthly_avg_seconds_ratio));
		BigDecimal monthly_avg_seconds_ratio_decimal100 = new BigDecimal(Float.toString(100f));
		Float monthly_avg_seconds_ratio_fen=monthly_avg_seconds_ratio_decimal.multiply(monthly_avg_seconds_ratio_decimal100).floatValue();

		Float night_talk_seconds_ratio=nightActivitiesObject.getFloat("night_talk_seconds_ratio");
		BigDecimal night_talk_seconds_ratio_decimal = new BigDecimal(Float.toString(night_talk_seconds_ratio));
		BigDecimal night_talk_seconds_ratio_decimal100 = new BigDecimal(Float.toString(100f));
		Float night_talk_seconds_fen=night_talk_seconds_ratio_decimal.multiply(night_talk_seconds_ratio_decimal100).floatValue();

		Float night_msg_ratio=nightActivitiesObject.getFloat("night_msg_ratio");
		BigDecimal night_msg_ratio_decimal = new BigDecimal(Float.toString(night_msg_ratio));
		BigDecimal night_msg_ratio_decimal100 = new BigDecimal(Float.toString(100f));
		Float night_msg_ratio_fen=night_msg_ratio_decimal.multiply(night_msg_ratio_decimal100).floatValue();

		nightActivitiesInfo.setMonthlyAvgSeconds(monthly_avg_seconds_fen);
		nightActivitiesInfo.setMonthlyAvgSecondsRatio(monthly_avg_seconds_ratio_fen);
		nightActivitiesInfo.setNightTalkSecondsRatio(night_talk_seconds_fen);
		nightActivitiesInfo.setNightMsgRatio(night_msg_ratio_fen);
		authTjMobileVO.setNightActivities(nightActivitiesInfo);
		//特殊号码类别
		JSONArray  specialCateArray = resp.getJSONArray("special_cate");
		if(specialCateArray!=null&&!specialCateArray.isEmpty()) {
			List<SpecialCateInfo> specialCates=new ArrayList<SpecialCateInfo>();
			List<SpecialCateInfo> specialCatesEchart=new ArrayList<SpecialCateInfo>();
			for(int i=0;i<specialCateArray.size();i++) {
				SpecialCateInfo specialCateInfo=new SpecialCateInfo();
				JSONObject insertObj = specialCateArray.getJSONObject(i);
				String cate=insertObj.getString("cate");
				Integer talk_cnt=insertObj.getInteger("talk_cnt");
				Integer talk_seconds=insertObj.getInteger("talk_seconds");
				BigDecimal talk_seconds_decimal = new BigDecimal(Float.toString(talk_seconds));
				BigDecimal call_seconds_decimal60 = new BigDecimal(60);
				BigDecimal talk_secondsFen=talk_seconds_decimal.divide(call_seconds_decimal60,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Float.toString(call_seconds));
				BigDecimal ratioFen=call_seconds_decimal.divide(call_seconds_decimal60,2,BigDecimal.ROUND_HALF_UP);

				Integer called_cnt=insertObj.getInteger("called_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Float.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(call_seconds_decimal60,2,BigDecimal.ROUND_HALF_UP);

				Integer msg_cnt=insertObj.getInteger("msg_cnt");
				Integer send_cnt=insertObj.getInteger("send_cnt");
				Integer receive_cnt=insertObj.getInteger("receive_cnt");
				//封装echart数据
				if(talk_cnt>0){
					SpecialCateInfo specialCateInfoEchart=new SpecialCateInfo();
					specialCateInfoEchart.setTalkCnt(talk_cnt);
					specialCateInfoEchart.setTalkSeconds(talk_secondsFen);
					specialCateInfoEchart.setCate(cate);
					specialCatesEchart.add(specialCateInfoEchart);
				}
				specialCateInfo.setCate(cate);
				specialCateInfo.setTalkCnt(talk_cnt);
				specialCateInfo.setTalkSeconds(talk_secondsFen);
				specialCateInfo.setCallCnt(call_cnt);
				specialCateInfo.setCallSeconds(ratioFen);
				specialCateInfo.setCalledCnt(called_cnt);
				specialCateInfo.setCalledSeconds(called_seconds_fen);
				specialCateInfo.setMsgCnt(msg_cnt);
				specialCateInfo.setSendCnt(send_cnt);
				specialCateInfo.setReceiveCnt(receive_cnt);
				//通话详情
				JSONArray  phoneDetailArray =insertObj.getJSONArray("phone_detail");
				if(phoneDetailArray!=null&&!phoneDetailArray.isEmpty()) {
					List<SpecialCateSonInfo> specialCateSonInfos = new ArrayList<SpecialCateSonInfo>();
					for (int j = 0; j < phoneDetailArray.size(); j++) {
						SpecialCateSonInfo specialCateSonInfo=new SpecialCateSonInfo();
						JSONObject phoneDetailObj = phoneDetailArray.getJSONObject(j);
						Integer called_cnt_son=phoneDetailObj.getInteger("called_cnt");
						Integer talk_seconds_son=phoneDetailObj.getInteger("talk_seconds");
						BigDecimal talk_seconds_son_decimal = new BigDecimal(Float.toString(talk_seconds_son));
						BigDecimal decimal60 = new BigDecimal(60);
						BigDecimal talk_seconds_son_fen=talk_seconds_son_decimal.divide(decimal60,2,BigDecimal.ROUND_HALF_UP);

						Integer talk_cnt_son=phoneDetailObj.getInteger("talk_cnt");
						String phone_info_son=phoneDetailObj.getString("phone_info");
						Integer receive_cnt_son=phoneDetailObj.getInteger("receive_cnt");
						Integer called_seconds_son=phoneDetailObj.getInteger("called_seconds");
						BigDecimal called_seconds_son_decimal = new BigDecimal(Float.toString(called_seconds_son));
						BigDecimal called_seconds_son_fen=called_seconds_son_decimal.divide(decimal60,2,BigDecimal.ROUND_HALF_UP);

						Integer msg_cnt_son=phoneDetailObj.getInteger("msg_cnt");
						Integer call_cnt_son=phoneDetailObj.getInteger("call_cnt");
						String phone_son=phoneDetailObj.getString("phone");
						Integer send_cnt_son=phoneDetailObj.getInteger("send_cnt");
						Integer call_seconds_son=phoneDetailObj.getInteger("call_seconds");
						BigDecimal call_seconds_son_decimal = new BigDecimal(Float.toString(call_seconds_son));
						BigDecimal call_seconds_son_fen=call_seconds_son_decimal.divide(decimal60,2,BigDecimal.ROUND_HALF_UP);

						specialCateSonInfo.setCallCnt(call_cnt_son);
						specialCateSonInfo.setCalledCnt(called_cnt_son);
						specialCateSonInfo.setTalkSeconds(talk_seconds_son_fen);
						specialCateSonInfo.setTalkCnt(talk_cnt_son);
						specialCateSonInfo.setPhoneInfo(phone_info_son);
						specialCateSonInfo.setReceiveCnt(receive_cnt_son);
						specialCateSonInfo.setCalledSeconds(called_seconds_son_fen);
						specialCateSonInfo.setMsgCnt(msg_cnt_son);
						specialCateSonInfo.setPhone(phone_son);
						specialCateSonInfo.setSendCnt(send_cnt_son);
						specialCateSonInfo.setCallSeconds(call_seconds_son_fen);
						specialCateSonInfos.add(specialCateSonInfo);
					}
					specialCateInfo.setSpecialCateSonInfos(specialCateSonInfos);
				}
				specialCates.add(specialCateInfo);
			}
			authTjMobileVO.setSpecialCate(specialCates);
			authTjMobileVO.setSpecialCateEchart(specialCatesEchart);
		}
		//联系人地区分析
		JSONArray  contactAreaAnalysisArray = resp.getJSONArray("contact_area_analysis");
		if(contactAreaAnalysisArray!=null&&!contactAreaAnalysisArray.isEmpty()) {
			List<ContactAreaAnalysisInfo> contactAreaAnalysis = new ArrayList<ContactAreaAnalysisInfo>();
			Integer taotal=0;
			//计算总联系人数量
			for (int i = 0; i < contactAreaAnalysisArray.size(); i++) {
				JSONObject insertObj = contactAreaAnalysisArray.getJSONObject(i);
				Integer contact_phone_cnt=insertObj.getInteger("contact_phone_cnt");
				taotal=taotal+contact_phone_cnt;
			}
			for (int i = 0; i < contactAreaAnalysisArray.size(); i++) {
				ContactAreaAnalysisInfo contactAreaAnalysisInfo = new ContactAreaAnalysisInfo();
				JSONObject insertObj = contactAreaAnalysisArray.getJSONObject(i);
				String areaContact=insertObj.getString("area");
				Integer contact_phone_cnt=insertObj.getInteger("contact_phone_cnt");
				BigDecimal contact_phone_cnt_decimal = new BigDecimal(Integer.toString(contact_phone_cnt));
				BigDecimal taotal_decimal = new BigDecimal(Integer.toString(taotal));
				BigDecimal contactRadio=contact_phone_cnt_decimal.divide(taotal_decimal,4,BigDecimal.ROUND_HALF_UP);

				Integer talk_seconds=insertObj.getInteger("talk_seconds");
				BigDecimal talk_seconds_decimal = new BigDecimal(Integer.toString(talk_seconds));
				BigDecimal fen_decimal = new BigDecimal(60);
				BigDecimal talk_seconds_fen=talk_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer talk_cnt=insertObj.getInteger("talk_cnt");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Integer.toString(call_seconds));
				BigDecimal call_seconds_fen=call_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Integer.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer called_cnt=insertObj.getInteger("called_cnt");
				Integer msg_cnt=insertObj.getInteger("msg_cnt");
				Integer send_cnt=insertObj.getInteger("send_cnt");
				Integer receive_cnt=insertObj.getInteger("receive_cnt");
				contactAreaAnalysisInfo.setArea(areaContact);
				contactAreaAnalysisInfo.setContactPhoneCnt(contact_phone_cnt);
				contactAreaAnalysisInfo.setContactRadio(contactRadio.multiply(new BigDecimal(100)));
				contactAreaAnalysisInfo.setTalkSeconds(talk_seconds_fen);
				contactAreaAnalysisInfo.setTalkCnt(talk_cnt);
				contactAreaAnalysisInfo.setCalledSeconds(called_seconds_fen);
				contactAreaAnalysisInfo.setCallSeconds(call_seconds_fen);
				contactAreaAnalysisInfo.setCallCnt(call_cnt);
				contactAreaAnalysisInfo.setCalledCnt(called_cnt);
				contactAreaAnalysisInfo.setMsgCnt(msg_cnt);
				contactAreaAnalysisInfo.setSendCnt(send_cnt);
				contactAreaAnalysisInfo.setReceiveCnt(receive_cnt);
				contactAreaAnalysis.add(contactAreaAnalysisInfo);
			}
			authTjMobileVO.setContactAreaAnalysis(contactAreaAnalysis);
			//封装echart图表数据
			if(contactAreaAnalysis.size()>=10){
				authTjMobileVO.setContactAreaAnalysisEchartTop10(contactAreaAnalysis.subList(0,10));
			}else{
				authTjMobileVO.setContactAreaAnalysisEchartTop10(contactAreaAnalysis);
			}
		}
		//运营商月消费
		JSONArray  monthlyConsumptionArray = resp.getJSONArray("monthly_consumption");
		if(monthlyConsumptionArray!=null&&!monthlyConsumptionArray.isEmpty()) {
			List<MonthlyConsumptionInfo> monthlyConsumption = new ArrayList<MonthlyConsumptionInfo>();
			for (int i = 0; i < monthlyConsumptionArray.size(); i++) {
				MonthlyConsumptionInfo monthlyConsumptionInfo = new MonthlyConsumptionInfo();
				JSONObject insertObj = monthlyConsumptionArray.getJSONObject(i);
				String month=insertObj.getString("month");
				Integer talk_seconds=insertObj.getInteger("talk_seconds");
				Integer talk_cnt=insertObj.getInteger("talk_cnt");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Integer.toString(call_seconds));
				BigDecimal fen_decimal = new BigDecimal(60);
				BigDecimal call_seconds_fen=call_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Integer.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer called_cnt=insertObj.getInteger("called_cnt");
				Integer msg_cnt=insertObj.getInteger("msg_cnt");
				Integer send_cnt=insertObj.getInteger("send_cnt");
				Integer receive_cnt=insertObj.getInteger("receive_cnt");
				Float net_flow=insertObj.getFloat("net_flow");
				BigDecimal net_flow_decimal = new BigDecimal(Float.toString(net_flow)).setScale(2,BigDecimal.ROUND_HALF_UP);
				Float call_consumption=insertObj.getFloat("call_consumption");
				if(call_consumption==null){
					call_consumption=new Float(0);
				}
				BigDecimal call_consumption_decimal = new BigDecimal(Float.toString(call_consumption)).setScale(2,BigDecimal.ROUND_HALF_UP);

				monthlyConsumptionInfo.setMonth(month);
				monthlyConsumptionInfo.setTalkSeconds(talk_seconds);
				monthlyConsumptionInfo.setTalkCnt(talk_cnt);
				monthlyConsumptionInfo.setCallSeconds(call_seconds_fen);
				monthlyConsumptionInfo.setCallCnt(call_cnt);
				monthlyConsumptionInfo.setCallCnt(call_cnt);
				monthlyConsumptionInfo.setCalledSeconds(called_seconds_fen);
				monthlyConsumptionInfo.setCalledCnt(called_cnt);
				monthlyConsumptionInfo.setMsgCnt(msg_cnt);
				monthlyConsumptionInfo.setSendCnt(send_cnt);
				monthlyConsumptionInfo.setReceiveCnt(receive_cnt);
				monthlyConsumptionInfo.setNetFlow(net_flow_decimal);
				monthlyConsumptionInfo.setCallConsumption(call_consumption_decimal);
				monthlyConsumption.add(monthlyConsumptionInfo);
			}
			authTjMobileVO.setMonthlyConsumption(monthlyConsumption);
		}
		//通话地区分析
		JSONArray  telAreaAnalysisArray = resp.getJSONArray("tel_area_analysis");
		if(telAreaAnalysisArray!=null&&!telAreaAnalysisArray.isEmpty()) {
			List<TelAreaAnalysisInfo> telAreaAnalysis = new ArrayList<TelAreaAnalysisInfo>();
			for (int i = 0; i < telAreaAnalysisArray.size(); i++) {
				TelAreaAnalysisInfo telAreaAnalysisInfo = new TelAreaAnalysisInfo();
				JSONObject insertObj = telAreaAnalysisArray.getJSONObject(i);
				String areaTelArea=insertObj.getString("area");
				Integer talk_seconds=insertObj.getInteger("talk_seconds");
				BigDecimal talk_seconds_decimal = new BigDecimal(Integer.toString(talk_seconds));
				BigDecimal fen_decimal = new BigDecimal(60);
				BigDecimal talk_seconds_fen=talk_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer talk_cnt=insertObj.getInteger("talk_cnt");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Integer.toString(call_seconds));
				BigDecimal call_seconds_fen=call_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Integer.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer called_cnt=insertObj.getInteger("called_cnt");
				telAreaAnalysisInfo.setArea(areaTelArea);
				telAreaAnalysisInfo.setTalkSeconds(talk_seconds_fen);
				telAreaAnalysisInfo.setTalkCnt(talk_cnt);
				telAreaAnalysisInfo.setCallSeconds(call_seconds_fen);
				telAreaAnalysisInfo.setCallCnt(call_cnt);
				telAreaAnalysisInfo.setCalledSeconds(called_seconds_fen);
				telAreaAnalysisInfo.setCalledCnt(called_cnt);
				telAreaAnalysis.add(telAreaAnalysisInfo);
			}
			authTjMobileVO.setTelAreaAnalysis(telAreaAnalysis);
			//封装echart图表数据
			if(telAreaAnalysis.size()>=10){
				authTjMobileVO.setTelAreaAnalysisTop10(telAreaAnalysis.subList(0,10));
			}else{
				authTjMobileVO.setTelAreaAnalysisTop10(telAreaAnalysis);
			}
		}
		//通话记录
		JSONArray  callLogArray = resp.getJSONArray("call_log");
		if(callLogArray!=null&&!callLogArray.isEmpty()) {
			List<CallLogInfo> callLogs = new ArrayList<CallLogInfo>();
			for (int i = 0; i < callLogArray.size(); i++) {
				CallLogInfo callLogInfo = new CallLogInfo();
				JSONObject insertObj = callLogArray.getJSONObject(i);
				String phoneAnalysis=insertObj.getString("phone");
				String phone_info=insertObj.getString("phone_info");
				String phone_label=insertObj.getString("phone_label");
				String phone_location=insertObj.getString("phone_location");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Integer.toString(call_seconds));
				BigDecimal fen_decimal = new BigDecimal(60);
				BigDecimal call_seconds_fen=call_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Integer.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer called_cnt=insertObj.getInteger("called_cnt");
				Integer send_cnt=insertObj.getInteger("send_cnt");
				Integer receive_cnt=insertObj.getInteger("receive_cnt");
				callLogInfo.setPhone(phoneAnalysis);
				callLogInfo.setPhoneInfo(phone_info);
				callLogInfo.setPhoneLabel(phone_label);
				callLogInfo.setPhoneLocation(phone_location);
				callLogInfo.setCalledSeconds(called_seconds_fen);
				callLogInfo.setCallSeconds(call_seconds_fen);
				callLogInfo.setCallCnt(call_cnt);
				callLogInfo.setCalledCnt(called_cnt);
				callLogInfo.setSendCnt(send_cnt);
				callLogInfo.setReceiveCnt(receive_cnt);
				callLogs.add(callLogInfo);
			}
			authTjMobileVO.setCallLog(callLogs);
		}
		//通讯城市分析
		JSONArray  tripInfoArray = resp.getJSONArray("trip_info");
		if(tripInfoArray!=null&&!tripInfoArray.isEmpty()) {
			List<TripInfo> tripInfos = new ArrayList<TripInfo>();
			for (int i = 0; i < tripInfoArray.size(); i++) {
				TripInfo tripInfo = new TripInfo();
				JSONObject insertObj = tripInfoArray.getJSONObject(i);
				String trip_leave =insertObj.getString("trip_leave ");
				String trip_dest =insertObj.getString("trip_dest ");
				String trip_type =insertObj.getString("trip_type ");
				tripInfo.setTripDest(trip_dest);
				tripInfo.setTripLeave(trip_leave);
				tripInfo.setTripType(trip_type);
				tripInfos.add(tripInfo);
			}
			authTjMobileVO.setTripInfo(tripInfos);
		}
		return authTjMobileVO;
	}
	//通话详情
	public AuthTjMobileVO getTjCallogVo(String jsoninfo,AuthUserEntity authUser,String mobileRawReport){
		Map<String, Object> result = new HashMap<>();
		AuthTjMobileVO authTjMobileVO=new AuthTjMobileVO();
		JSONObject resp  = JSONObject.parseObject(jsoninfo);
        //查询该用户的通讯录并且和通话记录匹配姓名
        String contact = "";
        Map<String, String> contactMap=null;
        UserEntity contactUser = userService.queryByMobile(authUser.getMobile());
        if (contactUser != null) {
            contact = contactUser.getContact();
        }
        if (StringUtils.isBlank(contact)) {
            AuthUserContactEntity contactEntity = authUserContactDao.queryObject(authUser.getUserId());

            if (contactEntity != null) {
                contact = contactEntity.getContact();
            }
        }
        if(StringUtils.isNotBlank(contact)){
            contactMap =new  Gson().fromJson(contact, HashMap.class);
        }
		//1、用户输入信息
		JSONObject  inputinfoObject = resp.getJSONObject("input_info");
		String name = inputinfoObject.getString("name");
		String idCard = inputinfoObject.getString("id_card");
		String phone = inputinfoObject.getString("phone");
		authTjMobileVO.setName(name);
		authTjMobileVO.setIdCard(idCard);
		authTjMobileVO.setPhone(phone);
		//2、紧急联系人
		JSONArray emrgencyInfoArray=inputinfoObject.getJSONArray("emergency_info");
        List<EmergencyInfo> emergencyInfos=new ArrayList<EmergencyInfo>();
		if(emrgencyInfoArray!=null&&!emrgencyInfoArray.isEmpty()){
			for(int i=0;i<emrgencyInfoArray.size();i++) {
				EmergencyInfo emergencyInfo=new EmergencyInfo();
				JSONObject insertObj = emrgencyInfoArray.getJSONObject(i);
				Integer no=insertObj.getInteger("no");
				String namejinji = insertObj.getString("name");
				String phonejinji = insertObj.getString("phone");
				String relation = insertObj.getString("relation");
				if("father".equals(relation)){
					emergencyInfo.setRelation("父亲");
				}else if("mother".equals(relation)){
					emergencyInfo.setRelation("母亲");
				}else if("spouse".equals(relation)){
					emergencyInfo.setRelation("配偶");
				}else if("brother".equals(relation)){
					emergencyInfo.setRelation("兄弟");
				}else if("sister".equals(relation)){
					emergencyInfo.setRelation("姐妹");
				}else if("children".equals(relation)){
					emergencyInfo.setRelation("子女");
				}else if("friend".equals(relation)){
					emergencyInfo.setRelation("朋友");
				}else if("colleague".equals(relation)){
					emergencyInfo.setRelation("同事");
				}else if("other".equals(relation)){
					emergencyInfo.setRelation("其他");
				}
				emergencyInfo.setNo(no);
				emergencyInfo.setName(namejinji);
				emergencyInfo.setPhone(phonejinji);
				emergencyInfos.add(emergencyInfo);
			}
			authTjMobileVO.setEmergency_info(emergencyInfos);
		}

		//通话记录
		JSONArray  callLogArray = resp.getJSONArray("call_log");
		if(callLogArray!=null&&!callLogArray.isEmpty()) {
			List<CallLogInfo> callLogs = new ArrayList<CallLogInfo>();
			for (int i = 0; i < callLogArray.size(); i++) {
				CallLogInfo callLogInfo = new CallLogInfo();
				JSONObject insertObj = callLogArray.getJSONObject(i);
				String phoneAnalysis=insertObj.getString("phone");
				String phone_info=insertObj.getString("phone_info");
				String phone_label=insertObj.getString("phone_label");
				String phone_location=insertObj.getString("phone_location");
				Integer call_seconds=insertObj.getInteger("call_seconds");
				BigDecimal call_seconds_decimal = new BigDecimal(Integer.toString(call_seconds));
				BigDecimal fen_decimal = new BigDecimal(60);
				BigDecimal call_seconds_fen=call_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer call_cnt=insertObj.getInteger("call_cnt");
				Integer called_seconds=insertObj.getInteger("called_seconds");
				BigDecimal called_seconds_decimal = new BigDecimal(Integer.toString(called_seconds));
				BigDecimal called_seconds_fen=called_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer talk_seconds=insertObj.getInteger("talk_seconds");
				BigDecimal talk_seconds_decimal = new BigDecimal(Integer.toString(talk_seconds));
				BigDecimal talk_seconds_fen=talk_seconds_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP);

				Integer talk_cnt=insertObj.getInteger("talk_cnt");
				Integer called_cnt=insertObj.getInteger("called_cnt");
				Integer send_cnt=insertObj.getInteger("send_cnt");
				Integer receive_cnt=insertObj.getInteger("receive_cnt");

				//通讯录不为空的情况下匹配手机号
				if(contactMap!=null){
					String personName=contactMap.get(phoneAnalysis);
                    if(personName!=null&&!"".equals(personName)){
                        callLogInfo.setPersonName(personName);
                    }else{
                        callLogInfo.setPersonName("-");
                    }
                    if(emergencyInfos!=null&&!emergencyInfos.isEmpty()){
                        for(EmergencyInfo emergencyInfo:emergencyInfos){
                        	if(phoneAnalysis.equals(emergencyInfo.getPhone())){
								callLogInfo.setJinjiPerson("紧急联系人号码");
								break;
							}
                        }
                    }
				}

				callLogInfo.setPhone(phoneAnalysis);
				callLogInfo.setPhoneInfo(phone_info);
				callLogInfo.setPhoneLabel(phone_label);
				callLogInfo.setPhoneLocation(phone_location);
				callLogInfo.setTalkSeconds(talk_seconds_fen);
				callLogInfo.setTalkCnt(talk_cnt);
				callLogInfo.setCalledSeconds(called_seconds_fen);
				callLogInfo.setCallSeconds(call_seconds_fen);
				callLogInfo.setCallCnt(call_cnt);
				callLogInfo.setCalledCnt(called_cnt);
				callLogInfo.setSendCnt(send_cnt);
				callLogInfo.setReceiveCnt(receive_cnt);
				callLogs.add(callLogInfo);
			}
			authTjMobileVO.setCallLog(callLogs.subList(0,Math.min(10, callLogs.size() - 1)));
		}
		//通话记录原始数据
		JSONObject mobileRawResp  = JSONObject.parseObject(mobileRawReport);
		JSONArray dataListArray=mobileRawResp.getJSONArray("data_list");
		List<AuthTjCallLogsVO> authTjCallLogsVOs=new ArrayList<AuthTjCallLogsVO>();
		List<AuthTjCallLogVO> callLogsList=new ArrayList<AuthTjCallLogVO>();
		if(dataListArray!=null&&!dataListArray.isEmpty()){
			for(int i=0;i<dataListArray.size();i++) {
				JSONObject dataObject= dataListArray.getJSONObject(i);
				JSONArray teldataArray=dataObject.getJSONArray("teldata");
				if(teldataArray!=null&&!teldataArray.isEmpty()) {
					for (int j = 0; j < teldataArray.size(); j++) {
						AuthTjCallLogsVO authTjCallLogsVO=new AuthTjCallLogsVO();
						JSONObject insertObj= teldataArray.getJSONObject(j);
						String month=insertObj.getString("month");
						Integer totalSize=insertObj.getInteger("insertObj");
						JSONArray itemsArray=insertObj.getJSONArray("items");
						if(itemsArray!=null&&!itemsArray.isEmpty()) {
							List<AuthTjCallLogVO> authTjCallLogVOs=new ArrayList<AuthTjCallLogVO>();
							for (int k = 0; k < itemsArray.size(); k++) {
								JSONObject itemsObject= itemsArray.getJSONObject(k);
								AuthTjCallLogVO authTjCallLogVO=new AuthTjCallLogVO();
								String business_name=itemsObject.getString("business_name");
								String call_time=itemsObject.getString("call_time");
								String call_type=itemsObject.getString("call_type");
								String fee=itemsObject.getString("fee");
								String special_offer=itemsObject.getString("special_offer");
								String trade_addr=itemsObject.getString("trade_addr");
								String trade_time=itemsObject.getString("trade_time");
								BigDecimal trade_time_decimal = new BigDecimal(StringUtils.defaultIfBlank(trade_time,"0"));
								BigDecimal fen_decimal = new BigDecimal(60);
								String trade_time_fen=trade_time_decimal.divide(fen_decimal,2,BigDecimal.ROUND_HALF_UP).toString();


								String trade_type=itemsObject.getString("trade_type");
								String receive_phone=itemsObject.getString("receive_phone");
								if(contactMap!=null){
									String personName=contactMap.get(receive_phone);
									if(personName!=null&&!"".equals(personName)){
										authTjCallLogVO.setContactName(personName);
									}else{
										authTjCallLogVO.setContactName("-");
									}
								}
								authTjCallLogVO.setBusiness_name(business_name);
								authTjCallLogVO.setCall_time(call_time);
								authTjCallLogVO.setCall_type(call_type);
								authTjCallLogVO.setFee(fee);
								authTjCallLogVO.setSpecial_offer(special_offer);
								authTjCallLogVO.setTrade_addr(trade_addr);
								authTjCallLogVO.setTrade_time(trade_time_fen);
								authTjCallLogVO.setTrade_type(trade_type);
								authTjCallLogVO.setReceive_phone(receive_phone);
								authTjCallLogVOs.add(authTjCallLogVO);
								callLogsList.add(authTjCallLogVO);
							}
							authTjCallLogsVO.setAuthTjCallLogs(authTjCallLogVOs);
						}
						authTjCallLogsVO.setMonth(month);
						authTjCallLogsVO.setTotal_size(totalSize);
						authTjCallLogsVOs.add(authTjCallLogsVO);
					}
				}
			}
		}
		authTjMobileVO.setAuthTjCallLogsList(authTjCallLogsVOs);
		Collections.reverse(callLogsList);
		authTjMobileVO.setCallLogsList(callLogsList);
		return authTjMobileVO;
	}
	
	@Override
	public TjReportEntity querySuccessReportByTaskId(String taskId,TianjiType tianjiType) {
		return this.tjReportDao.querySuccessReportByTaskId(taskId,tianjiType);
	}
}
