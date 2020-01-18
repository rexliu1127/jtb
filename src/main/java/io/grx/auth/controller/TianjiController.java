package io.grx.auth.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.grx.auth.entity.requestModel.RequestContactNameListModel;
import io.grx.auth.entity.requestModel.RequestContactNameModel;
import io.grx.common.utils.DateUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.modules.auth.entity.AuthUserTianJi;
import io.grx.modules.auth.service.AuthUserTianJiService;
import io.grx.tianji.TianjiUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import io.grx.auth.service.TianjiService;
import io.grx.common.utils.R;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.TjReportEntity;
import io.grx.modules.auth.enums.TianjiState;
import io.grx.modules.auth.enums.TianjiType;
import io.grx.modules.auth.enums.VerifyStatus;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.TjReportService;

@Controller
@RequestMapping("/tj")
public class TianjiController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${tianji.userPrefix}")
    private String userPrefix;

    @Autowired
    private TjReportService tjReportService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private TianjiService tianjiService;

    @Autowired
    private AuthUserTianJiService authUserReportTJService;


    private Interner<String> pool;

    public TianjiController() {
        pool = Interners.newWeakInterner();
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> test(HttpServletRequest request, @RequestBody RequestContactNameListModel model){

        System.out.println(model.toString());
        return R.ok();
    }

    /**
     * 回调结果显示
     * @return
     */
    @RequestMapping(value = "/operator/return", method = RequestMethod.GET)
    public String operatorReturn(){

        return "auth/tj_return";
    }

    /**
     * 数据通知
     * @return
     */
    @RequestMapping(value = "/operator/notify",consumes = "multipart/form-data", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> operatorNotify(HttpServletRequest request) {

        String search_id = request.getParameter("search_id");
        String outUniqueId = request.getParameter("outUniqueId");
        String userId = request.getParameter("userId");
        String state = request.getParameter("state");
        String account = request.getParameter("account");
        String accountType = request.getParameter("accountType");

        System.out.println("接收到的参数：search_id为："
                + search_id + "outUniqueId为：" + outUniqueId
                + "userId为：" + userId + "state为：" + state
                + "account为：" + account + "accountType为：" + accountType);

        if (StringUtils.isBlank(search_id)) {
            logger.info("运营商回调失败：header not found");
            //writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "header not found: search_id");
            return R.error("header not found: search_id");
        }

        if (!"report".equals(state)) {
            logger.info("运营商回调状态错误：state is " + state);
            //writeMessage(response, HttpServletResponse.SC_BAD_REQUEST, "state is " + state);
            return R.error("state is " + state);
        }

        try {


            AuthUserTianJi authUserReportTJ =  authUserReportTJService.queryByUserId(Long.valueOf(userId));
            if(authUserReportTJ!=null && authUserReportTJ.getDataStatus() == 0){

                net.sf.json.JSONObject result = TianjiUtils.getTianjiData(search_id);
                logger.info("TJ运营商数据获取" + result);
                if (result.getInt("error") == 200) {


                    AuthUserEntity authUserEntity =  authUserService.queryObject(authUserReportTJ.getUserId());
                    if(authUserEntity!=null){

                        //更新认证结果
                        AuthUserEntity authUserEntityUpd = new AuthUserEntity();
                        authUserEntityUpd.setUserId(authUserReportTJ.getUserId());
                        authUserEntityUpd.setAuthStatus(true);
                        authUserEntityUpd.setAuthOperatorStatus(true);
                        authUserEntityUpd.setMerchantNo(authUserEntity.getMerchantNo());
                        authUserService.update(authUserEntity);
                    }



                    //修改认证记录
                    AuthUserTianJi model = new AuthUserTianJi();
                    model.setUserId(authUserReportTJ.getUserId());
                    model.setDataStatus(1);
                    model.setUpdateTime(DateUtils.formateDateTime(new Date()));
                    model.setSearchId(search_id);
                    model.setDataStr(result.getJSONObject("wd_api_mobilephone_getdatav2_response").getString("data"));
                    authUserReportTJService.update(model);
                } else {
                    logger.info("TJ运营商数据获取失败" + result);
                }

            }

        }catch (Exception ex){

        }
        return R.ok();
    }

   /**
    * 天机认证return
    *
    * @return
    */
    @RequestMapping(value = "/return/{type}", method = RequestMethod.GET)
    public String tjReturn(@PathVariable(value = "type") String type,
                        String userId, String search_id, String outUniqueId, String state, HttpServletRequest request) {
        logger.info("Tianji return. type: {}, userId: {}, outUniqueId: {}, search_id: {}, state: {}",
                type, userId, outUniqueId, search_id, state);

        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));
        TianjiState tianjiState = TianjiState.valueOf(StringUtils.upperCase(state));

        synchronized (pool.intern(outUniqueId)) {
            TjReportEntity reportEntity = tjReportService.queryByUniqueId(outUniqueId);

            if (reportEntity == null) {
                reportEntity = new TjReportEntity();

                Long authUserId = NumberUtils.toLong(StringUtils.substringAfter(userId, userPrefix));
                AuthUserEntity userEntity = authUserService.queryObject(authUserId);
                reportEntity.setCreateTime(new Date());
                reportEntity.setUserId(userEntity.getUserId());
                reportEntity.setTaskId(outUniqueId);
                reportEntity.setIdNo(userEntity.getIdNo());
                reportEntity.setName(userEntity.getName());
                reportEntity.setMobile(userEntity.getMobile());
                reportEntity.setTianjiType(tianjiType);
                reportEntity.setSearchId(search_id);
            } else if (reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS
                    && StringUtils.isNotBlank(reportEntity.getSearchId())) {
                logger.info("Duplicated return. ignore it.");

                if (HttpContextUtils.isMidouAppClient(request)) {
                    return "redirect:/return_android.html";
                }

                return "redirect:/auth/apply.html";
            }

            reportEntity.setTianjiState(tianjiState);
            reportEntity.setUpdateTime(new Date());

            if (tianjiState == TianjiState.CRAWL_FAIL || tianjiState == TianjiState.REPORT_FAIL) {
                reportEntity.setVerifyStatus(VerifyStatus.FAILED);
            } else {
                reportEntity.setVerifyStatus(VerifyStatus.PROCESSING);
            }

            if (tianjiState == TianjiState.REPORT) {
                reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
            }

            if (reportEntity.getId() == null) {
                tjReportService.save(reportEntity);
            } else {
                tjReportService.update(reportEntity);
            }
        }

        if (HttpContextUtils.isMidouAppClient(request)) {
            return "redirect:/return_android.html";
        }

        return "redirect:/auth/apply.html";
    }


    /**
     * 天机认证return
     *
     * @return
     */
    @RequestMapping(value = "/callback/{type}")
    @ResponseBody
    public String callback(@PathVariable(value = "type") String type,
                         String userId, String outUniqueId, String state, String account,
                         String search_id, String accountType, String errorReasonDetail) {
        logger.info("Tianji callback. type: {}, userId: {}, outUniqueId: {}, state: {}",
                type, userId, outUniqueId, state);
        logger.info("Tianji callback. account: {}, search_id: {}, accountType: {}, errorReasonDetail: {}",
                account, search_id, accountType, errorReasonDetail);

        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));
        TianjiState tianjiState = TianjiState.valueOf(StringUtils.upperCase(state));

        synchronized (pool.intern(outUniqueId)) {
            TjReportEntity reportEntity = tjReportService.queryByUniqueId(outUniqueId);

            if (reportEntity == null) {
                reportEntity = new TjReportEntity();

                Long authUserId = NumberUtils.toLong(StringUtils.substringAfter(userId, userPrefix));
                AuthUserEntity userEntity = authUserService.queryObject(authUserId);
                reportEntity.setCreateTime(new Date());
                reportEntity.setUserId(userEntity.getUserId());
                reportEntity.setTaskId(outUniqueId);
                reportEntity.setIdNo(userEntity.getIdNo());
                reportEntity.setName(userEntity.getName());
                reportEntity.setTianjiType(tianjiType);
                reportEntity.setSearchId(search_id);
            } else if (reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS
                    && StringUtils.isNotBlank(reportEntity.getSearchId())) {
                logger.info("Duplicated callback. ignore it.");
                return "success";
            }

            reportEntity.setTianjiState(tianjiState);
            reportEntity.setUpdateTime(new Date());
            reportEntity.setSearchId(search_id);

            if (tianjiState == TianjiState.CRAWL_FAIL || tianjiState == TianjiState.REPORT_FAIL) {
                reportEntity.setVerifyStatus(VerifyStatus.FAILED);
            } else {
                reportEntity.setVerifyStatus(VerifyStatus.PROCESSING);
            }

            // download report
            if (tianjiState == TianjiState.REPORT) {
                reportEntity.setVerifyStatus(VerifyStatus.SUCCESS);
            }
            if (reportEntity.getId() == null) {
                tjReportService.save(reportEntity);
            } else {
                tjReportService.update(reportEntity);
            }

            if (tianjiState == TianjiState.REPORT && tianjiType == TianjiType.MOBILE) {
//                tianjiService.getMobileRawReport(reportEntity);
            }
        }

        return "success";
    }

    /**
     * 天机认证return
     *
     * @return
     */
    @RequestMapping(value = "/report_entity/{type}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public R mobielReport(@PathVariable(value = "userId") Long userId,
                               @PathVariable(value = "type") String type) {

        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, tianjiType);

        return R.ok().put("reportEntity", reportEntity);
    }

    /**
     *
     *
     * @return
     */
    @RequestMapping(value = "/report_url/{type}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public R mobileReportUrl(@PathVariable(value = "userId") Long userId,
                          @PathVariable(value = "type") String type) {

        TianjiType tianjiType = TianjiType.valueOf(StringUtils.upperCase(type));

        TjReportEntity reportEntity = tjReportService.queryLatestByUserId(userId, tianjiType);

        if (reportEntity != null) {
            if (reportEntity.getVerifyStatus() == VerifyStatus.SUCCESS) {
                String url = tianjiService.getReportUrl(reportEntity);
                return R.ok().put("url", url);
            } else if (reportEntity.getVerifyStatus() == VerifyStatus.FAILED) {
                return R.error("认证失败. 请通知客户重新提交认证.");
            } else {
                return R.error("报告正在生成中");
            }
        }

        return R.error("用户还未提交认证!");
    }

}
