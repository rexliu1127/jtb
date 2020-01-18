package io.grx.auth.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthUserContactService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.YxReportService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections4.MapUtils;
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
import io.grx.auth.service.YiXiangService;
import io.grx.common.utils.Constant;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.HttpUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.auth.enums.YiXiangType;
import io.grx.modules.oss.cloud.CloudStorageService;
import io.grx.modules.oss.cloud.OSSFactory;

@Service
public class YiXiangServiceImpl implements YiXiangService {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${yixiang.orgId}")
    private String orgId;
    @Value("${yixiang.apiKey}")
    private String apiKey;
    @Value("${yixiang.userPrefix}")
    private String userPrefix;
    @Value("${yixiang.host}")
    private String host;

    @Value("${huluobo.userPrefix}")
    private String hluoPrefix;
    @Value("${huluobo.host}")
    private String hostHuoluobo;
    @Value("${huluobo.apiSecret}")
    private String apiSecret;

    private CloudStorageService cloudStorageService;

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private AuthUserContactService authUserContactService;
    @Autowired
    private AuthUserService authUserService;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);

    @Override
    public R getH5Url(final YiXiangType yiXiangType) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先完善基本信息!");
        }

        YxReportEntity lastReport = yxReportService.queryLatestByUserId(user.getUserId(), yiXiangType);
        if (lastReport != null) {
            if (lastReport.getVerifyStatus() == VerifyStatus.SUBMITTED
                    || lastReport.getVerifyStatus() == VerifyStatus.PROCESSING) {
                return R.error("你已提交认证，请稍后刷新查看结果");
            }

            if (lastReport.getVerifyStatus() == VerifyStatus.SUCCESS && !lastReport.isExpired()) {
                return R.error("你已认证成功，请刷新查看最新结果");
            }
        }

        String timeMark = DateUtils.formateDate(new Date(), "yyyyMMddHHmmss");
        String taskId = userPrefix + "-" + UUIDGenerator.getShortUUID();
        try {
            String url = host
                    + "/h5/"
                    + orgId
                    + "/"
                    + getMD5(orgId + timeMark + yiXiangType.name().toLowerCase() + taskId + apiKey)
                    + "/" + timeMark
                    + "/" + yiXiangType.name().toLowerCase()
                    + "/" + taskId
                    + "?jumpUrl="
                    + HttpContextUtils.getRequestBaseUrl() + "/yx/return/" + user.getUserId()
                    + "/" + yiXiangType.name().toLowerCase()
                    + "/" + taskId;

            YxReportEntity reportEntity = new YxReportEntity();
            reportEntity.setUserId(user.getUserId());
            reportEntity.setIdNo(user.getIdNo());
            reportEntity.setName(user.getName());
            reportEntity.setMobile(user.getMobile());
            reportEntity.setTaskId(taskId);
            reportEntity.setCreateTime(new Date());
            reportEntity.setYiXiangType(yiXiangType);
            reportEntity.setVerifyStatus(VerifyStatus.NEW);
            yxReportService.save(reportEntity);

            logger.info("Go to YX h5 url: {}", url);
            return R.ok().put("url", url);
        } catch (Exception e) {
            logger.error("Failed to get H5 url", e);
        }
        return R.error("获取连接失败");
    }

    @Override
    public R getTaskId(final YiXiangType yiXiangType) {
        AuthUserEntity user = ShiroUtils.getAuthUser();
        if (StringUtils.isBlank(user.getName())) {
            return R.error("请先完善基本信息!");
        }

        YxReportEntity lastReport = yxReportService.queryLatestByUserId(user.getUserId(), yiXiangType);
        if (lastReport != null) {
            if (lastReport.getVerifyStatus() == VerifyStatus.SUBMITTED
                    || lastReport.getVerifyStatus() == VerifyStatus.PROCESSING) {
                return R.error("你已提交认证，请稍后刷新查看结果");
            }

            if (lastReport.getVerifyStatus() == VerifyStatus.SUCCESS && !lastReport.isExpired()) {
                return R.error("你已认证成功，请刷新查看最新结果");
            }
        }

        String timeMark = DateUtils.formateDate(new Date(), "yyyyMMddHHmmss");
        String taskId = userPrefix + "-" + UUIDGenerator.getShortUUID();

        YxReportEntity reportEntity = new YxReportEntity();
        reportEntity.setUserId(user.getUserId());
        reportEntity.setIdNo(user.getIdNo());
        reportEntity.setName(user.getName());
        reportEntity.setMobile(user.getMobile());
        reportEntity.setTaskId(taskId);
        reportEntity.setCreateTime(new Date());
        reportEntity.setYiXiangType(yiXiangType);
        reportEntity.setVerifyStatus(VerifyStatus.NEW);
        yxReportService.save(reportEntity);

        return R.ok().put("taskId", taskId);
    }

    @Override
    public void downloadReport(final YxReportEntity reportEntity) throws Exception {
        Map<String, String> params = new HashMap<>();
//        params.put("orgId", orgId);
//        params.put("token", reportEntity.getSearchId());
//        params.put("sign", getMD5(orgId + apiKey));
        params.put("apiUser", orgId);
        params.put("token", reportEntity.getSearchId());
        params.put("apiEnc", getMD5(orgId + apiKey));

        // json
        HttpResponse httpResponse = HttpUtils.doGet(host, "/api/user/data", MapUtils.EMPTY_SORTED_MAP, params);

        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed to download report. Http content: " + httpResponse.getEntity()
                    .getContent());
        }

        String path = "yx_" + reportEntity.getYiXiangType().name().toLowerCase() + "/" + DateUtils.formateDate
                (reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity.getSearchId() + ".data";

        byte[] jsonBytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        try (InputStream is = new ByteArrayInputStream(jsonBytes)) {
            getCloudStorageService().upload(is, path);
        }

        reportEntity.setReportJsonPath(path);
        //获取淘宝认证的芝麻分
        try {
            if (reportEntity != null && reportEntity.getYiXiangType()==YiXiangType.TAOBAOPAY && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                String reportStr = getJsonReport(reportEntity);
                JSONObject resp  = JSONObject.fromObject(reportStr);
                JSONObject  detailObject = resp.getJSONObject("detail");
                JSONObject  dataObject = detailObject.getJSONObject("data");
                JSONObject  taobaoObject = dataObject.getJSONObject("taobao");
                JSONObject  baseinfoObject = taobaoObject.getJSONObject("basic_info");
                //芝麻分
                String zm_score = baseinfoObject.getString("zm_score");

                //查询auth_user
                AuthUserEntity userEntity = authUserService.queryAuthByYiXiangReportTaskId(reportEntity.getTaskId());
                if (userEntity != null) {
                    userEntity.setTaobaoSesamePoints(zm_score);
                    authUserService.update(userEntity);
                } else {
                    logger.warn("Not found order id: {}", reportEntity.getTaskId());
                }
            }
        } catch (Exception e) {
            logger.error("获取淘宝认证的芝麻分错误", e);
        }
        try {
            // html
            httpResponse = HttpUtils.doGet(host, "/api/user/data/report", MapUtils.EMPTY_SORTED_MAP, params);

            if (httpResponse.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed to download report (html). Http content: " + httpResponse.getEntity()
                        .getContent());
            }

            try (InputStream is = httpResponse.getEntity().getContent()) {
                Map<String, Object> json = new ObjectMapper().readValue(IOUtils.toString(is, "UTF-8"),
                        new TypeReference<Map<String, Object>>() {
                        });

                path = "yx_" + reportEntity.getYiXiangType().name().toLowerCase() + "/" + DateUtils.formateDate
                        (reportEntity.getCreateTime(), "yyyyMMdd") + "/" + reportEntity.getSearchId() + ".html";
                getCloudStorageService().upload(new ByteArrayInputStream(MapUtils.getString(json, "detail").getBytes
                        (Constant.ENCODING_UTF8)), path);

                reportEntity.setReportHtmlPath(path);
            }
        } catch (Exception e) {
            logger.error("Failed to download html report", e);
        }

        reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
        if (reportEntity.getYiXiangType() == YiXiangType.BAIDUYUN) {
            // 下载通讯录
            Map<String, Object> json = new ObjectMapper().readValue(IOUtils.toString(jsonBytes, "UTF-8"),
                    new TypeReference<Map<String, Object>>() {
                    });

            Map<String, String> results = new HashMap<>();
            Collection<Map<String, Object>> contacts = (Collection<Map<String, Object>>) ((Map<String, Object>) ((Map<String, Object>) json.get("detail")).get("data")).get("contactses");
            for (Map<String, Object> group : contacts) {
                Collection<Map<String, String>> details = (Collection<Map<String, String>>) group.get("contacts_details");
                for (Map<String, String> detail : details) {
                    results.put(detail.get("mobile"), detail.get("note_name"));
                }
            }

            logger.debug("{} has {} contacts. search_id: {}", reportEntity.getMobile(),
                    results.size(), reportEntity.getSearchId());
            if (results.size() > 0) {
                authUserContactService.saveOrUpdate(reportEntity.getUserId(), results);
            } else {
                reportEntity.setVerifyStatus(VerifyStatus.FAILED);
            }
        }
        yxReportService.update(reportEntity);
    }

    /**
     * 调用灯塔供应商接口获取借贷数据（包括无忧，借贷宝  今借到平台）
     * @param userID  用户ID
     * @param merchantNo  商户号
     * @param name  姓名
     * @param id_no 身份证号
     * @param mobile  手机号码
     */
    public   YxReportEntity saveYxReportByHuoluobo(Long userID,String merchantNo,String name,String id_no,String mobile)
    {

        //接口服务地址
        StringBuffer hostUrl =  new StringBuffer(hostHuoluobo) ;
        hostUrl.append("?").append("api_secret=").append(apiSecret).append("&")
                .append("phone=").append(mobile).append("&")
                .append("name=").append(name).append("&")
                .append("idcard=").append(id_no);

        logger.info("get Huluobo data from pararms:{}",hostUrl.toString());
        YxReportEntity reportEntity = null;
        try
        {
            //调用灯塔-胡萝卜接口获取借贷报告
            HttpResponse response = HttpUtils.doGet(hostUrl.toString(), new HashMap<>());
            String responseStr = EntityUtils.toString(response.getEntity(), Constant.ENCODING_UTF8);

            Date  now = new Date();
            String taskId = hluoPrefix + "-" + UUIDGenerator.getShortUUID();

            YiXiangType yiXiangType =  YiXiangType.valueOf(StringUtils.upperCase("huluobo"));
            VerifyStatus  status  =  VerifyStatus.valueOf(StringUtils.upperCase("success"));
            //保存记录
            reportEntity  = new  YxReportEntity();
            reportEntity.setMerchantNo(merchantNo);
            reportEntity.setUserId(userID);
            reportEntity.setTaskId(taskId);
            reportEntity.setYiXiangType(yiXiangType);
            reportEntity.setName(name);
            reportEntity.setIdNo(id_no);
            reportEntity.setMobile(mobile);
            reportEntity.setVerifyStatus(status);
            reportEntity.setExpired(true);
            reportEntity.setCreateTime(now);

            String path = StringUtils.substringBefore(reportEntity.getYiXiangType().name().toLowerCase(), "_") + "/"
                    + DateUtils.formateDate(reportEntity.getCreateTime(), "yyyyMMdd") + "/" + id_no+".json" ;

            reportEntity.setReportJsonPath(path);

            //上传到oss服务器
            getCloudStorageService().upload(new ByteArrayInputStream(responseStr.getBytes(StandardCharsets.UTF_8)), path);
            //保存认证记录
            yxReportService.save(reportEntity);

        }
        catch (Exception e)
        {
            logger.error("调用灯塔供应商接口获取借贷数据异常", e);
        }

        return reportEntity;
    }

    /**
     *  综合报告---获取灯塔-胡萝卜JSON数据源封装
     * @param reportEntity
     * @return
     */
    public AuthHuLuoboVO  getCompreByHuLuobo(YxReportEntity reportEntity)
    {
        {
            AuthHuLuoboVO  vo = new AuthHuLuoboVO();
            try
            {
                //四大平台是否存在逾期(false无逾期)
                boolean overdueFlag =false;

                //四大借条平台申请数,最大四个
                int appAllCount = 0;

                //JSON格式数据源
                String jsonStr = getJsonReport(reportEntity);

                JSONObject resp  = JSONObject.fromObject(jsonStr);

                //1、借贷宝
                JSONObject  jiedaibaoObject = resp.getJSONObject("JIE_DAI_BAO_LOAN");
                //历史逾期金额
                String his_overdue_amt = jiedaibaoObject.getString("his_overdue_amt");
                if(!"无".equals(his_overdue_amt))
                {
                    overdueFlag = true;
                }
                //历史贷款笔数
                String total_in_order_cnt =  jiedaibaoObject.getString("total_in_order_cnt");
                if(!"无".equals(total_in_order_cnt))
                {
                    appAllCount = appAllCount + 1;
                }


                //2、无忧借条
                JSONObject  wuyouObject = resp.getJSONObject("WU_YOU_LOAN");
                //是否命中  1 命中（存在当前 45 天以上逾期借条）
                String wuyouHit = wuyouObject.getString("hit");
                if("1".equals(wuyouHit))
                {
                    overdueFlag = true;
                }
                JSONArray  wuyouArray = wuyouObject.getJSONArray("datalist");
                if(wuyouArray!=null)
                {
                    List<AuthWuYouVO>  wuyouList = new ArrayList<AuthWuYouVO>();
                    int wuyouArraySize = wuyouArray.size();
                    if(wuyouArraySize > 0)
                    {
                        appAllCount = appAllCount + 1;
                    }

                }

                //3、米房借条信息
                JSONObject  miFangObject = resp.getJSONObject("MI_FANG_LOAN");
                //是否命中米房
                String mifangHit = miFangObject.getString("hit");
                if("1".equals(mifangHit) ||  "3".equals(mifangHit))
                {
                    overdueFlag = true;
                }

                JSONArray  mifangArray = wuyouObject.getJSONArray("datalist");
                if(mifangArray!=null)
                {
                    List<AuthMiFangVO>  mifangList = new ArrayList<AuthMiFangVO>();
                    int mifangArraySize = mifangArray.size();
                    if(mifangArraySize > 0 )
                    {
                        appAllCount = appAllCount + 1;
                    }

                }

                //4、今借到借条信息
                JSONObject  jinjiedaoObject = resp.getJSONObject("JIN_JIE_DAO_LOAN");
                //历史逾期次数
                String n_overdue_cnt =  jinjiedaoObject.getString("n_overdue_cnt");
                if(n_overdue_cnt!=null && Integer.parseInt(n_overdue_cnt) >0 )
                {
                    overdueFlag = true;
                }

                //累积借入笔数
                String n_borrow_cnt = jinjiedaoObject.getString("n_borrow_cnt");
                if(n_borrow_cnt!=null && Integer.parseInt(n_borrow_cnt) > 0)
                {
                    appAllCount = appAllCount + 1;
                }
                vo.setOverdueFlag(overdueFlag);
                vo.setAppAllCount(appAllCount);

            }catch (Exception e)
            {
                logger.error("load getHuLuoboVoInfo is  error...",e);
            }

            return vo;

        }
    }
    /**
     * 获取灯塔-胡萝卜JSON数据源封装
     * @param reportEntity
     * @return
     */
    public AuthHuLuoboVO  getHuLuoboVoInfo(YxReportEntity reportEntity)
    {
        AuthHuLuoboVO  vo = new AuthHuLuoboVO();
        try
        {

            //JSON格式数据源
            String jsonStr = getJsonReport(reportEntity);

            JSONObject resp  = JSONObject.fromObject(jsonStr);
            //网贷黑名单
            JSONObject  loanBlack = resp.getJSONObject("NET_LOAN_BLACK_LIST");
            String hitLoanBlack = loanBlack.getString("hit");
            String noteLoanBlack = loanBlack.getString("note");
            vo.setHit(hitLoanBlack);
            vo.setNote(noteLoanBlack);

            //借贷宝
            AuthJieDaiBaoVO jiedaibaoVo = new AuthJieDaiBaoVO();
            JSONObject  jiedaibaoObject = resp.getJSONObject("JIE_DAI_BAO_LOAN");
            //是否命中借贷宝  1：命中  0:未命中
            String jiedaiHit = jiedaibaoObject.getString("hit");
            jiedaibaoVo.setHit(jiedaiHit);
            //是否高风险用户
            String is_high_risk_user = jiedaibaoObject.getString("is_high_risk_user");
            jiedaibaoVo.setIsHighRiskUser(is_high_risk_user);
            //最近一次访问日期
            String last_visit_dt = jiedaibaoObject.getString("last_visit_dt");
            jiedaibaoVo.setLastDate(last_visit_dt);
            //30天以上逾期次数
            String thirtyd_overdue_cnt = jiedaibaoObject.getString("thirtyd_overdue_cnt");
            jiedaibaoVo.setThirtydOverdueCnt(thirtyd_overdue_cnt);
            //历史逾期金额
            String his_overdue_amt = jiedaibaoObject.getString("his_overdue_amt");

            jiedaibaoVo.setHisOverdueAmt(his_overdue_amt);
            //最近一次逾期时间
            String last_overdue_dt =  jiedaibaoObject.getString("last_overdue_dt");
            jiedaibaoVo.setLastOverdueDt(last_overdue_dt);
            //最近一次逾期金额
            String last_overdue_amt =  jiedaibaoObject.getString("last_overdue_amt");
            jiedaibaoVo.setLastOverdueAmt(last_overdue_amt);
            //当前逾期金额
            String curr_overdue_amt = jiedaibaoObject.getString("curr_overdue_amt");
            jiedaibaoVo.setCurrOverdueAmt(curr_overdue_amt);
            //当前逾期最大天数
            String curr_overdue_days = jiedaibaoObject.getString("curr_overdue_days");
            jiedaibaoVo.setCurrOverdueDays(curr_overdue_days);
            //首次逾期时间
            String first_overdue_dt =  jiedaibaoObject.getString("first_overdue_dt");
            jiedaibaoVo.setFirstOverdueDt(first_overdue_dt);
            //首次逾期金额
            String first_overdue_amt =  jiedaibaoObject.getString("first_overdue_amt");
            jiedaibaoVo.setFirstOverdueAmt(first_overdue_amt);
            //最近一次还款时间
            String last_repay_tm = jiedaibaoObject.getString("last_repay_tm");
            jiedaibaoVo.setLastRepayTm(last_repay_tm);
            //总还款次数
            String repay_times = jiedaibaoObject.getString("repay_times");
            jiedaibaoVo.setRepayTimes(repay_times);
            //正在进行的贷款笔数
            String curr_debt_product_cnt = jiedaibaoObject.getString("curr_debt_product_cnt");
            jiedaibaoVo.setCurrDebtProductCnt(curr_debt_product_cnt);
            //历史贷款笔数
            String total_in_order_cnt =  jiedaibaoObject.getString("total_in_order_cnt");

            jiedaibaoVo.setTotalInOrderCnt(total_in_order_cnt);
            //历史总借款金额
            String total_in_order_amt = jiedaibaoObject.getString("total_in_order_amt");
            jiedaibaoVo.setTotalInOrderAmt(total_in_order_amt);
            vo.setJiedaibaoVo(jiedaibaoVo);

            //无忧借条信息
            JSONObject  wuyouObject = resp.getJSONObject("WU_YOU_LOAN");
            //是否命中  1 命中（存在当前 45 天以上逾期借条）
            String wuyouHit = wuyouObject.getString("hit");

            String wuyouNote =  wuyouObject.getString("note");
            vo.setWuYouHit(wuyouHit);
            vo.setWuYouNote(wuyouNote);

            JSONArray  wuyouArray = wuyouObject.getJSONArray("datalist");
            if(wuyouArray!=null)
            {
                List<AuthWuYouVO>  wuyouList = new ArrayList<AuthWuYouVO>();
                int wuyouArraySize = wuyouArray.size();

               for(int k=0;k<wuyouArraySize;k++)
               {
                   AuthWuYouVO  wuYouVO = new AuthWuYouVO();
                   JSONObject  wuyouOb = wuyouArray.getJSONObject(k);
                   //姓名
                   String name = wuyouOb.getString("name");
                   //身份证号
                   String idcard = wuyouOb.getString("idcard");
                   //miso
                   String result = wuyouOb.getString("result");
                   wuYouVO.setName(name);
                   wuYouVO.setIdcard(idcard);
                   wuYouVO.setResult(result);
                   wuyouList.add(wuYouVO);
               }
                vo.setWuyouList(wuyouList);
            }

            //米房借条信息
            JSONObject  miFangObject = resp.getJSONObject("MI_FANG_LOAN");
            //是否命中米房
            String mifangHit = miFangObject.getString("hit");

            String mifangNote = miFangObject.getString("note");
            vo.setMiFangHit(mifangHit);
            vo.setMiFangNote(mifangNote);
            JSONArray  mifangArray = miFangObject.getJSONArray("datalist");
            if(mifangArray!=null)
            {
                List<AuthMiFangVO>  mifangList = new ArrayList<AuthMiFangVO>();
                int mifangArraySize = mifangArray.size();

                for(int k=0;k<mifangArraySize;k++)
                {
                    AuthMiFangVO  mifangVO = new AuthMiFangVO();
                    JSONObject  mifangOb = mifangArray.getJSONObject(k);
                    //state 1 为当前逾期 2 为历史逾期已还清；
                    String state = mifangOb.getString("state");
                    //逾期天数
                    String days = mifangOb.getString("days");
                    //金额
                    String money = mifangOb.getString("money");
                    //应还日期
                    String overtime = mifangOb.getString("overtime");
                    String result = mifangOb.getString("result");

                    mifangVO.setState(state);
                    mifangVO.setDays(days);
                    mifangVO.setMoney(money);
                    mifangVO.setOvertime(overtime);
                    mifangVO.setResult(result);

                    mifangList.add(mifangVO);
                }
                vo.setMifangList(mifangList);
            }


            //今借到借条信息
            AuthJinJieDaoVO  jinJieDaoVO = new AuthJinJieDaoVO();
            JSONObject  jinjiedaoObject = resp.getJSONObject("JIN_JIE_DAO_LOAN");
            //是否命中
            String jinjiedaoHit = jinjiedaoObject.getString("hit");
            String jinjiedaoNote = jinjiedaoObject.getString("note");
            jinJieDaoVO.setJinJieDaoHit(jinjiedaoHit);
            jinJieDaoVO.setJinJieDaoNote(jinjiedaoNote);
            //注册日期
            String register_time = jinjiedaoObject.getString("register_time");
            jinJieDaoVO.setRegisterTime(register_time);
            //累积借入人数
            String n_borrow_people_num = jinjiedaoObject.getString("n_borrow_people_num");
            jinJieDaoVO.setnBorrowPeopleNum(n_borrow_people_num);
            //累积借出次数
            String n_lend_cnt =  jinjiedaoObject.getString("n_lend_cnt");
            jinJieDaoVO.setnLendCnt(n_lend_cnt);
            //累积借款金额
            String n_borrow_amt = jinjiedaoObject.getString("n_borrow_amt");
            jinJieDaoVO.setnBorrowAmt(n_borrow_amt);
            //当前逾期金额
            String n_current_overdue_amt = jinjiedaoObject.getString("n_current_overdue_amt");
            jinJieDaoVO.setnCurrentOverdueAmt(n_current_overdue_amt);
            //>=7 天逾期次数
            String n_overdue_7days_cnt = jinjiedaoObject.getString("n_overdue_7days_cnt");
            jinJieDaoVO.setnOverdue7daysCnt(n_overdue_7days_cnt);
            //当前逾期笔数
            String n_current_overdue_cnt = jinjiedaoObject.getString("n_current_overdue_cnt");
            jinJieDaoVO.setnCurrentOverdueCnt(n_current_overdue_cnt);
            //>=7 天逾期金额
            String n_overdue_7days_amt = jinjiedaoObject.getString("n_overdue_7days_amt");
            jinJieDaoVO.setnOverdue7daysAmt(n_overdue_7days_amt);
            //累积逾期总额
            String n_overdue_amt = jinjiedaoObject.getString("n_overdue_amt");
            jinJieDaoVO.setnOverdueAmt(n_overdue_amt);
            //历史逾期次数
            String n_overdue_cnt =  jinjiedaoObject.getString("n_overdue_cnt");
            jinJieDaoVO.setnOverdueCnt(n_overdue_cnt);
            //最大逾期金额
            String n_to_overdue_max_amt = jinjiedaoObject.getString("n_to_overdue_max_amt");
            jinJieDaoVO.setnToOverdueMaxAmt(n_to_overdue_max_amt);
            //最近 7 天逾期次数
            String n_current_overdue_7days_cnt = jinjiedaoObject.getString("n_current_overdue_7days_cnt");
            jinJieDaoVO.setnCurrentOverdue7daysCnt(n_current_overdue_7days_cnt);
            //是否超过 90 天的逾期
            String n_overude_90_days =  jinjiedaoObject.getString("n_overude_90_days");
            jinJieDaoVO.setnOverude90Days(n_overude_90_days);
            //累积借入笔数
            String n_borrow_cnt = jinjiedaoObject.getString("n_borrow_cnt");
            jinJieDaoVO.setnBorrowCnt(n_borrow_cnt);
            //>=30 天逾期金额
            String n_overdue_30days_amt =  jinjiedaoObject.getString("n_overdue_30days_amt");
            jinJieDaoVO.setnOverdue30daysAmt(n_overdue_30days_amt);
            //>=30 天逾期次数
            String n_overdue_30days_cnt = jinjiedaoObject.getString("n_overdue_30days_cnt");
            jinJieDaoVO.setnOverdue30daysCnt(n_overdue_30days_cnt);
            //逾期列表
            JSONArray  jinJieDaoArray = jinjiedaoObject.getJSONArray("overduelist");
            if(jinJieDaoArray!=null)
            {
                List<AuthJinJieDaoOverDueVO>  jinJieDaoOverDueVOSList = new ArrayList<AuthJinJieDaoOverDueVO>();

                for(int k=0;k<jinJieDaoArray.size();k++)
                {
                    AuthJinJieDaoOverDueVO  jinJieDaoOverDueVO = new AuthJinJieDaoOverDueVO();
                    JSONObject  jinjiedaoOverOb = jinJieDaoArray.getJSONObject(k);
                    //还款日期
                    String t_repay_tm = jinjiedaoOverOb.getString("t_repay_tm");
                    jinJieDaoOverDueVO.settRepayTm(t_repay_tm);
                    //计息日期
                    String c_tm = jinjiedaoOverOb.getString("c_tm");
                    jinJieDaoOverDueVO.setCtm(c_tm);
                    //金额
                    String n_amt = jinjiedaoOverOb.getString("n_amt");
                    jinJieDaoOverDueVO.setNamt(n_amt);
                    //逾期天数
                    String n_count = jinjiedaoOverOb.getString("n_count");
                    jinJieDaoOverDueVO.setNcount(n_count);
                    //是否处理
                    String b_end = jinjiedaoOverOb.getString("b_end");
                    jinJieDaoOverDueVO.setBend(b_end);

                    jinJieDaoOverDueVOSList.add(jinJieDaoOverDueVO);
                }
                jinJieDaoVO.setOverduelist(jinJieDaoOverDueVOSList);
            }
            vo.setJinJieDaoVO(jinJieDaoVO);

        }catch (Exception e)
        {
            logger.error("load getHuLuoboVoInfo is  error...",e);
        }

        return vo;

    }


    /**
     *  电商-全知(淘宝和支付聚合接口)
     * @param reportEntity
     * @return
     */
    public AuthTaobaoPayVO getAuthTaobaoPayVO(YxReportEntity reportEntity)
    {
        AuthTaobaoPayVO  vo = new  AuthTaobaoPayVO();
        try
        {
            //JSON格式数据源
            String jsonStr = getJsonReport(reportEntity);

            JSONObject resp  = JSONObject.fromObject(jsonStr);

            JSONObject  detailObj = resp.getJSONObject("detail");
            JSONObject  detaObj = detailObj.getJSONObject("data");
            //淘宝信息
            JSONObject  taobaoObj = detaObj.getJSONObject("taobao");
            //支付宝
            JSONObject  alipayObj = detaObj.getJSONObject("alipay");
            //淘宝基础信息
            JSONObject  tbBasicinfo = taobaoObj.getJSONObject("basic_info");
            //用户名
            String tbUserName  = tbBasicinfo.getString("user_name");
            vo.setTbUserName(tbUserName);
            //淘宝买家级别
            String tbVipLevel = tbBasicinfo.getString("vip_level");
            vo.setTbVipLevel(replaceStr(tbVipLevel));
            //淘宝邮箱
            String tbemail = tbBasicinfo.getString("email");
            vo.setTbemail(replaceStr(tbemail));
            //淘气值
            String taoScore = tbBasicinfo.getString("tao_score");
            vo.setTaoScore(replaceStr(taoScore));
            //真实姓名
            String tbRealName = tbBasicinfo.getString("real_name");
            vo.setTbRealName(replaceStr(tbRealName));
            //	//绑定手机
            String tbmobile = tbBasicinfo.getString("mobile");
            vo.setTbMobile(replaceStr(tbmobile));
            //好评率
            String tbFavorableRate = tbBasicinfo.getString("favorable_rate");
            vo.setTbFavorableRate(replaceStr(tbFavorableRate));
            //性别值
            String tbgender = tbBasicinfo.getString("gender");
            vo.setTbgender(replaceStr(tbgender));
            //是否实名
            String tbidentityStatus = tbBasicinfo.getString("identity_status");
            vo.setTbidentityStatus(replaceStr(tbidentityStatus));
            //芝麻信用分
            String tbzmScore = tbBasicinfo.getString("zm_score");
            vo.setTbzmScore(replaceStr(tbzmScore));
            //收货地址
            JSONArray  taobaoAddressesArray = taobaoObj.getJSONArray("addresses");
            List<TaobaoAddressVO>  taobaoAddressList = new ArrayList<TaobaoAddressVO>();
            if(taobaoAddressesArray!=null) {
                for (int k = 0; k < taobaoAddressesArray.size(); k++) {
                    TaobaoAddressVO taobaoAddressVO = new TaobaoAddressVO();
                    JSONObject taobaoAddressOb = taobaoAddressesArray.getJSONObject(k);
                    //姓名
                    String name = taobaoAddressOb.getString("name");
                    taobaoAddressVO.setName(name);
                    //地址
                    String address = taobaoAddressOb.getString("address");
                    taobaoAddressVO.setAddress(address);
                    //联系方式
                    String phone_no =taobaoAddressOb.getString("phone_no");
                    taobaoAddressVO.setPhoneNo(phone_no);
                    //邮编
                    String postCode =taobaoAddressOb.getString("postcode");
                    taobaoAddressVO.setPostCode(postCode);
                    //备注
                    String note = taobaoAddressOb.getString("note");
                    taobaoAddressVO.setNote(note);

                    taobaoAddressList.add(taobaoAddressVO);

                }
            }
            vo.setTaobaoAddressList(taobaoAddressList);
            //订单信息
            JSONArray  tradeDetailsArray =   taobaoObj.getJSONArray("trade_details");
            List<TrandeInfoVO>   trandeInfoList= new ArrayList<TrandeInfoVO>();
            if(tradeDetailsArray!=null) {
                for (int k = 0; k < tradeDetailsArray.size(); k++) {
                    TrandeInfoVO trandeInfoVO = new TrandeInfoVO();
                    JSONObject trandeDetailsOb = tradeDetailsArray.getJSONObject(k);
                    //订单信息
                    JSONObject trade_infoOb = trandeDetailsOb.getJSONObject("trade_info");
                    //交易状态
                    String trade_status = trade_infoOb.getString("trade_status");
                    trandeInfoVO.setTradeStatus(trade_status);
                    //订单金额
                    String amt = trade_infoOb.getString("amount");
                    trandeInfoVO.setAmount(amt);
                    //成交时间
                    String trade_time = trade_infoOb.getString("trade_time");
                    trandeInfoVO.setTradeTime(trade_time);
                    //收货人姓名
                    String receiver = trade_infoOb.getString("receiver");
                    if(receiver==null || "null".equals(receiver))
                    {
                        receiver = "";
                    }
                    trandeInfoVO.setReceiver(receiver);
                    //收货人联系方式
                    String receive_phone = trade_infoOb.getString("receive_phone");
                    if(receive_phone==null || "null".equals(receive_phone))
                    {
                        receive_phone = "";
                    }
                    trandeInfoVO.setReceivePhone(receive_phone);
                    //商品名称
                    StringBuffer producteNameSf = new  StringBuffer("");
                    JSONArray  productInfoArray =   trandeDetailsOb.getJSONArray("product_info");
                    for (int i= 0; i < productInfoArray.size(); i++)
                    {
                        JSONObject product_info = productInfoArray.getJSONObject(i);
                        String product_name = product_info.getString("product_name");
                        producteNameSf.append(product_name).append(";;");
                    }
                    trandeInfoVO.setProductName(producteNameSf.toString());

                    trandeInfoList.add(trandeInfoVO);

                }
            }
            vo.setTrandeInfoList(trandeInfoList);

            //支付宝基础信息
            JSONObject  alipaBasicinfo = alipayObj.getJSONObject("basic_info");
            //支付宝账户
            String  alipay_user_name = alipaBasicinfo.getString("user_name");
            vo.setAlipayUserName(replaceStr(alipay_user_name));
            //支付宝账户信息
            JSONObject  wealth = alipayObj.getJSONObject("wealth");
            //支付宝余额
            String alipayBalance = wealth.getString("balance");
            vo.setAlipayBalance(replaceStr(alipayBalance));
            //余额宝 余额
            String alipayYuebao   = wealth.getString("yuebao");
            vo.setAlipayYuebao(replaceStr(alipayYuebao));
            //花呗总额度
            String alipayhbTotalLimit = wealth.getString("hb_total_limit");
            vo.setAlipayhbTotalLimit(replaceStr(alipayhbTotalLimit));
            //累计历史收益
            String alipayYebTotalEarnings =  wealth.getString("yeb_total_earnings");
            vo.setAlipayYebTotalEarnings(replaceStr(alipayYebTotalEarnings));
            //花呗可用额度
            String alipayhbAvailableLimit = wealth.getString("hb_available_limit");
            vo.setAlipayhbAvailableLimit(replaceStr(alipayhbAvailableLimit));
            //支付宝绑定银行卡
            List<AlipayBankcardsVO> alipayBankcardsList = new ArrayList<AlipayBankcardsVO>();
            JSONArray  bankCardsArray =   alipayObj.getJSONArray("bank_cards");
            if(bankCardsArray!=null) {
                for (int k = 0; k < bankCardsArray.size(); k++) {
                    AlipayBankcardsVO alipayBankcardsVO = new AlipayBankcardsVO();
                    JSONObject alipayBankcardsOb = bankCardsArray.getJSONObject(k);
                    //银行名称
                    String bankName = alipayBankcardsOb.getString("bank_name");
                    alipayBankcardsVO.setBankName(bankName);
                    //卡号
                    String cardNum = alipayBankcardsOb.getString("card_num");
                    alipayBankcardsVO.setCardNum(cardNum);
                    //卡类型
                    String card_type = alipayBankcardsOb.getString("card_type");
                    alipayBankcardsVO.setCardType(card_type);
                    //户名
                    String card_name =  alipayBankcardsOb.getString("card_name");
                    alipayBankcardsVO.setCardName(card_name);
                    //绑定手机号
                    String phone_num = alipayBankcardsOb.getString("phone_num");
                    alipayBankcardsVO.setPhoneNum(phone_num);
                    //开卡时间
                    String open_date = alipayBankcardsOb.getString("open_date");
                    alipayBankcardsVO.setOpenDate(open_date);
                    //是否快捷
                    String quick_payment  = alipayBankcardsOb.getString("quick_payment");
                    alipayBankcardsVO.setQuickPayment(quick_payment);

                    alipayBankcardsList.add(alipayBankcardsVO);
                }

            }
            vo.setAlipayBankcardsList(alipayBankcardsList);

        }catch (Exception e)
        {
            logger.error("load getAuthTaobaoPayVO is  error...",e);
        }


        return vo;

    }


    public String replaceStr(String sourceStr)
    {
        if(sourceStr == null ||  "null".equals(sourceStr) || "".equals(sourceStr))
        {
            return "-";
        }
        return sourceStr;
    }

    @Override
    public String getJsonReport(final YxReportEntity reportEntity) {
        String path = reportEntity.getReportJsonPath();

        try (InputStream is = getCloudStorageService().get(path)) {
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load report", e);
        }
    }

    @Override
    public String getHtmlReport(final YxReportEntity reportEntity) {
        String path = reportEntity.getReportHtmlPath();

        try (InputStream is = getCloudStorageService().get(path)) {
            String result = IOUtils.toString(is, Constant.ENCODING_UTF8);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load report", e);
        }
    }

    @Override
    public void asyncDownloadReport(final YxReportEntity reportEntity) {
        fixedThreadPool.submit(new DownloadTask(this, reportEntity));
    }

    private String getMD5(String info) {
        try {
            //获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //update(byte[])方法，输入原数据
            //类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
            md5.update(info.getBytes("UTF-8"));
            //digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
            //digest()返回值16位长度的哈希值，由byte[]承接
            byte[] md5Array = md5.digest();
            //byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
            return bytesToHex1(md5Array);
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    private static String bytesToHex1(byte[] md5Array) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < md5Array.length; i++) {
            int temp = 0xff & md5Array[i];//TODO:此处为什么添加 0xff & ？
            String hexString = Integer.toHexString(temp);
            if (hexString.length() == 1) {//如果是十六进制的0f，默认只显示f，此时要补上0
                strBuilder.append("0").append(hexString);
            } else {
                strBuilder.append(hexString);
            }
        }
        return strBuilder.toString();
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

class DownloadTask implements Runnable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    YiXiangService yiXiangService;
    YxReportEntity reportEntity;
    DownloadTask(YiXiangService yiXiangService, YxReportEntity reportEntity) {
        this.yiXiangService = yiXiangService;
        this.reportEntity = reportEntity;
    }

    @Override
    public void run() {
        try {
            yiXiangService.downloadReport(reportEntity);
        } catch (Exception e) {
            logger.error("Failed to download report:" + reportEntity.getTaskId(), e);
        }
    }
}
