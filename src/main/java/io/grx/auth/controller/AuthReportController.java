package io.grx.auth.controller;


import java.io.PrintWriter;
import java.util.*;


import io.grx.auth.service.YouDunService;
import io.grx.common.enums.ValueEnum;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.Query;
import io.grx.common.utils.R;
import io.grx.modules.auth.converter.AuthRequestHistoryVOConverter;
import io.grx.modules.auth.dto.AuthRequestHistoryVO;
import io.grx.modules.auth.entity.*;
import io.grx.modules.auth.service.*;
import io.grx.modules.opt.service.ChannelService;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.entity.TxLenderEntity;
import io.grx.modules.tx.service.TxLenderService;
import io.grx.tianji.TianjiUtils;
import io.grx.youdun.YouDunDuoTouReportUtils;
import io.grx.youdun.YouDunUtils;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.auth.service.TianjiService;
import io.grx.auth.service.YiXiangService;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.enums.YiXiangType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/autha/report")
public class AuthReportController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthRequestService authRequestService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private TjReportService tjReportService;

    @Autowired
    private YouDunService youDunService;

    @Autowired
    private TianjiService tianjiService;

    @Autowired
    private YxReportService yxReportService;

    @Autowired
    private YiXiangService yiXiangService;

    @Autowired
    private AuthUserContactService authUserContactService;
    @Autowired
    private TxLenderService txLenderService;

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private AuthRequestHistoryVOConverter authRequestHistoryVOConverter;

    @Autowired
    private AuthRequestHistoryService authRequestHistoryService;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private AuthUserTianJiService authUserTianJiService;
    @Autowired
    private AuthUserReportDTService authUserReportDTService;

    /**
     * 天机电商报告
     *
     * @return
     */
    @RequestMapping(value = "/tj/ds_all/{requestId}", method = RequestMethod.GET)
    public String cxbMainReport(Model model, @PathVariable(value = "requestId") Long requestId) {

        try {

            AuthRequestEntity requestEntity = authRequestService.queryObject(requestId);

            Map<ValueEnum, Object> dsReports = new LinkedHashMap<>();
            if (requestEntity != null) {
                for (TianjiType type : Arrays.asList(TianjiType.ALIPAY, TianjiType.TAOBAO_CRAWL)) {
                    TjReportEntity reportEntity = tjReportService.queryLatestByUserId(requestEntity.getUserId(), type);
                    if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                        String reportStr = tianjiService.getJsonReport(reportEntity);
                        Map<String, Object> reportMap = new ObjectMapper().readValue(reportStr,
                                new TypeReference<Map<String, Object>>() {
                                });
                        dsReports.put(type, reportMap);
                    }
                }

                for (TianjiType type : Arrays.asList(TianjiType.JD)) {
                    TjReportEntity reportEntity = tjReportService.queryLatestByUserId(requestEntity.getUserId(), type);
                    if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                        try {
                            String reportStr = tianjiService.getHtmlReport(reportEntity);

                            if (StringUtils.isNotBlank(reportStr)) {
                                reportStr = StringUtils.substringAfter(reportStr, "<div class=\"def_report_content\">");
                                reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                                reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                                if (type == TianjiType.JD) {
                                    reportStr = StringUtils.replace(reportStr, "电商报告", "京东报告");
                                }
                            }
                            dsReports.put(type, reportStr);
                        } catch (Exception e) {
                            logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                    .getTianjiType().name(), e);
                        }
                    }
                }

                for (YiXiangType type : Arrays.asList(YiXiangType.TAOBAOPAY)) {
                    YxReportEntity reportEntity = yxReportService.queryLatestByUserId(requestEntity.getUserId(), type);
                    if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                        try {
                            String reportStr = yiXiangService.getJsonReport(reportEntity);

                            Map<String, Object> reportMap = new ObjectMapper().readValue(reportStr,
                                    new TypeReference<Map<String, Object>>() {
                                    });
                            dsReports.put(type, reportMap);
                        } catch (Exception e) {
                            logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                    .getYiXiangType().name(), e);
                        }
                    }
                }

             }
             model.addAttribute("reportMap", dsReports);

            return "auth/cxb/tj_ds_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 天机电商报告
     *
     * @return
     */
    @RequestMapping(value = "/tj/ds_all_user/{userId}", method = RequestMethod.GET)
    public String cxbMainReportByUser(Model model, @PathVariable(value = "userId") Long userId) {

        try {

            Map<ValueEnum, Object> dsReports = new LinkedHashMap<>();
            for (TianjiType type : Arrays.asList(TianjiType.ALIPAY, TianjiType.TAOBAO_CRAWL)) {
                TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, type);
                if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    String reportStr = tianjiService.getJsonReport(reportEntity);
                    Map<String, Object> reportMap = new ObjectMapper().readValue(reportStr,
                            new TypeReference<Map<String, Object>>() {
                            });
                    dsReports.put(type, reportMap);
                }
            }

            for (TianjiType type : Arrays.asList(TianjiType.JD, TianjiType.EMAIL_CRAWL)) {
                TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, type);
                if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    try {
                        String reportStr = tianjiService.getHtmlReport(reportEntity);

                        if (StringUtils.isNotBlank(reportStr)) {
                            reportStr = StringUtils.substringAfter(reportStr, "<div class=\"def_report_content\">");
                            reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                            reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                            if (type == TianjiType.JD) {
                                reportStr = StringUtils.replace(reportStr, "电商报告", "京东报告");
                            }
                        }
                        dsReports.put(type, reportStr);
                    } catch (Exception e) {
                        logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                .getTianjiType().name(), e);
                    }
                }
            }

            for (YiXiangType type : Arrays.asList(YiXiangType.TAOBAOPAY)) {
                YxReportEntity reportEntity = yxReportService.queryLatestByUserId(userId, type);
                if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    try {
                        String reportStr = yiXiangService.getJsonReport(reportEntity);

                        Map<String, Object> reportMap = new ObjectMapper().readValue(reportStr,
                                new TypeReference<Map<String, Object>>() {
                                });
                        dsReports.put(type, reportMap);
                    } catch (Exception e) {
                        logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                .getYiXiangType().name(), e);
                    }
                }
            }
            model.addAttribute("reportMap", dsReports);

            return "auth/cxb/tj_ds_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 亿象借贷报告
     *
     * @return
     */
    @RequestMapping(value = "/yx/loan_all/{requestId}", method = RequestMethod.GET)
    public String cxbLoanReport(Model model, @PathVariable(value = "requestId") Long requestId) {

        try {

            AuthRequestEntity requestEntity = authRequestService.queryObject(requestId);

            Map<YiXiangType, Object> dsReports = new LinkedHashMap<>();
            if (requestEntity != null) {
                for (YiXiangType type : Arrays.asList(YiXiangType.MIFANG, YiXiangType.JIEDAIBAO,
                        YiXiangType.JIEDAIBAO, YiXiangType.WUYOUJIETIAO, YiXiangType.YOUPINZHENG)) {
                    YxReportEntity reportEntity = yxReportService.queryLatestByUserId(requestEntity.getUserId(), type);
                    if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                        try {
                            String reportStr = yiXiangService.getHtmlReport(reportEntity);

                            reportStr = StringUtils.substringAfter(reportStr, "<div id='json-table-container'>");
                            reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                            reportStr = "<div class='json-table-container'>" + reportStr + "</div>";
                            dsReports.put(type, reportStr);
                        } catch (Exception e) {
                            logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                    .getYiXiangType().name(), e);
                        }
                    }
                }

            }
            model.addAttribute("reportMap", dsReports);

            return "auth/cxb/yx_loan_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/yx/loan_all_user/{userId}", method = RequestMethod.GET)
    public String cxbLoanReportByUser(Model model, @PathVariable(value = "userId") Long userId) {

        try {
            Map<YiXiangType, Object> dsReports = new LinkedHashMap<>();
            for (YiXiangType type : Arrays.asList(YiXiangType.MIFANG, YiXiangType.JIEDAIBAO,
                    YiXiangType.JIEDAIBAO, YiXiangType.WUYOUJIETIAO, YiXiangType.YOUPINZHENG)) {
                YxReportEntity reportEntity = yxReportService.queryLatestByUserId(userId, type);
                if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    try {
                        String reportStr = yiXiangService.getHtmlReport(reportEntity);

                        reportStr = StringUtils.substringAfter(reportStr, "<div id='json-table-container'>");
                        reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                        reportStr = "<div class='json-table-container'>" + reportStr + "</div>";
                        dsReports.put(type, reportStr);
                    } catch (Exception e) {
                        logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                .getYiXiangType().name(), e);
                    }
                }
            }
            model.addAttribute("reportMap", dsReports);

            return "auth/cxb/yx_loan_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 天机其他报告
     *
     * @return
     */
    @RequestMapping(value = "/tj/other/{requestId}", method = RequestMethod.GET)
    public String tjOtherReport(Model model, @PathVariable(value = "requestId") Long requestId) {

        try {

            AuthRequestEntity requestEntity = authRequestService.queryObject(requestId);

            Map<TianjiType, Object> dsReports = new LinkedHashMap<>();
            if (requestEntity != null) {
                for (TianjiType type : Arrays.asList(TianjiType.INSURE, TianjiType.FUND)) {
                    TjReportEntity reportEntity = tjReportService.queryLatestByUserId(requestEntity.getUserId(), type);
                    if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                        try {
                            String reportStr = tianjiService.getHtmlReport(reportEntity);
                            reportStr = StringUtils.substringAfter(reportStr, "<div class=\"def_report_content\">");
                            reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                            reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                            dsReports.put(type, reportStr);
                        } catch (Exception e) {
                            logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                    .getTianjiType().name(), e);
                        }
                    }
                }
            }
            model.addAttribute("reportMap", dsReports);

            return "auth/cxb/tj_other_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 天机其他报告
     *
     * @return
     */
    @RequestMapping(value = "/tj/other_user/{userId}", method = RequestMethod.GET)
    public String tjOtherReportByUser(Model model, @PathVariable(value = "userId") Long userId) {

        try {

            Map<TianjiType, Object> dsReports = new LinkedHashMap<>();
            for (TianjiType type : Arrays.asList(TianjiType.INSURE, TianjiType.FUND)) {
                TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, type);
                if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                    try {
                        String reportStr = tianjiService.getHtmlReport(reportEntity);
                        reportStr = StringUtils.substringAfter(reportStr, "<div class=\"def_report_content\">");
                        reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                        reportStr = StringUtils.substringBeforeLast(reportStr, "</div>");
                        dsReports.put(type, reportStr);
                    } catch (Exception e) {
                        logger.error("Failed to load report, {}, {}", reportEntity.getUserId(), reportEntity
                                .getTianjiType().name(), e);
                    }
                }
            }
            model.addAttribute("reportMap", dsReports);

            return "auth/cxb/tj_other_report";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/tj/callLog/{userId}", method = RequestMethod.GET)
    public String mobileCallLogPage(Model model, @PathVariable(value = "userId") Long userId) throws Exception {

        TianjiType tianjiType = TianjiType.MOBILE;

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, tianjiType);

        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            String  mobileRawReport = tianjiService.getMobileRawReport(reportEntity);
            Map<String, Object> reportMap = new ObjectMapper().readValue(mobileRawReport,
                    new TypeReference<Map<String, Object>>() {
                    });

            AuthUserEntity userEntity = authUserService.queryObject(userId);
            Map<String, String> contactsMap = new HashMap<>();
            contactsMap.putAll(authUserContactService.getContacts(userId));
            contactsMap.put(userEntity.getContact1Mobile(), userEntity.getContact1Name());
            contactsMap.put(userEntity.getContact2Mobile(), userEntity.getContact2Name());
            contactsMap.put(userEntity.getContact3Mobile(), userEntity.getContact3Name());

            Collection callLogs = (Collection) ((Map<String, Object>) ((Collection) reportMap.get("data_list")).iterator().next()).get("teldata");
            model.addAttribute("callLogs", callLogs);
            model.addAttribute("contactsMap", contactsMap);
        }

        return "auth/cxb/tj_call_log";
    }
    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/tj/{type}/{userId}", method = RequestMethod.GET)
    public String mobileReportPage(Model model, @PathVariable(value = "userId") Long userId,
                                   @PathVariable(value = "type") String type) throws Exception {

        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, tianjiType);

        boolean isv3 = false;
        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            String reportHtml = tianjiService.getHtmlReport(reportEntity);
            String reportJson = tianjiService.getJsonReport(reportEntity);

            String body = StringUtils.substringBeforeLast(StringUtils.substringAfter(reportHtml, "<body>"), "</div>") + "</div>";


            isv3 = StringUtils.indexOf(body, "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">") > 0;


            String bodyPart1 = "";

            String bodyPart2 = "";
            if (isv3) {

                bodyPart1 = StringUtils.substringBefore(body, "<div class=\"def_report_items def_report_7_columns\" id=\"call_log\">");
                bodyPart2 = "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">"
                        + StringUtils.substringAfter(body, "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">");
            } else {
                bodyPart1 = StringUtils.substringBefore(body, "<div class=\"def_report_items def_report_7_columns\" id=\"callLog\">");
                bodyPart2 = "<div class=\"sidebar-container\">" + StringUtils.substringAfter(body, "<div class=\"sidebar-container\">");
            }

            AuthUserEntity userEntity = authUserService.queryObject(userId);
            Map<String, String> contactsMap = new HashMap<>();
            contactsMap.putAll(authUserContactService.getContacts(userId));
            contactsMap.put(userEntity.getContact1Mobile(), userEntity.getContact1Name());
            contactsMap.put(userEntity.getContact2Mobile(), userEntity.getContact2Name());
            contactsMap.put(userEntity.getContact3Mobile(), userEntity.getContact3Name());

            Map<String, Object> reportMap = new ObjectMapper().readValue(reportJson,
                    new TypeReference<Map<String, Object>>() {
                    });
            Collection<Map<String, Object>> calls = (Collection<Map<String, Object>>) MapUtils.getObject(reportMap, "call_log");

            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"def_report_items def_report_8_columns\" id=\"callLog\"><h4>通讯记录</h4>" +
                    "<ul class=\"def_report_th\">" +
                    "<li>号码</li><li>互联网标识</li><li>类别标签</li><li>号码归属</li><li>最后通话时间</li><li>主叫情况</li>" +
                    "<li>被叫情况</li><li>短信情况</li></ul>");

            for (Map<String, Object> callLog : calls) {
                String phone = (String) callLog.get("phone");
                sb.append("<ul class=\"def_report_td\"><li>" + phone + "</li>");

                String nameLabel = "";
                String phoneLabel = (String) callLog.get("phone_label");
                if (StringUtils.isNotBlank(phoneLabel)) {
                    nameLabel +="<span class=\"label warning\">" + phoneLabel + "</span>";
                }
                String name = contactsMap.get(phone);
                if (StringUtils.isNotBlank(name)) {
                    nameLabel += ("<span>" + name + "</span>");
                }
                sb.append("<li>" + StringUtils.defaultIfBlank(nameLabel, "-")+ "</li>");
                sb.append("<li>" + StringUtils.defaultIfBlank(MapUtils.getString(callLog, "phone_info"), "-")+ "</li>");
                sb.append("<li>" + StringUtils.defaultIfBlank(MapUtils.getString(callLog, "phone_location"), "-")+ "</li>");
                sb.append("<li>" + StringUtils.replace(MapUtils.getString(callLog, "last_contact_date"), " ", "<br/>")+ "</li>");
                sb.append("<li>" + MapUtils.getInteger(callLog, "call_cnt", 0) + "次，<br/>"
                        + ((MapUtils.getInteger(callLog, "call_seconds", 0) * 100 / 60) / 100.0) + "分</li>");
                sb.append("<li>" + MapUtils.getInteger(callLog, "called_cnt", 0) + "次，<br/>"
                        + ((MapUtils.getInteger(callLog, "called_seconds", 0) * 100 / 60) / 100.0) + "分</li>");
                sb.append("<li>" + MapUtils.getInteger(callLog, "send_cnt", 0) + "次，<br/>"
                        + MapUtils.getInteger(callLog, "receive_cnt", 0) + "次</li>");
                sb.append("</ul>");
            }
            sb.append("</div>");
            if (!isv3) {
                sb.append("</div></div>");
            }

            model.addAttribute("reportHtml", bodyPart1 + sb.toString() + bodyPart2);
        }

        if (isv3) {
            return "auth/cxb/tj_" + StringUtils.lowerCase(type) + "_report_v3";
        }
        return "auth/cxb/tj_" + StringUtils.lowerCase(type) + "_report";
    }
    /**
     *   米兜综合报告
     */
    @RequestMapping(value = "/tj/comprehensive/{requestId}", method = RequestMethod.GET)
    public String comprehensivePagee(Model model, @PathVariable(value = "requestId")  Long requestId) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryObject(requestId);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());
        //获取运营商报告JSON
        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase("mobile"));

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(authRequestVO.getUserId(), tianjiType);
        AuthMobileReportVO   mobileVo  = new AuthMobileReportVO();   //运营商报告
        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            //获取运营商JSON格式数据
            String reportJson = tianjiService.getJsonReport(reportEntity);
            mobileVo = tianjiService.getMobileVo(reportJson);

        }

        //获取多头报告的JSON数据
        AuthYouDunReportVO  youduInfoVO = new AuthYouDunReportVO();
        String jsonPth  = authUserVO.getAuthReportJsonUrl();
        if(StringUtils.isNotBlank(jsonPth) && !StringUtils.startsWithIgnoreCase(jsonPth, "http"))
        {
              youduInfoVO =  youDunService.getYoudunInfo(jsonPth);
        }


        //通讯录风险
        Map<String, String> contactMap = authUserContactService.getContacts(authRequestVO.getUserId());
        if(contactMap!=null && contactMap.size() > 0)
        {
            mobileVo.setMaillist("有");
            mobileVo.setContactsClass1Cnt(contactMap.size());
        }
        else
        {
            mobileVo.setMaillist("无");
            mobileVo.setContactsClass1Cnt(0);
        }

        //借条平台风险
        AuthJieTiaoReportVO  jietiaoVO  = new  AuthJieTiaoReportVO();
        int effectApplyCnt = 0;
        if (authRequestVO != null) {
            for (YiXiangType type : Arrays.asList(YiXiangType.MIFANG, YiXiangType.JIEDAIBAO,
                    YiXiangType.JIEDAIBAO, YiXiangType.WUYOUJIETIAO, YiXiangType.YOUPINZHENG)) {
                YxReportEntity yxREntity = yxReportService.queryLatestByUserId(authRequestVO.getUserId(), type);
                if (yxREntity != null && yxREntity.getVerifyStatus() == VerifyStatus.SUCCESS) {

                    effectApplyCnt  = effectApplyCnt +1;
                }
            }
        }
        jietiaoVO.setEffectApplyCnt(effectApplyCnt);

        model.addAttribute("mobileVo", mobileVo);
        model.addAttribute("authRequestVO", authRequestVO);
        model.addAttribute("authUserVO", authUserVO);
        model.addAttribute("jietiaoVO", jietiaoVO);
        model.addAttribute("youduInfoVO", youduInfoVO);
        return "auth/cxb/tj_comprehensive";

    }




    /**
     *   米兜综合报告
     */
    @RequestMapping(value = "/tj/jsonComprehensive/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  jsonComprehensive(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {


        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        Date  createTime = authRequestVO.getCreateTime();
        if(createTime!=null)
        {
            //作为临时存储-评估时间
            String apptime= DateUtils.formateDate(createTime,"yyyy-MM-dd");
            authRequestVO.setContact3Mobile(apptime);
        }

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        //芝麻淘宝分
        String taobaoSesamePoints = authUserVO.getTaobaoSesamePoints();
        if(taobaoSesamePoints==null ||  "null".equals(taobaoSesamePoints) || "".equals(taobaoSesamePoints))
        {
            authUserVO.setTaobaoSesamePoints("");
            authUserVO.setContact2Mobile("未知");
        }
        else
        {
              int   points =  Integer.parseInt(taobaoSesamePoints);
              if(points <= 550)
              {
                  authUserVO.setContact2Mobile("较差");
              }
              else if(points > 550 && points<= 600)
              {
                  authUserVO.setContact2Mobile("中等");
              }
              else if(points > 600 && points<= 650)
              {
                  authUserVO.setContact2Mobile("良好");
              }
              else if(points > 650 && points<= 700)
              {
                  authUserVO.setContact2Mobile("优秀");
              }
              else if(points > 700)
              {
                  authUserVO.setContact2Mobile("极好");
              }

        }
        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }

        //获取运营商报告JSON
        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase("mobile"));

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(authRequestVO.getUserId(), tianjiType);
        AuthMobileReportVO   mobileVo  = new AuthMobileReportVO();   //运营商报告
        mobileVo.setIdCardCheck("5");//无法判断
        mobileVo.setMnoBaseInfoRiskList(2); //无法判断
        mobileVo.setSocialRiskList(2);
        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            //获取运营商JSON格式数据
        	try {
        		String reportJson = tianjiService.getJsonReport(reportEntity);
                mobileVo = tianjiService.getMobileVo(reportJson);
        	}catch(Exception e) {
        		logger.error("运营商报告解析异常",e);
        	}
            

        }


        //获取多头报告的JSON数据
        mobileVo.setDuotouRiskList(2);  //无法判断
        AuthYouDunReportVO  youduInfoVO = new AuthYouDunReportVO();
        String jsonPth  = authUserVO.getAuthReportJsonUrl();
        if(StringUtils.isNotBlank(jsonPth) && !StringUtils.startsWithIgnoreCase(jsonPth, "http"))
        {
            youduInfoVO =  youDunService.getYoudunInfo(jsonPth);

            mobileVo.setDuotouRiskList(youduInfoVO.getRiskList());   //多头风险
        }

        //通讯录风险
        mobileVo.setMobileRiskList(2);  //无法判断
        Map<String, String> contactMap = authUserContactService.getContacts(authRequestVO.getUserId());
        if(contactMap!=null && contactMap.size() > 0)
        {
            mobileVo.setMobileRiskList(0);
            if(contactMap.size() < 30) //通讯录小于30个 有风险
            {
                mobileVo.setMobileRiskList(1);
            }
            mobileVo.setMaillist("有");
            mobileVo.setContactsClass1Cnt(contactMap.size());
        }
        else
        {
            mobileVo.setMobileRiskList(1);
            mobileVo.setMaillist("无");
            mobileVo.setContactsClass1Cnt(1);
        }

        //借条平台风险 获取 灯塔-胡萝卜借贷报告JSON
        mobileVo.setJietiaoRiskList(2);
        AuthHuLuoboVO  authHuLuoboVO = new AuthHuLuoboVO();
        YiXiangType yiXiangType =  YiXiangType.valueOf(StringUtils.upperCase("huluobo"));
        YxReportEntity  xyReportEntity  =  yxReportService.queryLatestByUserId(authRequestVO.getUserId(),yiXiangType);
        if(xyReportEntity!=null)
        {
            //解析JSON封装数据
            authHuLuoboVO =   yiXiangService.getCompreByHuLuobo(xyReportEntity);
            mobileVo.setJietiaoRiskList(0);
            //借条是否有风险
            boolean  jietiaoFlag = authHuLuoboVO.isOverdueFlag();
            if(jietiaoFlag)
            {
                mobileVo.setJietiaoRiskList(1);
            }
        }

        //是否多头申请 = 大于等于20
        //手机号关联身份证个数 = 大于等于2
        //身份证关联手机号个数 = 大于等于2
        mobileVo.setPartnerCount(0);
        int idcCount = 0;
        int phoneCount = 0;
        Integer  idcIn = mobileVo.getIdcCount();
        if(idcIn!=null)
        {
            idcCount = idcIn.intValue();
        }
        Integer phoneIn = mobileVo.getPhoneCount();
        if(phoneIn!=null)
        {
            phoneCount = phoneIn.intValue();
        }
        if( idcCount >=2 ||  phoneCount>=2 )
        {
            mobileVo.setDuotouRiskList(1);   //多头风险
        }

        boolean ower = false;
        boolean isBuy = false;
        if(null != authRequestVO) {
            String merchantNo = authUserVO.getMerchantNo(); //商户号
            String merchanNo = authRequestVO.getMerchantNo();
            if(null!=merchantNo &&merchantNo.equals(merchanNo)) {
                ower = true;
            }
            Long assigneeId = authRequestVO.getAssigneeId();
            if(null != assigneeId) {
                isBuy = true;
            }

        }
        if(!ower && !isBuy) {
            if(null!=mobileVo) {
                mobileVo.setMobile("******");
            }
            if(null!=authRequestVO) {
                authRequestVO.setContact1Mobile("******");
                authRequestVO.setContact2Mobile("******");
                authRequestVO.setContact3Mobile("******");
                authRequestVO.setContact1Name("***");
                authRequestVO.setContact2Name("***");
                authRequestVO.setContact3Name("***");
                authRequestVO.setName("***");
                authRequestVO.setIdNo("***");
                authRequestVO.setQqNo("******");
                authRequestVO.setWechatNo("******");
            }
            if(null!=authUserVO) {
                authUserVO.setContact1Mobile("******");
                authUserVO.setContact2Mobile("******");
                authUserVO.setContact3Mobile("******");
                authUserVO.setContact1Name("***");
                authUserVO.setContact2Name("***");
                authUserVO.setContact3Name("***");
                authUserVO.setName("***");
                authUserVO.setIdNo("***");
                authUserVO.setQqNo("******");
                authUserVO.setWechatNo("******");
                authUserVO.setMobile("******");
            }
            if(null!=youduInfoVO) {
                youduInfoVO.setName("***");
            }
            if(null!=authHuLuoboVO) {

            }
            if(null!=txlenderList) {

            }

        }
        return R.ok().put("count", count)
                .put("mobileVo", mobileVo)
                .put("authRequestVO", authRequestVO)
                .put("authUserVO", authUserVO)
                .put("jietiaoVO", authHuLuoboVO)
                .put("txlenderList", txlenderList)
                .put("youduInfoVO", youduInfoVO);

    }

    /**
     *   有盾多头报告
     */
    @RequestMapping(value = "/yd/youdunReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  youdunReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }
        //商户是否开启多头
        boolean  flag  = channelService.isYouDaoCreditEnabled(authRequestVO.getMerchantNo());
        if(!flag)
        {
            //开启对头，产品要求提示
            return R.error("余额不足，请尽快充值!");
        }

        if(StringUtils.isBlank(authUserVO.getIdNo())){

            return R.error("用户未实名认证");
        }

        String reportRespStr = "";
        AuthUserReportDT authUserReportDT =  authUserReportDTService.queryByUserId(authUserVO.getUserId());
        if(authUserReportDT == null){

            Map<String,Object> result =  YouDunUtils.getDuoTouReport(authUserVO.getIdNo());

            int status = Integer.parseInt(result.get("status").toString());
            if(status == 1){
                AuthUserReportDT saveModel = new AuthUserReportDT();
                saveModel.setUserId(authUserVO.getUserId());
                saveModel.setRespStr(result.get("respStr").toString());
                saveModel.setStatus(status);
                saveModel.setCreateTime(DateUtils.formateDateTime(new Date()));
                authUserReportDTService.save(saveModel);
            }else{

                return R.error(result.get("msg").toString());
            }

        }else{
            reportRespStr = authUserReportDT.getRespStr();
        }


        AuthYouDunReportVO  youduInfoVO = YouDunDuoTouReportUtils.trans(reportRespStr);
        if(youduInfoVO == null){
            return R.error("获取数据出错");
        }

        //评估时间
        String time  = youduInfoVO.getLastModifiedTime();
        Date  now  = new Date();
        if(time!=null)
        {
            now = DateUtils.parseDate(time);
        }
        time = DateUtils.formateDate(now);
        youduInfoVO.setLastModifiedTime(time);

        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("youduInfoVO", youduInfoVO)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }


    /**
     *   米兜报告
     */
    @RequestMapping(value = "/md/midouReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  midouReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }

        //查询该用户所有的借款记录,倒序
        List<TxBaseEntity>  voList  =  txBaseService.queryBaseListByIdNo(authRequestVO.getIdNo());
        //借款次数
        int appCounts = 0;
        //最长逾期天数
        int  overdueLongDay=0;
        //逾期次数
        int overdueCounts=0;
        //最近一次逾期天数
        int lastOverdueDay=0;
        //最近一次是否逾期
        boolean ifOverdue = false;
        //最近一次逾期后正常还款次数
        int repayCounts = 0;

        if(voList!=null && !voList.isEmpty())
        {
             int num = 1;
             boolean  tempIfOverdue  = false; //是否逾期
             for(TxBaseEntity  entity : voList)
             {
                  int status = entity.getStatus().getValue();
                  if(4 == status)   // 4.已经逾期
                  {
                      tempIfOverdue = true;
                      //计算逾期天数
                      int tempOverdueLongDay  =  0;
                      Date  endDate = entity.getEndDate();
                      Date  repayDate  = entity.getRepayDate();
                      if(endDate!=null && repayDate!=null)
                      {
                          Long tempDay  = DateUtils.daysBetween(endDate,repayDate);
                          tempOverdueLongDay = tempDay.intValue();
                      }
                      //最长逾期天数
                      if(tempOverdueLongDay > overdueLongDay)
                      {
                          overdueLongDay = tempOverdueLongDay;
                      }

                      if( num==1) //最近一次是否逾期
                      {
                          ifOverdue = true;

                          //最近一次逾期天数
                          if(overdueCounts == 0)
                          {
                              lastOverdueDay =  tempOverdueLongDay;
                          }
                      }
                      //逾期次数
                      overdueCounts  = overdueCounts + 1;
                  }
                  else
                  {
                      if(!tempIfOverdue)
                      {
                          repayCounts = repayCounts + 1;
                      }
                  }
                 num = num + 1;

                  //借款周期
                 Date beginDate = entity.getBeginDate();
                 Date enDate = entity.getEndDate();
                 Long tempLongDay  = DateUtils.daysBetween(beginDate,enDate);
                 //临时存储
                 entity.setMerchantNo(tempLongDay.toString());

             }
            appCounts = voList.size();
        }

        AuthMidouReportVO  midouVo = new AuthMidouReportVO();
        midouVo.setAppCounts(appCounts);
        midouVo.setOverdueLongDay(overdueLongDay);
        midouVo.setOverdueCounts(overdueCounts);
        midouVo.setLastOverdueDay(lastOverdueDay);
        midouVo.setIfOverdue(ifOverdue);
        midouVo.setRepayCounts(repayCounts);

        midouVo.setBaseInfoList(voList);

        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("midouVo", midouVo)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }

    /**
     *   灯塔-胡萝卜借贷报告
     */
    @RequestMapping(value = "/dengta/jietiaoReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  jietiaoReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);
        
        /* update by wanghao 2019-04-14 如果 申请时间在本次修改之后,那么显示新颜的报告*/
        Date xinyanOnlineDate = DateUtils.parseDateTime("2019-04-14 00:00:00");
        if(authRequestVO.getCreateTime().getTime()>xinyanOnlineDate.getTime()) {
        	Long uid = authRequestVO.getUserId();
        	return R.ok().put("showXinYan", true).put("uid", uid);
        }
        
        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }

        //获取 灯塔-胡萝卜借贷报告JSON
        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase("mobile"));
        YiXiangType yiXiangType =  YiXiangType.valueOf(StringUtils.upperCase("huluobo"));

        YxReportEntity reportEntity  =  yxReportService.queryLatestByUserId(authRequestVO.getUserId(),yiXiangType);
        if(reportEntity ==null || StringUtils.isEmpty(reportEntity.getReportJsonPath()))
        {
            //如果无 灯塔-胡萝卜借贷报告  重新调用接口去获取
            reportEntity = yiXiangService.saveYxReportByHuoluobo(authUserVO.getUserId(),authUserVO.getMerchantNo(),authUserVO.getName(),authUserVO.getIdNo(),authUserVO.getMobile());
        }

        //解析JSON封装数据
        AuthHuLuoboVO  authHuLuoboVO =   yiXiangService.getHuLuoboVoInfo(reportEntity);


        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("authHuLuoboVO", authHuLuoboVO)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }


    /**
     *   电商-淘宝支付宝聚合
     */
    @RequestMapping(value = "/taobaopay/taobaopayReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  taobaopayReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }

        //获取 淘宝支付宝聚合SON
        YiXiangType yiXiangType =  YiXiangType.valueOf(StringUtils.upperCase("taobaopay"));

        YxReportEntity reportEntity  =  yxReportService.queryLatestByUserId(authRequestVO.getUserId(),yiXiangType);
        if(reportEntity==null ||  reportEntity.getReportJsonPath()==null  ||  "".equals(reportEntity.getReportJsonPath()))
        {
            return R.error("此用户未有电商数据，可提醒用户支付宝和淘宝进行认证!");
        }

        //解析JSON封装数据
        AuthTaobaoPayVO  taobaoPayVO =   yiXiangService.getAuthTaobaoPayVO(reportEntity);

        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("taobaoPayVO", taobaoPayVO)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }


    /**
     *   详情->通讯录报告
     */
    @RequestMapping(value = "/contact/contactReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  contactReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }


        //查询通讯录
        int contactSize = 0 ;
        Map<String, String> contactMap = authUserContactService.getContacts(authUserVO.getUserId());
        List<Map<String, String>> results = new ArrayList<>();
        if (MapUtils.isNotEmpty(contactMap)) {
            for (Map.Entry<String, String> entry : contactMap.entrySet()) {
                HashMap<String, String> contact = new HashMap<>();
                contact.put("mobile", entry.getKey());
                contact.put("name", entry.getValue());
                results.add(contact);
            }

            contactSize = contactMap.size();
        }

        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("contactSize", contactSize)
                .put("contacts", results)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }

    /**
     *   详情->跟踪日志
     */
    @RequestMapping(value = "/histories/historiesReport/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public R  historiesReport(Model model, @PathVariable(value = "uuid")  String uuid) throws Exception {

        AuthRequestEntity authRequestVO = authRequestService.queryByUuid(uuid);

        AuthUserEntity authUserVO = authUserService.queryObject(authRequestVO.getUserId());

        int count = authRequestService.queryRequestCount(authUserVO.getUserId(), authRequestVO.getRequestId());

        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUserVO!=null)
        {
            String merchantNo = authUserVO.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }

        //查询跟踪日志
        List<AuthRequestHistoryVO> histories = authRequestHistoryVOConverter.convert(
                authRequestHistoryService.queryHistories(authRequestVO.getRequestId()));

        return R.ok().put("count", count)
                .put("authRequestVO", authRequestVO)
                .put("histories", histories)
                .put("txlenderList", txlenderList)
                .put("authUserVO", authUserVO);
    }


    /**
     * 姓名脱敏
     * @param fullName
     * @return
     */
    public String chineseName(String fullName)
    {
        if(StringUtils.isBlank(fullName))
        {
                 return "";
        }
        String name = StringUtils.left(fullName, 1);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /*@RequestMapping(value = "/bqs/{type}/{userId}", method = RequestMethod.GET)
>>>>>>> .r524
    public String bqsMobileReportPage(Model model, @PathVariable(value = "userId") Long userId,
                                   @PathVariable(value = "type") String type) throws Exception {

        BaiqishiType baiqishiType = BaiqishiType.valueOf(StringUtils.upperCase(type));

        BqsReportEntity bqsReportEntity = bqsReportService.queryLatestByUserId(userId, baiqishiType);
        //如果白骑士认证记录为空 则说明可能是以前的申请单认证的是天机运营商
        if(bqsReportEntity==null){
            String typeStr="mobile";
            TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(typeStr));

            TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, tianjiType);

            boolean isv3 = false;
            if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                String reportHtml = tianjiService.getHtmlReport(reportEntity);
                String reportJson = tianjiService.getJsonReport(reportEntity);

                String body = StringUtils.substringBeforeLast(StringUtils.substringAfter(reportHtml, "<body>"), "</div>") + "</div>";


                isv3 = StringUtils.indexOf(body, "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">") > 0;


                String bodyPart1 = "";

                String bodyPart2 = "";
                if (isv3) {

                    bodyPart1 = StringUtils.substringBefore(body, "<div class=\"def_report_items def_report_7_columns\" id=\"call_log\">");
                    bodyPart2 = "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">"
                            + StringUtils.substringAfter(body, "<div class=\"def_report_items def_report_3_columns\" id=\"trip_info\">");
                } else {
                    bodyPart1 = StringUtils.substringBefore(body, "<div class=\"def_report_items def_report_7_columns\" id=\"callLog\">");
                    bodyPart2 = "<div class=\"sidebar-container\">" + StringUtils.substringAfter(body, "<div class=\"sidebar-container\">");
                }

                AuthUserEntity userEntity = authUserService.queryObject(userId);
                Map<String, String> contactsMap = new HashMap<>();
                contactsMap.putAll(authUserContactService.getContacts(userId));
                contactsMap.put(userEntity.getContact1Mobile(), userEntity.getContact1Name());
                contactsMap.put(userEntity.getContact2Mobile(), userEntity.getContact2Name());
                contactsMap.put(userEntity.getContact3Mobile(), userEntity.getContact3Name());

                Map<String, Object> reportMap = new ObjectMapper().readValue(reportJson,
                        new TypeReference<Map<String, Object>>() {
                        });
                Collection<Map<String, Object>> calls = (Collection<Map<String, Object>>) MapUtils.getObject(reportMap, "call_log");

                StringBuilder sb = new StringBuilder();
                sb.append("<div class=\"def_report_items def_report_8_columns\" id=\"callLog\"><h4>通讯记录</h4>" +
                        "<ul class=\"def_report_th\">" +
                        "<li>号码</li><li>互联网标识</li><li>类别标签</li><li>号码归属</li><li>最后通话时间</li><li>主叫情况</li>" +
                        "<li>被叫情况</li><li>短信情况</li></ul>");

                for (Map<String, Object> callLog : calls) {
                    String phone = (String) callLog.get("phone");
                    sb.append("<ul class=\"def_report_td\"><li>" + phone + "</li>");

                    String nameLabel = "";
                    String phoneLabel = (String) callLog.get("phone_label");
                    if (StringUtils.isNotBlank(phoneLabel)) {
                        nameLabel +="<span class=\"label warning\">" + phoneLabel + "</span>";
                    }
                    String name = contactsMap.get(phone);
                    if (StringUtils.isNotBlank(name)) {
                        nameLabel += ("<span>" + name + "</span>");
                    }
                    sb.append("<li>" + StringUtils.defaultIfBlank(nameLabel, "-")+ "</li>");
                    sb.append("<li>" + StringUtils.defaultIfBlank(MapUtils.getString(callLog, "phone_info"), "-")+ "</li>");
                    sb.append("<li>" + StringUtils.defaultIfBlank(MapUtils.getString(callLog, "phone_location"), "-")+ "</li>");
                    sb.append("<li>" + StringUtils.replace(MapUtils.getString(callLog, "last_contact_date"), " ", "<br/>")+ "</li>");
                    sb.append("<li>" + MapUtils.getInteger(callLog, "call_cnt", 0) + "次，<br/>"
                            + ((MapUtils.getInteger(callLog, "call_seconds", 0) * 100 / 60) / 100.0) + "分</li>");
                    sb.append("<li>" + MapUtils.getInteger(callLog, "called_cnt", 0) + "次，<br/>"
                            + ((MapUtils.getInteger(callLog, "called_seconds", 0) * 100 / 60) / 100.0) + "分</li>");
                    sb.append("<li>" + MapUtils.getInteger(callLog, "send_cnt", 0) + "次，<br/>"
                            + MapUtils.getInteger(callLog, "receive_cnt", 0) + "次</li>");
                    sb.append("</ul>");
                }
                sb.append("</div>");
                if (!isv3) {
                    sb.append("</div></div>");
                }

                model.addAttribute("reportHtml", bodyPart1 + sb.toString() + bodyPart2);
            }

            if (isv3) {
                return "auth/cxb/tj_" + StringUtils.lowerCase(typeStr) + "_report_v3";
            }
            return "auth/cxb/tj_" + StringUtils.lowerCase(typeStr) + "_report";
        }else{
            //白骑士认证记录不为空则取白骑士的报告
            String reportHtml=null;
            String returnHtml="auth/cxb/bqs_" + StringUtils.lowerCase(type) + "_report";
            if (bqsReportEntity != null && bqsReportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                reportHtml = baiQiShiService.getHtmlReport(bqsReportEntity);
                if(reportHtml!=null&&!"".equals(reportHtml)){
                    String body = StringUtils.substringBeforeLast(StringUtils.substringAfter(reportHtml, "<body>"), "</div>") + "</div>";
                    model.addAttribute("reportHtml", body);
                }else{
                    reportHtml = bqsReportEntity.getReportHtmlPath();
                    returnHtml="redirect:"+reportHtml;
                }
                //String reportJson = baiQiShiService.getJsonReport(bqsReportEntity);
            }
            return returnHtml;
        }
    }*/

    /**
     * 新获取运营商报告
     * @param
     * @param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/newTjJson/{type}/{requestUuid}", method = RequestMethod.GET)
    @ResponseBody
    public R mobileReportJson(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "type") String type, @PathVariable("requestUuid") String requestUuid) throws Exception {

        AuthRequestEntity authRequest = authRequestService.queryByUuid(requestUuid);
        AuthUserEntity authUser = authUserService.queryObject(authRequest.getUserId());
        if(authUser.isAuthOperatorStatus()){
            return R.ok().put("url",HttpContextUtils.getRequestBaseUrl()+"/autha/report/tianji/"+authUser.getUserId()+"?token="+request.getHeader("token"));
        }else{
            return R.error("未获取到运营商报告");
        }
        /*
        int count = authRequestService.queryRequestCount(authUser.getUserId(), authRequest.getRequestId());
        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));
        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUser!=null)
        {
            String merchantNo = authUser.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }
        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(authUser.getUserId(), tianjiType);
        

        if(reportEntity!=null&&reportEntity.getVerifyStatus()!=VerifyStatus.SUCCESS) {
        	try {
        		String taskId = reportEntity.getTaskId();
            	//根据searchId搜索已经认证成功的数据
            	TjReportEntity flowReport = this.tjReportService.querySuccessReportByTaskId(taskId, tianjiType);
            	if(flowReport!=null) {
            		reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
            		reportEntity.setSearchId(flowReport.getSearchId());
            		reportEntity.setTianjiState(flowReport.getTianjiState());
            		this.tjReportService.update(reportEntity);
            		reportEntity = tjReportService.queryLatestByUserId(authUser.getUserId(), tianjiType);
            	}
        	}catch(Exception e) {
        		logger.error("查看详情时,跟踪报告异常, taskId="+reportEntity.getTaskId());
        	}
        	
        }
        
        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            try {
                String reportJson = tianjiService.getJsonReport(reportEntity);
                //封装运营商实体
                AuthTjMobileVO authTjMobileVO=tjReportService.getTjMobileVo(reportJson);
                return R.ok().put("authTjMobileVO",authTjMobileVO)
                        .put("request", authRequest)
                        .put("count", count)
                        .put("txlenderList",txlenderList);
            }catch(Exception e) {
                return R.error("未获取到运营商报告");
            }
        }else {
            //return R.error("未获取到运营商报告");
            return R.ok();
        }*/


    }

    @RequestMapping(value = "/tianji/{userId}", method = RequestMethod.GET)
    public void TianJiOperatorReport(HttpServletRequest request, HttpServletResponse response,@PathVariable long userId) throws Exception
    {

        AuthUserTianJi authUserTianJi =  authUserTianJiService.queryByUserId(userId);
        if(authUserTianJi!=null && (authUserTianJi.getDataStatus() == 1)){

            String detailStrHtml = "";

            if(authUserTianJi.getDetailStatus() == 1){
                detailStrHtml = authUserTianJi.getDetailHtml();
            }else{
                JSONObject res = TianjiUtils.getTianjiReportDetail(userId+"",authUserTianJi.getOutUniqueId(),"html");

                System.out.println("===="+res.toString());
                JSONObject detailResponse = res.getJSONObject("tianji_api_tianjireport_detail_response");
                detailStrHtml = detailResponse.getString("html");

                AuthUserTianJi updateModel = new AuthUserTianJi();
                updateModel.setUserId(userId);
                updateModel.setDetailStatus(1);
                updateModel.setDataStatus(1);
                updateModel.setDetailHtml(detailStrHtml);
                updateModel.setDetailJson(detailResponse.getString("json"));
                authUserTianJiService.update(updateModel);
            }

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter write = response.getWriter();
            write.print(detailStrHtml);
            write.flush();
            write.close();
        }


    }

    /**
     * 通话详情
     * @param model
     * @param requestUuid
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/callLog/{requestUuid}", method = RequestMethod.GET)
    @ResponseBody
    public R mobileCallogJson(Model model, @PathVariable("requestUuid") String requestUuid) throws Exception {
        AuthRequestEntity authRequest = authRequestService.queryByUuid(requestUuid);
        AuthUserEntity authUser = authUserService.queryObject(authRequest.getUserId());
        int count = authRequestService.queryRequestCount(authUser.getUserId(), authRequest.getRequestId());
        TianjiType tianjiType = TianjiType.MOBILE;
        //查询出借人列表
        List<TxLenderEntity>  txlenderList = new ArrayList<TxLenderEntity>();
        //查找借款申请人用户信息
        if(authUser!=null)
        {
            String merchantNo = authUser.getMerchantNo(); //商户号

            Map<String, Object> map = new HashMap<>();
            map.put("merchantNo",merchantNo);
            Query query = new Query(map);
            //查询出借人
            txlenderList = txLenderService.queryListByMerchantNo(query);
        }


        AuthUserTianJi authUserTianJi =  authUserTianJiService.queryByUserId(authUser.getUserId());
        if(authUserTianJi!=null){

            String reportJson = authUserTianJi.getDetailJson();

            AuthTjMobileVO authTjMobileVO=tjReportService.getTjCallogVo(reportJson,authUser,authUserTianJi.getDataStr());
            return R.ok().put("authTjMobileVO",authTjMobileVO)
                    .put("request", authRequest)
                    .put("count", count)
                    .put("txlenderList",txlenderList);

        }else{
            return R.error("此用户未获取运营商数据");
        }
        /*
        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(authUser.getUserId(), tianjiType);
        if (reportEntity != null && reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
            try {
                String reportJson = tianjiService.getJsonReport(reportEntity);
                //获取通话记录json
                String  mobileRawReport = tianjiService.getMobileRawReport(reportEntity);

                //通话记录实体
                AuthTjMobileVO authTjMobileVO=tjReportService.getTjCallogVo(reportJson,authUser,mobileRawReport);
                return R.ok().put("authTjMobileVO",authTjMobileVO)
                        .put("request", authRequest)
                        .put("count", count)
                        .put("txlenderList",txlenderList);
            }catch(Exception e) {
                logger.error("failed to load calllog", e);
                return R.error("未获取到通话详情");
            }

        }else {
            return R.error("未获取到通话详情");
        }*/
    }
}
