package io.grx.auth.service.impl;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import io.grx.auth.service.TianjiService;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.enums.MobileReNameCheck;
import net.sf.json.JSONArray;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rong360.tianji.sample.OpenapiClient;
import com.rong360.tianji.utils.RequestUtil;
import io.grx.modules.auth.enums.ContactType;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.TjReportService;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import net.sf.json.JSONObject;

@Service
public class TianjiServiceImpl implements TianjiService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${tianji.appId}")
    private String appId;
    @Value("${tianji.privateKey}")
    private String privateKey;
    @Value("${tianji.userPrefix}")
    private String userPrefix;
    @Value("${tianji.reportPath}")
    private String reportPath;
    @Value("${tianji.host}")
    private String host;

    @Value("${tianji.mobilePath}")
    private String mobilePath;
    @Value("${tianji.mobileHost}")
    private String mobileHost;

    @Value("${tianji.storageBucket}")
    private String bucket;

    @Autowired
    private TjReportService tjReportService;

    private CloudStorageService cloudStorageService;

    @Override
    public R collectUser(final TianjiType tianjiType) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先完善基本信息");
        }
//        if (StringUtils.isBlank(user.getContact1Mobile())) {
//            return R.error("请先完善紧急联系人信息");
//        }

        TjReportEntity lastReport = tjReportService.queryLatestByUserId(user.getUserId(), tianjiType);
        if (lastReport != null) {
            if (lastReport.getVerifyStatus() == VerifyStatus.SUBMITTED
                    || lastReport.getVerifyStatus() == VerifyStatus.PROCESSING) {
                return R.error("你已提交认证，请稍后刷新查看结果");
            }

            if (lastReport.getVerifyStatus() == VerifyStatus.SUCCESS && !lastReport.isExpired()) {
                return R.error("你已认证成功，请刷新查看最新结果");
            }
        }

        Map<String, String> params = new HashMap<>();

        params.put("type", tianjiType.name().toLowerCase());
        params.put("outUniqueId", UUIDGenerator.getUUID());
        params.put("name", user.getName());
        params.put("phone", user.getMobile());
        params.put("idNumber", user.getIdNo());
        params.put("userId", userPrefix + user.getUserId());

        if (tianjiType == TianjiType.MOBILE) {
            // 联系人信息
        	if(StringUtils.isNotEmpty(user.getContact1Mobile())) {
        		params.put("emergencyName1", user.getContact1Name());
                params.put("emergencyPhone1", user.getContact1Mobile().replaceAll(" ", ""));
                String contact1Relation = "";
                if (user.getContact1Type() == ContactType.SPOUSE) {
                    contact1Relation = "spouse";
                } else if (user.getContact1Type() == ContactType.PARENT) {
                    contact1Relation = "father";
                } else if (user.getContact1Type() == ContactType.RELATIVE) {
                    contact1Relation = "brother";
                }
                params.put("emergencyRelation1", contact1Relation);
        	}
            

            if(StringUtils.isNotEmpty(user.getContact2Mobile())) {
            	params.put("emergencyName2", user.getContact2Name());
                params.put("emergencyPhone2", user.getContact2Mobile().replaceAll(" ", ""));

                String contact2Relation = "";
                if (user.getContact2Type() == ContactType.SPOUSE) {
                    contact2Relation = "spouse";
                } else if (user.getContact2Type() == ContactType.PARENT) {
                    contact2Relation = "father";
                } else if (user.getContact2Type() == ContactType.RELATIVE) {
                    contact2Relation = "brother";
                }
                params.put("emergencyRelation2", contact2Relation);
            }

            if(StringUtils.isNotEmpty(user.getContact3Mobile())){
            	params.put("emergencyName3", user.getContact3Name());
                params.put("emergencyPhone3", user.getContact3Mobile().replaceAll(" ", ""));
                String contact3Relation = "";
                if (user.getContact1Type() == ContactType.SPOUSE) {
                    contact3Relation = "spouse";
                } else if (user.getContact1Type() == ContactType.PARENT) {
                    contact3Relation = "father";
                } else if (user.getContact1Type() == ContactType.RELATIVE) {
                    contact3Relation = "brother";
                }
                params.put("emergencyRelation3", contact3Relation);
            }
        }

        String urlBase = HttpContextUtils.getRequestBaseUrl();
        params.put("returnUrl", urlBase + "/tj/return/" + tianjiType.name());
        params.put("notifyUrl", urlBase + "/tj/callback/" + tianjiType.name());

        try {
            Map<String, Object> response;
            if (tianjiType == TianjiType.MOBILE && StringUtils.isNotBlank(mobileHost)) {
                response = postJsonData(mobileHost, mobilePath, params);
            } else {
                response = postJsonData(host, reportPath, params);
            }

            int code = MapUtils.getIntValue(response, "error", -1);
            if (code == 200) {
                Map<String, Object> result = MapUtils.getMap(response, "result");
                return R.ok().put("url", MapUtils.getString(result, "redirectUrl"));
            } else {
                String msg = MapUtils.getString(response, "msg");
                return R.error(msg);
            }

        } catch (Exception e) {
            logger.error("Failed to collectUser", e);
        }
        return R.error("未知错误, 请联系客服");
    }

    @Override
    public void downloadReport(final TjReportEntity reportEntity) {
        OpenapiClient sample = new OpenapiClient();

        sample.setAppId(appId); // TODO 设置Appid
        sample.setPrivateKey(privateKey); // TODO 设置机构私钥，需要使用方替换private_key.pem文件
        sample.setIsTestEnv(false); // TODO 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
        sample.setPrintLog(false);
        sample.setLogid(RequestUtil.generateLogid());

        sample.setMethod("tianji.api.tianjireport.detail");
        sample.setField("userId", userPrefix + reportEntity.getUserId());
        sample.setField("outUniqueId", reportEntity.getTaskId());
        sample.setField("reportType", "html");

        boolean hasError = false;
        try {
            JSONObject ret = sample.execute();
            if (ret.getInt("error") == 200) {
                String reportName = DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd")
                        + "/" + reportEntity.getTaskId();

                JSONObject responseContent = ret.getJSONObject("tianji_api_tianjireport_detail_response");
                String html = responseContent.getString("html");
                if (StringUtils.isNotBlank(html)) {
                    writeReportToFile(html, reportName, "html");
                    reportEntity.setReportHtmlPath(reportName + ".html");
                }

                String json = responseContent.getString("json");
                if (StringUtils.isNotBlank(json)) {
                    writeReportToFile(json, reportName, "json");
                    reportEntity.setReportJsonPath(reportName + ".json");
                }

            } else {
                logger.error("Error report: {}", ret.toString());
                hasError = true;
            }
        } catch (Exception e) {
            logger.error("Failed to download report", e);
            hasError = true;
        }

        if (hasError) {
            reportEntity.setVerifyStatus(VerifyStatus.FAILED);
        } else {
            reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
        }

        tjReportService.update(reportEntity);
    }

    @Override
    public String getReportUrl(final TjReportEntity reportEntity) {
        return getCloudStorageService().generatePresignedUrl(bucket,
                reportEntity.getTianjiType().name().toLowerCase() + "/"
                        + DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity
                        .getSearchId() + ".html", 60 * 3);
    }

    @Override
    public String getJsonReport(final TjReportEntity reportEntity) {
        return getReportContent(reportEntity, "json");
    }

    @Override
    public String getHtmlReport(final TjReportEntity reportEntity) {
        return getReportContent(reportEntity, "html");
    }

    @Override
    public String getMobileRawReport(final TjReportEntity reportEntity) {
        String path = StringUtils.substringBefore(reportEntity.getTianjiType().name().toLowerCase(), "_") + "/"
                + DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity
                .getSearchId() + "_raw.json.gz";

        try (InputStream is = getCloudStorageService().get(bucket, path)) {
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
            return result;
        } catch (Exception e) {
            logger.error("Failed to load report from OSS: {}", path, e);

            path = StringUtils.substringBefore(path, ".gz");

            try (InputStream is = getCloudStorageService().get(bucket, path)) {
                String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
                return result;
            } catch (Exception e1) {
                logger.error("Failed to load report from OSS: {}, try to download again.", path, e1);
                return downloadMobileRaw(reportEntity);
            }
        }
    }

    private String downloadMobileRaw(final TjReportEntity reportEntity) {
        OpenapiClient sample = new OpenapiClient();

        sample.setAppId(appId); // TODO 设置Appid
        sample.setPrivateKey(privateKey); // TODO 设置机构私钥，需要使用方替换private_key.pem文件
        sample.setIsTestEnv(false); // TODO 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
        sample.setPrintLog(false);
        sample.setLogid(RequestUtil.generateLogid());

        sample.setMethod("wd.api.mobilephone.getdatav2");
        sample.setField("user_id", reportEntity.getSearchId());
        sample.setField("merchant_id", appId);

        boolean hasError = false;
        try {
            JSONObject ret = sample.execute();
            if (ret.getInt("error") == 200) {
                String path = StringUtils.substringBefore(reportEntity.getTianjiType().name().toLowerCase(), "_") + "/"
                        + DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity
                        .getSearchId() + "_raw.json.gz";

                JSONObject responseContent = ret.getJSONObject("wd_api_mobilephone_getdatav2_response");

                String json = responseContent.getString("data");
                if (StringUtils.isNotBlank(json)) {
                    logger.info("Upload mobile raw data to {}", path);
                    getCloudStorageService().upload(bucket, new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), path);

                    return json;
                }

            } else {
                logger.error("Error report: {}", ret.toString());
                hasError = true;
            }
        } catch (Exception e) {
            logger.error("Failed to download report", e);
            hasError = true;
        }

        return "";
    }

    private String getReportContent(final TjReportEntity reportEntity, String ext) {
        String path = StringUtils.substringBefore(reportEntity.getTianjiType().name().toLowerCase(), "_") + "/"
                + DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity
                .getSearchId() + "." + ext;
        String pathWithGz = path + ".gz";
        try (InputStream is = getCloudStorageService().get(bucket, pathWithGz)) {
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
            return result;
        } catch (Exception e) {
            logger.error("Failed to load report from OSS: {}", path, e);

            try (InputStream is = getCloudStorageService().get(bucket, path)) {
                String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
                return result;
            } catch (Exception e1) {
                logger.error("Failed to load report from OSS: {}", path, e1);
                throw new RuntimeException("Failed to load report", e1);
            }
        }
    }

    private void writeReportToFile(String content, String reportName, String reportType) throws Exception {
        String dstFilename = reportName + "." + reportType;

        File dstFile = new File(dstFilename);
        FileUtils.touch(dstFile);
        // 写入文件

        OutputStream os = new FileOutputStream(dstFile);

        try {
            IOUtils.write(content, os, Constant.ENCODING_UTF8);
        } finally {
            IOUtils.closeQuietly(os);
        }
    }


    private Map<String, Object> postJsonData(String host, String path, Map<String, String> requestMap) throws Exception {
        logger.info("Tianji request. HOST: {}, URL: {}, parameters: {}", host, path, requestMap);

        final HttpResponse response = HttpUtils.doPost(host, path,
                MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, requestMap);

        final String responseJson = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
        logger.info("Tianji response json: {}", responseJson);

        return new ObjectMapper().readValue(responseJson,
                new TypeReference<Map<String, Object>>() {
                });
    }

    /**
     * 根据运营商报告JSON格式解析封装数据形成米兜综合报告
     * @param jsoninfo
     * @return
     */
    public AuthMobileReportVO getMobileVo(String jsoninfo)
    {
        AuthMobileReportVO   vo = new AuthMobileReportVO();
        if(StringUtils.isEmpty(jsoninfo)) {
        	return vo;
        }
        try {

            JSONObject  resp  = JSONObject.fromObject(jsoninfo);
            //JSONObject  resp = json.getJSONObject("tianji_api_tianjireport_detail_response");
            //1、基本信息
            JSONObject  baseinfoObject = resp.getJSONObject("basic_info");

            //手机号码
            String mobile = baseinfoObject.getString("phone");
            vo.setMobile(mobile);

            //运营商验证
            int mnoBaseInfoRiskList  = 2;  //无风险

            //实名认证(1 一致)
            String reliability = baseinfoObject.getString("reliability");
            vo.setNameCheck(reliability);
            if(reliability!=null )
            {
                mnoBaseInfoRiskList = Integer.parseInt(reliability) - 1;  //实名认证
            }

            //身份证验证(1一致   2基本一致  3不一致   4  5无法验证)
            String id_card_check  = baseinfoObject.getString("id_card_check");
            vo.setIdCardCheck(id_card_check);
            if(id_card_check!=null && ("1".equals(id_card_check) ||  "2".equals(id_card_check)    ))
            {
                mnoBaseInfoRiskList = 0;
            }
            else if(id_card_check!=null && ("3".equals(id_card_check)  ))
            {
                mnoBaseInfoRiskList = 1;
            }
            else if(id_card_check!=null && ("4".equals(id_card_check) ||  "5".equals(id_card_check)    ))
            {
                mnoBaseInfoRiskList = 2;
            }
            vo.setMnoBaseInfoRiskList(mnoBaseInfoRiskList);

            //注册时间
            String reg_time =  baseinfoObject.getString("reg_time");
            vo.setRegTime(reg_time);

            //账户余额
            String cur_balance =  baseinfoObject.getString("cur_balance");
            vo.setCurBalance(cur_balance);


            //2、风险检测（risk_analysis）
            JSONObject risk_analysis_Object = resp.getJSONObject("risk_analysis");
            JSONObject financial_blacklist_by_id_card_obj = risk_analysis_Object.getJSONObject("financial_blacklist_by_id_card");
            JSONObject financial_blacklist_by_phone_obj = risk_analysis_Object.getJSONObject("financial_blacklist_by_phone");
            int black_type_count = 0;  //身份证号命中黑名单情况
            int black_type_phne = 0 ;  //手机号码命中黑名单情况
            if(financial_blacklist_by_id_card_obj!=null)
            {
                JSONArray black_type  = financial_blacklist_by_id_card_obj.getJSONArray("black_type");
                if(black_type!=null)
                {
                    black_type_count = black_type.size();
                }

                JSONArray phone_black_type  = financial_blacklist_by_phone_obj.getJSONArray("black_type");
                if(phone_black_type!=null)
                {
                    black_type_phne = phone_black_type.size();
                }

            }
            //身份证号命中黑名单情况
            vo.setCardhitCount(black_type_count);
            ////手机号码命中黑名单情况
            vo.setPhoneHitCount(black_type_phne);
            //本人TJ查询次数  searched_cnt
            String searched_cnt = risk_analysis_Object.getString("searched_cnt");
            vo.setSearchedCnt(searched_cnt);
            //本人逾期数
            int blacklist_cnt_num = 0;
            String blacklist_cnt = risk_analysis_Object.getString("blacklist_cnt");
            if(blacklist_cnt!=null)
            {
                blacklist_cnt_num = Integer.parseInt(blacklist_cnt);
            }
            vo.setBlacklistCnt(blacklist_cnt_num);

            //社交健康是否有风险(0无风险  1:有风险   2无法判断)
            vo.setSocialRiskList(0);
            if(blacklist_cnt_num>0 || black_type_count>0 || black_type_count>0)
            {
                vo.setSocialRiskList(1);
            }

            //紧急联系人1号 码命中逾期次数
            JSONArray  single_overdue = risk_analysis_Object.getJSONArray("single_overdue");
            if(single_overdue!=null && single_overdue.size() > 0)
            {
                List<SingleOverdueVO> singleOverdueList = new ArrayList<SingleOverdueVO>();

                for(Iterator iterator = single_overdue.iterator();iterator.hasNext();)
                {
                    Object o = iterator.next();
                    if(o!=null)
                    {
                        JSONObject  jsono = (JSONObject)o;
                        int no = jsono.getInt("no");
                        if(no!= 0)
                        {
                            SingleOverdueVO  singlevo = new SingleOverdueVO();
                            int hit_cnt =  jsono.getInt("hit_cnt");
                            singlevo.setNo(String.valueOf(no));
                            singlevo.setHitCnt(String.valueOf(hit_cnt));

                            singleOverdueList.add(singlevo);
                        }

                    }
                }

                vo.setSingleOverdue(singleOverdueList);
            }
            //3、紧急联系人分析（emergency_analysis
            JSONArray emergency_analysis_array = resp.getJSONArray("emergency_analysis");
            int emergencyCallCount = 0;
            if(emergency_analysis_array!=null && emergency_analysis_array.size() > 0) {

                for (Iterator iterator = emergency_analysis_array.iterator(); iterator.hasNext(); ) {
                    Object o = iterator.next();
                    if (o != null) {
                        JSONObject jsono = (JSONObject) o;
                        int talk_cnt  =  jsono.getInt("talk_cnt");
                        emergencyCallCount = emergencyCallCount + talk_cnt;
                    }
                }
            }
            vo.setEmergencyCallCount(emergencyCallCount);

            //4、用户画像（user_portrait）
            JSONObject user_portrait_Object = resp.getJSONObject("user_portrait");
            JSONObject silent_days_Object = user_portrait_Object.getJSONObject("silent_days");
            //号码静默情况
            String  max_interval =  silent_days_Object.getString("max_interval");
            vo.setMaxInterval(max_interval);
            //夜间活动
            JSONObject night_activities_Object = user_portrait_Object.getJSONObject("night_activities");
            //夜间通话占比
            String monthly_avg_seconds_ratioStr = "0%";
            Float monthly_avg_seconds_ratio  = Float.valueOf(night_activities_Object.getString("monthly_avg_seconds_ratio"));
            if(monthly_avg_seconds_ratio!=null){
                monthly_avg_seconds_ratioStr= String.valueOf(monthly_avg_seconds_ratio*100)+"%";
            }
            vo.setMonthlyAvgSecondsRatio(monthly_avg_seconds_ratioStr);
            //互通联系人个数
            String both_call_cnt = user_portrait_Object.getString("both_call_cnt");
            vo.setBothCallCnt(both_call_cnt);


            //5、用户信息检测（user_info_check）
            JSONObject user_info_check_Object = resp.getJSONObject("user_info_check");
            String contactsBlacklistRatio = null;
            if(user_info_check_Object!=null)
            {
                JSONObject check_black_info_Object = user_info_check_Object.getJSONObject("check_black_info");
                int  contacts_class1_blacklist_cnt =  check_black_info_Object.getInt("contacts_class1_blacklist_cnt");  //直接联系人中黑名单人数
                int  contacts_class1_cnt =check_black_info_Object.getInt("contacts_class1_cnt");  //直接联系人人数
                if(contacts_class1_blacklist_cnt !=-1 && contacts_class1_cnt != -1){
                    Double bb  = new BigDecimal(contacts_class1_blacklist_cnt).divide(new BigDecimal(contacts_class1_cnt),3, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue();
                    contactsBlacklistRatio = String.valueOf(bb)+"%";
                }else{
                    contactsBlacklistRatio="未查询到该用户信息";
                }
                //电话号码组合过其他身份证
                int  phonecards = 0;
                JSONObject check_search_info = user_info_check_Object.getJSONObject("check_search_info");
                if(check_search_info!=null)
                {
                    JSONArray  phone_with_other_idcards   = check_search_info.getJSONArray("phone_with_other_idcards");
                    if(phone_with_other_idcards!=null)
                    {
                        phonecards = phone_with_other_idcards.size();
                    }
                }
                vo.setIdcCount(phonecards);
                //身份证关联手机号个数
                int cardPhones  = 0;
                if(check_search_info!=null)
                {
                    JSONArray  idcard_with_other_phones   = check_search_info.getJSONArray("idcard_with_other_phones");
                    if(idcard_with_other_phones!=null)
                    {
                        cardPhones = idcard_with_other_phones.size();
                    }
                }
                vo.setPhoneCount(cardPhones);


            }
            //用户黑名单比例
            vo.setContactsBlacklistRatio(contactsBlacklistRatio);


            //6、特殊号码类别（special_cate）
            JSONArray  special_cate_array =  resp.getJSONArray("special_cate");
            int callCount110 = 0;  //110号码通话次数
            int  callCount005 = 0;  //贷款类通话次数
            int callCount008 = 0;  //互金公司通话次数
            int  cateCnt = 0 ; //通讯录借贷标签
            if(special_cate_array!=null && special_cate_array.size() > 0) {

                for (Iterator iterator = special_cate_array.iterator(); iterator.hasNext(); ) {
                    Object o = iterator.next();
                    if(o!=null)
                    {
                        JSONObject  jsono = (JSONObject)o;
                        String cate =jsono.getString("cate");
                        int talk_cnt = jsono.getInt("talk_cnt");
                        if("110".equals(cate))
                        {
                            callCount110 = callCount110 + talk_cnt;
                        }
                        else if("贷款类".equals(cate))
                        {
                            callCount005 = callCount005 + talk_cnt;
                            cateCnt = cateCnt+talk_cnt;

                        }
                        else  if("互金公司".equals(cate))
                        {
                            callCount008 = callCount008 + talk_cnt;
                            cateCnt = cateCnt+talk_cnt;
                        }
                    }
                }
            }
            vo.setCallCount110(callCount110);
            vo.setCallCount005(callCount005);
            vo.setCallCount008(callCount008);
            vo.setCateCnt(cateCnt);
            vo.setPartnerCount(0);

        } catch (Exception e) {
            logger.error("运营商报告JSON格式解析封装数据异常", e);
        }

        return vo;
    }

    private CloudStorageService getCloudStorageService() {
        if (cloudStorageService == null) {
            synchronized (this) {
                if (cloudStorageService == null) {
                    cloudStorageService = OSSFactory.build();
                }
            }
        }
        return cloudStorageService;
    }

    public static void main(String[] args) throws Exception {
        OpenapiClient sample = new OpenapiClient();

        sample.setAppId("2010581"); // TODO 设置Appid
        sample.setPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK6Y5EHv3Ki8m+cp\njAKiONab9pcT5J3SoM9UPiPXm3ulGAt8mX3c5yebfduvjs4vKFLiwTo3Bf/D/Ek9\nDpOJA+3Z+GC11JtVb3IqtGXP4sZOMF3j9+IvkrqhWCEXp8OGuedbA7xU2nwkydNA\nST6sgD9Mkh1UgmW0V+VxOHUlbaDpAgMBAAECgYEAmPOPYsQCBj/UQ9l9sgDy0e6n\nQwpGSIvwHDCsjzGeH98tBUMOI9iVF3l79CwDalSdep7yr1DsjHbgWDiIwG5TZWJ/\nfLwB5PqjHUEBcHdneqx0INQKp4LcFxRVRLiLlq3vz6EJorGj4RzYurjUAZwH+kUR\nIlE83H5Q6uVIvmk3CgECQQDn8hlWawy4VlcTpw4KFfHveGQzuUTEpnH6fEhkwyRp\ncM2kPI8TtR6pFc5VKBhk/Rl+pWx6/L42OSBEfDW8OWA5AkEAwLRBJnNF9x2sHSz6\nM4cCnnhG/Sfbsbc/rauqsRKI62w/ilM550bZULXSlA9SDHfUUy4P/n/wZFvV0uEU\nk/fmMQJAKca1QZduZxVGAcgpAzAIr3Ujtx07gZ/pD5CrCVsMh+FFaLtvmcEZkKLY\n0wWxvx7HJMRu0YgMSn/ni+5DT2+WIQJBALZOkcg/i/RyZO8hKv9ufeLQJVDA0Y46\nsAqseoqU32Xh/ebuP7x2gYdizHp4WAYlo4Ch9k2uWg2H+C1N9TrbbzECQGYuLtGe\nb8wAhb6aDmnRcDaKeN1gcWfZStEBq/AIjwpdHBnFeYxC+CpvuQjNQCSyBWsomnJ/\nk31zFoLoWRRjzuA="); // TODO 设置机构私钥，需要使用方替换private_key.pem文件
        sample.setIsTestEnv(false); // TODO 设置为请求测试环境，默认为线上环境（false），需要使用方替换，也可不替换
        sample.setPrintLog(false);
        sample.setLogid(RequestUtil.generateLogid());

        sample.setMethod("wd.api.mobilephone.getdatav2");
//        sample.setField("name", "习大大");
//        sample.setField("idNumber", "110101199003072594");
//        sample.setField("phone", "19900000000");
        sample.setField("user_id", "15424239194236964174");
        sample.setField("merchant_id", "2010581");

        boolean hasError = false;
        try {
            JSONObject ret = sample.execute();

            IOUtils.write(ret.toString(), new FileOutputStream("c:/Users/randy.huang/Downloads/1.json"), "UTF-8");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
