package io.grx.auth.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.grx.auth.service.YouDunService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthYouDunDevicesVO;
import io.grx.modules.auth.entity.AuthYouDunLoanInfoVO;
import io.grx.modules.auth.entity.AuthYouDunReportVO;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.util.Uu;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class YouDunServiceImpl implements YouDunService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${youdun.path}")
    private String path;
    @Value("${youdun.host}")
    private String host;
    @Value("${youdun.btnColor}")
    private String btnColor;
    @Value("${youdun.userPrefix}")
    private String userPrefix;
    @Value("${youdun.enableAll:false}")
    private boolean enableAll;
    @Value("${youdun.mandatory:false}")
    private boolean mandatory;
    @Value("${youdun.storageBucket:udcredit}")
    private String bucket;


    @Autowired
    private AuthUserService authUserService;

    private CloudStorageService cloudStorageService;

    @Override
    public String getH5Url(AuthUserEntity userEntity) {
        Map<String, String> params = new HashMap<>();

        String taskId = StringUtils.defaultString(userPrefix) + "-" + UUIDGenerator.getShortUUID();
        params.put("partner_order_id", taskId);
        params.put("btn_color", btnColor);
        params.put("name", userEntity.getName());
        params.put("id_no", userEntity.getIdNo());
        userEntity.setAuthTaskId(taskId);
        authUserService.update(userEntity);

        String urlBase = HttpContextUtils.getRequestBaseUrl();
        params.put("return_url", urlBase + "/yd/return/" + userEntity.getUserId());
        params.put("callback_url", urlBase + "/yd/callback");

        logger.info("Youdun request. URL: {}, parameters: {}", path, params);

        try {
            final HttpResponse response = HttpUtils.doPost(host, path,
                    MapUtils.EMPTY_MAP, MapUtils.EMPTY_MAP, params);

            final String reponse = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);
            logger.info("Youdun response json: {}", reponse);

            return reponse;
        } catch (Exception e) {
            logger.error("getH5Url", e);
        }

        return "";
    }

    @Override
    public boolean enabledForAll() {
        return enableAll;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * 根据有盾的JSON格式报告形成综合报告
     * @param jsonPath
     * @return
     */
    @Override
    public AuthYouDunReportVO getYoudunInfo(String jsonPath)
    {
        AuthYouDunReportVO   vo  = new AuthYouDunReportVO();

        HttpResponse httpResponse = null;
        try {
            byte[] jsonBytes = null;

            if (StringUtils.startsWithIgnoreCase(jsonPath, "ftp") || StringUtils.startsWithIgnoreCase(jsonPath, "http")) {
                httpResponse = HttpUtils.doGet(jsonPath, org.apache.commons.collections4.MapUtils.EMPTY_SORTED_MAP);
                if (httpResponse.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed to download report. Http content: " + httpResponse.getEntity()
                            .getContent());
                }
                jsonBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            } else {
                jsonBytes = IOUtils.toByteArray(getCloudStorageService().get(bucket, jsonPath));
            }
            InputStream is = new ByteArrayInputStream(jsonBytes);


        /*try (InputStream is = getCloudStorageService().get(jsonPath)) {*/

            //有盾报告JSON
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);

            JSONObject resultJson  =  JSONObject.parseObject(result);

            JSONObject bodyJson  =  resultJson.getJSONObject("body");

            //1--身份信息
            JSONObject id_detail  = bodyJson.getJSONObject("id_detail");

            String address = "";
            if(id_detail!=null)
            {
                //住址
                address = id_detail.getString("address");

            }
            vo.setAddress(address);
            //风险模型得分
            JSONObject score_detail  = bodyJson.getJSONObject("score_detail");
            String risk_evaluation= "";
            String score = "0";
            if(score_detail!=null)
            {
                //风险等级
                risk_evaluation = score_detail.getString("risk_evaluation");
                score =  score_detail.getString("score");

            }
            vo.setScore(score);
            vo.setRiskEvaluation(risk_evaluation);

            //2--图谱统计值
            JSONObject graph_detail  =  bodyJson.getJSONObject("graph_detail");
            //关联用户数  link_user_count
            int linkUserCount=0;
            //使用设备数  link_device_count
            int linkDeviceCount=0;
            int featuresFx = 0; //风险提示 +20190315+ (法院失信+网贷失信+活体攻击+用户标记)之和 = 大于等于2
            int deviceFx = 0 ;   //  (使用设备数:可疑设备+使用设备数:活体攻击设备 + 关联设备数:可疑设+关联设备数:活体攻击设备) 之和 = 大于2
            if(graph_detail!=null) {
                //关联用户数
                linkUserCount = Integer.parseInt(graph_detail.getString("link_user_count"));

                //使用设备数
                linkDeviceCount = Integer.parseInt(graph_detail.getString("link_device_count"));


                //关联用户详情
                JSONObject link_user_detail = graph_detail.getJSONObject("link_user_detail");

                //商户商户标记个数
                String partner_mark_count = link_user_detail.getString("partner_mark_count");
                if(partner_mark_count!=null && !"".equals(partner_mark_count))
                {
                    featuresFx = featuresFx + Integer.parseInt(partner_mark_count);
                }

                //其他关联设详情
                JSONObject other_link_device_detail = graph_detail.getJSONObject("other_link_device_detail");
                //可疑设备
                String other_frand_device_count = other_link_device_detail.getString("other_frand_device_count");
                if(other_frand_device_count!=null && !"".equals(other_frand_device_count))
                {
                    deviceFx = deviceFx + Integer.parseInt(other_frand_device_count);
                }
                //活体攻击设备
                String other_living_attack_device_count = other_link_device_detail.getString("other_living_attack_device_count");
                if(other_living_attack_device_count!=null && !"".equals(other_living_attack_device_count))
                {
                    deviceFx = deviceFx + Integer.parseInt(other_living_attack_device_count);
                }

                //使用设备数详情
                JSONObject link_device_detail = graph_detail.getJSONObject("link_device_detail");
                //可疑设备
                String frand_device_count = link_device_detail.getString("frand_device_count");
                if(frand_device_count!=null && !"".equals(frand_device_count))
                {
                    deviceFx = deviceFx + Integer.parseInt(frand_device_count);
                }
                //活体攻击设备
                String living_attack_device_count =  link_device_detail.getString("living_attack_device_count");
                if(living_attack_device_count!=null && !"".equals(living_attack_device_count))
                {
                    deviceFx = deviceFx + Integer.parseInt(living_attack_device_count);
                }

            }
            vo.setLinkUserCount(linkUserCount);
            vo.setLinkDeviceCount(linkDeviceCount);

            //3--信贷行为
            JSONObject loan_detail  =  bodyJson.getJSONObject("loan_detail");
            //实际借款平台数 actual_loan_platform_count
            int actualoanPlatformCount=0;
            //还款平台数    repayment_platform_count
            int repaymentPlatformCount=0;
            //还款笔数  还款次数  repayment_times_count
            int repaymentTimesCount=0;

            if(loan_detail!=null) {
                //还款次数
                repaymentTimesCount = Integer.parseInt(loan_detail.getString("repayment_times_count")) ;
                //借款平台数
                actualoanPlatformCount = Integer.parseInt(loan_detail.getString("actual_loan_platform_count"));
                //还款平台数
                repaymentPlatformCount = Integer.parseInt(loan_detail.getString("repayment_platform_count"));
            }
            vo.setActualoanPlatformCount(actualoanPlatformCount);
            vo.setRepaymentPlatformCount(repaymentPlatformCount);
            vo.setRepaymentTimesCount(repaymentTimesCount);

            //5.用户特征
            JSONArray featuresArray = bodyJson.getJSONArray("user_features");

            if(featuresArray!=null && featuresArray.size() > 0)
            {
                for (Iterator iterator = featuresArray.iterator(); iterator.hasNext();)
                {
                    Object o = iterator.next();
                    if(o!=null)
                    {
                        JSONObject jsonObject = (JSONObject) o;
                        String user_feature_type = jsonObject.getString("user_feature_type");
                        if("6".equals(user_feature_type) || "7".equals(user_feature_type) || "17".equals(user_feature_type) || "17".equals(user_feature_type)) //+20190315+ (法院失信+网贷失信+活体攻击+用户标记)之和 = 大于等于2
                        {
                            featuresFx = featuresFx + 1;
                        }
                    }
                }
            }

            //满足以下条件则提示风险:
            //+20190315+ (法院失信+网贷失信+活体攻击+用户标记)之和 = 大于等于2
            // +20190315+  (使用设备数:可疑设备+使用设备数:活体攻击设备 + 关联设备数:可疑设+关联设备数:活体攻击设备) 之和 = 大于2
            //总借款平台数 = 借款数 减去 还款数 大于等于 5
            //是否多头申请 = 大于等于20
            //手机号关联身份证个数 = 大于等于2
            //身份证关联手机号个数 = 大于等于2

            vo.setRiskList(0);
            if(featuresFx >= 2 ||  deviceFx>=2 || actualoanPlatformCount >=5 )
            {
                vo.setRiskList(1);
            }

        } catch (Exception e) {
            logger.error("Failed to load report from YouDu: {}", path, e);
            throw new RuntimeException("Failed to load report", e);
        }
        return vo;

    }

    /**
     * 全面解析有盾多头报告JSON格式数据
     * @param jsonPath
     * @return
     */
    @Override
    public AuthYouDunReportVO getAllYoudunInfo(String jsonPath)
    {
        AuthYouDunReportVO vo  = new AuthYouDunReportVO();

        HttpResponse httpResponse = null;
        try {
            byte[] jsonBytes = null;

            if (StringUtils.startsWithIgnoreCase(jsonPath, "ftp") || StringUtils.startsWithIgnoreCase(jsonPath, "http")) {
                httpResponse = HttpUtils.doGet(jsonPath, org.apache.commons.collections4.MapUtils.EMPTY_SORTED_MAP);
                if (httpResponse.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Failed to download report. Http content: " + httpResponse.getEntity()
                            .getContent());
                }
                jsonBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            } else {
                jsonBytes = IOUtils.toByteArray(getCloudStorageService().get(bucket, jsonPath));
            }
            InputStream is = new ByteArrayInputStream(jsonBytes);

            //有盾报告JSON
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);

            JSONObject resultJson  =  JSONObject.parseObject(result);

            JSONObject bodyJson  =  resultJson.getJSONObject("body");

            //用户档案最后更新时间
            String last_modified_time = bodyJson.getString("last_modified_time");
            if(last_modified_time!=null)
            {
                vo.setLastModifiedTime(last_modified_time);
            }

            //1--身份信息
            JSONObject id_detail  = bodyJson.getJSONObject("id_detail");

            String gender= "";
            String nation="";
            String address = "";
            String idcard = "";
            String names = "";
            String issuing_agency = "";
            if(id_detail!=null)
            {
                //性别
                gender = id_detail.getString("gender");
                //民族
                nation = id_detail.getString("nation");
                //住址
                address = id_detail.getString("address");
                //身份证
                idcard  = id_detail.getString("id_number_mask");
                //姓名
                names = id_detail.getString("names");
                //签发机构
                issuing_agency = id_detail.getString("issuing_agency");

            }
            vo.setGender(gender);
            vo.setNation(nation);
            vo.setAddress(address);
            vo.setIdNumberMask(idcard);
            vo.setName(names);
            vo.setIssuingAgency(issuing_agency);

            //风险模型得分
            JSONObject score_detail  = bodyJson.getJSONObject("score_detail");
            String score = "0";
            String risk_evaluation= "未知";
            if(score_detail!=null)
            {
                //评估模型得分
                score = score_detail.getString("score");
                //风险等级
                risk_evaluation = score_detail.getString("risk_evaluation");

            }
            vo.setScore(score);
            vo.setRiskEvaluation(risk_evaluation);

            //2--图谱统计值
            JSONObject graph_detail  =  bodyJson.getJSONObject("graph_detail");
            //关联用户数  link_user_count
            int linkUserCount=0;
            //使用设备数  link_device_count
            int linkDeviceCount=0;
            String mobile_count = "0";

            String other_link_device_count = "";
            String partner_user_count = "";
            String user_have_bankcard_count = "0";
            String bankcard_count = "";
            String court_dishonest_count = "";
            String online_dishonest_count = "";
            String living_attack_count = "";
            String partner_mark_count = "";
            String other_frand_device_count = "";
            String other_living_attack_device_count = "";
            String frand_device_count = "";
            String living_attack_device_count = "";
            if(graph_detail!=null) {
                //关联用户数
                linkUserCount = Integer.parseInt(graph_detail.getString("link_user_count"));
                //使用设备数
                linkDeviceCount = Integer.parseInt(graph_detail.getString("link_device_count"));

                //使用手机号个数
                mobile_count = graph_detail.getString("mobile_count");
                //其他关联设备数
                other_link_device_count = graph_detail.getString("other_link_device_count");
                //本商户内用户数
                partner_user_count =  graph_detail.getString("partner_user_count");
                //名下银行卡数
                user_have_bankcard_count =  graph_detail.getString("user_have_bankcard_count");
                //关联银行卡数
                bankcard_count = graph_detail.getString("bankcard_count");

                //关联用户详情
                JSONObject link_user_detail = graph_detail.getJSONObject("link_user_detail");
                //法院失信
                court_dishonest_count = link_user_detail.getString("court_dishonest_count");
                //网贷失信
                online_dishonest_count = link_user_detail.getString("online_dishonest_count");
                //活体攻击行为
                living_attack_count = link_user_detail.getString("living_attack_count");
                //商户商户标记个数
                partner_mark_count = link_user_detail.getString("partner_mark_count");

                //其他关联设详情
                JSONObject other_link_device_detail = graph_detail.getJSONObject("other_link_device_detail");
                //可疑设备
                other_frand_device_count = other_link_device_detail.getString("other_frand_device_count");
                //活体攻击设备
                other_living_attack_device_count = other_link_device_detail.getString("other_living_attack_device_count");

                //使用设备数详情
                JSONObject link_device_detail = graph_detail.getJSONObject("link_device_detail");
                //可疑设备
                frand_device_count = link_device_detail.getString("frand_device_count");
                //活体攻击设备
                living_attack_device_count =  link_device_detail.getString("living_attack_device_count");
            }
            vo.setLinkUserCount(linkUserCount);
            vo.setLinkDeviceCount(linkDeviceCount);
            vo.setMobileCount(Integer.parseInt(mobile_count));
            vo.setOtherDeviceCount(other_link_device_count);
            vo.setPartnerUserCount(partner_user_count);
            vo.setUserHaveBankcardCount(Integer.parseInt(user_have_bankcard_count));
            vo.setBankcardCount(bankcard_count);
            vo.setCourtDishonestCount(court_dishonest_count);
            vo.setOnlineDishonestCount(online_dishonest_count);
            vo.setLivingAttackCount(living_attack_count);
            vo.setPartnerMarkCount(partner_mark_count);
            vo.setOtherFrandDeviceCount(other_frand_device_count);
            vo.setOtherLivingAttackDcount(other_living_attack_device_count);
            vo.setFrandDeviceCount(frand_device_count);
            vo.setLivingAttackDcount(living_attack_device_count);


            //3--信贷行为
            JSONObject loan_detail  =  bodyJson.getJSONObject("loan_detail");
            //实际借款平台数 actual_loan_platform_count
            int actualoanPlatformCount=0;
            //还款平台数    repayment_platform_count
            int repaymentPlatformCount=0;
            //还款笔数  还款次数  repayment_times_count
            int repaymentTimesCount=0;
            //申请借款平台数
            int  loan_platform_count = 0;
            // 近1月实际借款平台数
            int actual_loan_platform_count_1m = 0;
            //近3个月实际借款平台
            int actual_loan_platform_count_3m = 0;
            //近6个月实际借款平台
            int actual_loan_platform_count_6m = 0;



            //近1个月申请平台数
            int  loan_platform_count_1m = 0;
            //近3月申请借款平台数
            int  loan_platform_count_3m = 0;
            //近6月申请借款平台数
            int loan_platform_count_6m = 0;

            //近1月还款平台数
            int  repayment_platform_count_1m = 0;
            //近3月还款平台数
            int repayment_platform_count_3m = 0;
            //近6月还款平台数
            int repayment_platform_count_6m = 0;

            if(loan_detail!=null) {
                //还款次数
                repaymentTimesCount = NumberUtils.toInt(loan_detail.getString("repayment_times_count"), 0) ;
                //实际借款平台数
                actualoanPlatformCount = NumberUtils.toInt(loan_detail.getString("actual_loan_platform_count"), 0);
                //还款平台数
                repaymentPlatformCount = NumberUtils.toInt(loan_detail.getString("repayment_platform_count"), 0);
                //申请借款平台
                loan_platform_count = NumberUtils.toInt(loan_detail.getString("loan_platform_count"), 0);

                //actual_loan_platform_count_1m  近1月实际借款平台数
                actual_loan_platform_count_1m = NumberUtils.toInt(loan_detail.getString("actual_loan_platform_count_1m"), 0) ;
                actual_loan_platform_count_3m = NumberUtils.toInt(loan_detail.getString("actual_loan_platform_count_3m"), 0) ;
                actual_loan_platform_count_6m = NumberUtils.toInt(loan_detail.getString("actual_loan_platform_count_6m"), 0) ;

                //loan_platform_count_1m  近1月申请借款平台数
                loan_platform_count_1m  =  NumberUtils.toInt(loan_detail.getString("loan_platform_count_1m"), 0) ;
                loan_platform_count_3m = NumberUtils.toInt(loan_detail.getString("loan_platform_count_3m"), 0) ;
                loan_platform_count_6m = NumberUtils.toInt(loan_detail.getString("loan_platform_count_6m"), 0) ;

                //近1月还款平台数
                repayment_platform_count_1m =  NumberUtils.toInt(loan_detail.getString("repayment_platform_count_1m"), 0) ;
                repayment_platform_count_3m =  NumberUtils.toInt(loan_detail.getString("repayment_platform_count_3m"), 0) ;
                repayment_platform_count_6m = NumberUtils.toInt(loan_detail.getString("repayment_platform_count_6m"), 0) ;

                //借款详情
                JSONArray loan_industryArray = loan_detail.getJSONArray("loan_industry");
                if(loan_industryArray!=null && loan_industryArray.size() > 0)
                {
                    List<AuthYouDunLoanInfoVO> loanInfoList = new ArrayList<AuthYouDunLoanInfoVO>();
                    for (Iterator iterator = loan_industryArray.iterator(); iterator.hasNext();)
                    {
                        Object o = iterator.next();
                        if(o!=null)
                        {
                            AuthYouDunLoanInfoVO  voinfo = new AuthYouDunLoanInfoVO();

                            JSONObject jsonObject = (JSONObject) o;
                            //行业名称
                            String name = jsonObject.getString("name");
                            //实际借款平台数
                            String actualloanPlatformCounts = jsonObject.getString("actual_loan_platform_count");
                            //申请借款平台数
                            String loanPlatformCounts  = jsonObject.getString("loan_platform_count");
                            //还款次数
                            String repaymentTimesCounts = jsonObject.getString("repayment_times_count");
                            //还款平台数
                            String repaymentPlatformCounts = jsonObject.getString("repayment_platform_count");

                            voinfo.setLoanName(name);
                            voinfo.setActualloanPlatformCounts(actualloanPlatformCounts);
                            voinfo.setLoanPlatformCounts(loanPlatformCounts);
                            voinfo.setRepaymentTimesCounts(repaymentTimesCounts);
                            voinfo.setRepaymentPlatformCounts(repaymentPlatformCounts);

                            loanInfoList.add(voinfo);
                        }
                    }
                    vo.setLoanInfoList(loanInfoList);
                }
            }
            vo.setLoanPlatformAllcount(loan_platform_count);
            vo.setActualoanPlatformCount(actualoanPlatformCount);
            vo.setRepaymentPlatformCount(repaymentPlatformCount);
            vo.setRepaymentTimesCount(repaymentTimesCount);

            //总申请借款
            int allLoanInfo = loan_platform_count_1m+loan_platform_count_3m+loan_platform_count_6m;
            //总实际借款
            int actualCountInfo = actual_loan_platform_count_1m+actual_loan_platform_count_3m+actual_loan_platform_count_6m;
            //总还款
            int repaymentCcountInfo = repayment_platform_count_1m+repayment_platform_count_3m+repayment_platform_count_6m;

           // StringBuffer allLoanInfoStr = new StringBuffer();
            List<Integer>  allLoanInfoList = new ArrayList<Integer>();
            allLoanInfoList.add(allLoanInfo);
            allLoanInfoList.add(loan_platform_count_6m);
            allLoanInfoList.add(loan_platform_count_3m);
            allLoanInfoList.add(loan_platform_count_1m);
           // allLoanInfoStr.append(allLoanInfo).append(",").append(loan_platform_count_6m).append(",").append(loan_platform_count_3m).append(",").append(loan_platform_count_1m);

            //StringBuffer actualCountInfoStr = new StringBuffer();
            List<Integer>  actualCountInfoList = new ArrayList<Integer>();
            actualCountInfoList.add(actualCountInfo);
            actualCountInfoList.add(actual_loan_platform_count_6m);
            actualCountInfoList.add(actual_loan_platform_count_3m);
            actualCountInfoList.add(actual_loan_platform_count_1m);
           // actualCountInfoStr.append(actualCountInfo).append(",").append(actual_loan_platform_count_6m).append(",").append(actual_loan_platform_count_3m).append(",").append(actual_loan_platform_count_1m);

            //StringBuffer repaymentCcountInfoStr = new StringBuffer();
            List<Integer>  repaymentCcountInfoList = new ArrayList<Integer>();
            repaymentCcountInfoList.add(repaymentCcountInfo);
            repaymentCcountInfoList.add(repayment_platform_count_6m);
            repaymentCcountInfoList.add(repayment_platform_count_3m);
            repaymentCcountInfoList.add(repayment_platform_count_1m);
           // repaymentCcountInfoStr.append(repaymentCcountInfo).append(",").append(repayment_platform_count_6m).append(",").append(repayment_platform_count_3m).append(",").append(repayment_platform_count_1m);


            vo.setAllLoanInfo(allLoanInfoList);
            vo.setActualCountInfo(actualCountInfoList);
            vo.setRepaymentCcountInfo(repaymentCcountInfoList);

            //4---设备信息
            JSONArray devicesListArray = bodyJson.getJSONArray("devices_list");

            if(devicesListArray!=null && devicesListArray.size() > 0)
            {
                List<AuthYouDunDevicesVO>   devicesList = new ArrayList<AuthYouDunDevicesVO>();

                for (Iterator iterator = devicesListArray.iterator(); iterator.hasNext();)
                {
                    Object o = iterator.next();
                    if(o!=null)
                    {
                        AuthYouDunDevicesVO  deinfo = new AuthYouDunDevicesVO();

                        JSONObject jsonObject = (JSONObject)o;
                        //设备名称
                        String device_name = jsonObject.getString("device_name");
                        //设备ID
                        String device_id = jsonObject.getString("device_id");
                        //设备最后使用日期
                        String device_last_use_date  = jsonObject.getString("device_last_use_date");
                        //设备关联用户数
                        String device_link_id_count = jsonObject.getString("device_link_id_count");
                        //设备详情
                        JSONObject device_detailObject = jsonObject.getJSONObject("device_detail");
                        if(device_detailObject!=null)
                        {
                            //是否安装作弊软件 大于0 表示有
                            String cheats_device = device_detailObject.getString("cheats_device");
                            //是否root  1 是 0 不是
                            String is_rooted = device_detailObject.getString("is_rooted");
                            //是否使用代理 1 是 0 不是
                            String is_using_proxy_port = device_detailObject.getString("is_using_proxy_port");
                            //借贷app安装数量
                            String app_instalment_count = device_detailObject.getString("app_instalment_count");
                            //网络类型
                            String network_type = device_detailObject.getString("network_type");
                            if(cheats_device!=null && !"".equals(cheats_device))
                            {
                                deinfo.setCheatsDevice(Integer.parseInt(cheats_device));
                            }
                            else
                            {
                                deinfo.setCheatsDevice(0);
                            }

                            if(app_instalment_count==null || "".equals(app_instalment_count))
                            {
                                app_instalment_count = "0";
                            }
                            deinfo.setIsRooted(is_rooted);
                            deinfo.setUsingProxyPort(is_using_proxy_port);
                            deinfo.setAppCount(app_instalment_count);
                            deinfo.setNetwork(network_type);
                        }
                        deinfo.setDeviceName(device_name);
                        deinfo.setDeviceId(device_id);
                        deinfo.setLastUseData(device_last_use_date);
                        deinfo.setLinkIdCount(device_link_id_count);

                        devicesList.add(deinfo);
                    }

                }
                vo.setDevicesList(devicesList);
            }

            //5.用户特征
            JSONArray featuresArray = bodyJson.getJSONArray("user_features");

            if(featuresArray!=null && featuresArray.size() > 0)
            {
                for (Iterator iterator = featuresArray.iterator(); iterator.hasNext();)
                {
                    Object o = iterator.next();
                    if(o!=null)
                    {
                        JSONObject jsonObject = (JSONObject) o;
                        String user_feature_type = jsonObject.getString("user_feature_type");
                        //设置用户特征
                        setFeatures(vo,user_feature_type);
                    }
                }
            }



        } catch (Exception e) {
            logger.error("Failed to load report from YouDu: {}", path, e);
            throw new RuntimeException("Failed to load report", e);
        }
        return vo;

    }

    @Override
    public String getCloudStorageUrl(String path) {
        return getCloudStorageService().generatePresignedUrl(bucket, path, 1800);
    }

    /**
     * 设置多头报告-用户特征
     * @param vo
     */
    public  void setFeatures(AuthYouDunReportVO   vo , String user_feature_type)
    {
        if("0".equals(user_feature_type))
        {
              vo.setFeatures0(user_feature_type);
        }
        else if("2".equals(user_feature_type))
        {
            vo.setFeatures2(user_feature_type);
        }
        else if("5".equals(user_feature_type))
        {
            vo.setFeatures5(user_feature_type);
        }
        else if("6".equals(user_feature_type))
        {
            vo.setFeatures6(user_feature_type);
        }
        else if("7".equals(user_feature_type))
        {
            vo.setFeatures7(user_feature_type);
        }
        else if("8".equals(user_feature_type))
        {
            vo.setFeatures8(user_feature_type);
        }
        else if("10".equals(user_feature_type))
        {
            vo.setFeatures10(user_feature_type);
        }
        else if("11".equals(user_feature_type))
        {
            vo.setFeatures11(user_feature_type);
        }
        else if("13".equals(user_feature_type))
        {
            vo.setFeatures13(user_feature_type);
        }
        else if("17".equals(user_feature_type))
        {
            vo.setFeatures17(user_feature_type);
        }
        else if("18".equals(user_feature_type))
        {
            vo.setFeatures18(user_feature_type);
        }
        else if("21".equals(user_feature_type))
        {
            vo.setFeatures21(user_feature_type);
        }
        else if("23".equals(user_feature_type))
        {
            vo.setFeatures23(user_feature_type);
        }
        else if("24".equals(user_feature_type))
        {
            vo.setFeatures24(user_feature_type);
        }

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
}
