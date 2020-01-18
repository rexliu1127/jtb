package io.grx.auth.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import io.grx.auth.service.YouDunService;
import io.grx.common.utils.*;
import io.grx.modules.auth.entity.AuthRequestEntity;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.entity.AuthUserOCREntity;
import io.grx.modules.auth.entity.AuthUserOCRRequestLogEntity;
import io.grx.modules.auth.service.AuthRequestService;
import io.grx.modules.auth.service.AuthUserOCRRequestLogService;
import io.grx.modules.auth.service.AuthUserOCRService;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.sys.service.SysFunDetailsService;
import io.grx.youdun.YouDunUtils;
import io.grx.youdun.model.OCRQueryModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/yd")
public class YouDunController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private YouDunService youDunService;

    @Autowired
    private SysFunDetailsService sysFunDetailsService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthUserOCRService authUserOCRService;
    @Autowired
    private AuthUserOCRRequestLogService authUserOCRRequestLogService;

    @Autowired
    private AuthRequestService authRequestService;

    @Value("${youdun.userPrefix}")
    private String userPrefix;

    @Autowired
    private Environment env;


    @RequestMapping(value = "/ocr/return")
    public String OCRReturn(HttpServletRequest request, Model model){

        System.out.println("===============OCRReturn=================");

        String partner_order_id = request.getParameter("partner_order_id");
        String result_auth = request.getParameter("result_auth");
        String result_status = request.getParameter("result_status");
        String errorcode = request.getParameter("errorcode");
        String message = request.getParameter("message");

        if(StringUtils.isNotBlank(partner_order_id) && StringUtils.isNotBlank(result_auth) && StringUtils.isNotBlank(result_status)){

            AuthUserOCRRequestLogEntity authUserOCRRequestLogEntity =  authUserOCRRequestLogService.queryByOrderId(partner_order_id);
            if(authUserOCRRequestLogEntity!=null){

                if(authUserOCRRequestLogEntity.getStatus() == 0 ){

                    if("T".equals(result_auth) && "01".equals(result_status)){


                        //修改日志状态
                        authUserOCRRequestLogService.updateStatus(partner_order_id,1);

                        //查询OCR结果
                        String str = YouDunUtils.queryOCRResult(partner_order_id);

                        OCRQueryModel ocrQueryModel =  JSONObject.parseObject(str, OCRQueryModel.class);
                        if(ocrQueryModel!=null){



                            //认证通过
                            authUserService.updateAuthStatus(authUserOCRRequestLogEntity.getUserId(),
                                    ocrQueryModel.getData().getId_name(),
                                    ocrQueryModel.getData().getId_number(),
                                    true);

                            AuthRequestEntity  authRequestEntity =  authRequestService.queryLatestByUserId(authUserOCRRequestLogEntity.getUserId(),0L);

                            //修改请求修改
                            AuthRequestEntity authRequestEntityUpdModel = new AuthRequestEntity();
                            authRequestEntityUpdModel.setRequestId(authRequestEntity.getRequestId());
                            authRequestEntityUpdModel.setName(ocrQueryModel.getData().getId_name());
                            authRequestEntityUpdModel.setIdNo(ocrQueryModel.getData().getId_number());
                            authRequestEntityUpdModel.setMerchantNo(authRequestEntity.getMerchantNo());
                            authRequestService.update(authRequestEntityUpdModel);


                            AuthUserOCREntity authUserOCREntity = new AuthUserOCREntity();
                            authUserOCREntity.setUserId(authUserOCRRequestLogEntity.getUserId());
                            authUserOCREntity.setIdUrl1("data:image/jpeg;base64,"+ocrQueryModel.getData().getIdcard_front_photo());
                            authUserOCREntity.setIdUrl2("data:image/jpeg;base64,"+ocrQueryModel.getData().getIdcard_back_photo());
                            authUserOCREntity.setIdUrl3("data:image/jpeg;base64,"+ocrQueryModel.getData().getLiving_photo());
                            authUserOCREntity.setCreateTime(DateUtils.formateDateTime(new Date()));
                            authUserOCRService.save(authUserOCREntity);
                        }
                    }
                }

                message = "认证成功!";

            }else{
                message = "请求异常,联系客服";
            }
        }else{
            message = "请求异常,联系客服";
        }

        model.addAttribute("msg",message);

        return "auth/usercenter/idCard_result";
    }




    /**
     * 认证return
     *
     *
     * @return
     */
    @RequestMapping(value = "/return/{userId}")
    public String tjReturn(@PathVariable(value = "userId") Long userId, HttpServletRequest request,
                           String partner_order_id, String result_auth, String result_status, String errorcode, String message) {
        Enumeration<String> eu = request.getParameterNames();
        while (eu.hasMoreElements()) {
            String key = eu.nextElement();
            logger.debug("YD return {} = {}", key, Arrays.asList(request.getParameterValues(key)));
        }

        logger.debug("userId={}", userId);
        logger.debug("partner_order_id={}", partner_order_id);
        logger.debug("result_auth={}", result_auth);
        logger.debug("result_status={}", result_status);
        logger.debug("errorcode={}", errorcode);
        logger.debug("message={}", message);

        if (StringUtils.equalsIgnoreCase(result_auth, "T")) {
            // 认证通过(相似度>0.7且人像比对结果为01)
            AuthUserEntity userEntity = authUserService.queryObject(userId);
            if (userEntity != null) {
                userEntity.setAuthStatus(true);
                userEntity.setAuthTaskId(partner_order_id);
                authUserService.update(userEntity);
            }

            return "redirect:/auth/apply.html";
        } else {
            return "redirect:/auth/apply.html?error=yd";
        }
    }

    /**
     * 认证return
     *
     *
     * @return
     */
    @RequestMapping(value = "/callback")
    @ResponseBody
    public String callback(HttpServletRequest request,
                           String partner_order_id, String idcard_front_photoUrl, String idcard_back_photoUrl,
                           String living_photo_url, String loan_url, String verify_status,
                           String id_number, String id_name,String jsonPath) {
        Map<String, String> params = new HashMap<>();
        params.put("partner_order_id", partner_order_id);
        params.put("idcard_front_photoUrl", idcard_front_photoUrl);
        params.put("idcard_back_photoUrl", idcard_back_photoUrl);
        params.put("living_photo_url", living_photo_url);
        params.put("loan_url", loan_url);
        params.put("verify_status", verify_status);
        params.put("id_number", id_number);
        params.put("id_name", id_name);
        params.put("jsonPath", jsonPath);

        logger.debug("YD callback parameter: {}", new Gson().toJson(params));

        if (StringUtils.startsWith(partner_order_id, userPrefix + "-")) {

            List<AuthUserEntity> userEntities = authUserService.queryByAuthTaskId(partner_order_id);
            if (CollectionUtils.isNotEmpty(userEntities)) {
                userEntities.forEach(userEntity -> {
                    if (StringUtils.equalsIgnoreCase(verify_status, "1")) {
                        userEntity.setAuthStatus(true);
                    } else {
                        userEntity.setAuthStatus(false);
                    }
                    userEntity.setIdNo(id_number);
                    userEntity.setName(id_name);
//            userEntity.setAuthTaskId(partner_order_id);
                    userEntity.setIdUrl1(idcard_front_photoUrl);
                    userEntity.setIdUrl2(idcard_back_photoUrl);
                    userEntity.setIdUrl3(living_photo_url);
                    userEntity.setAuthReportUrl(loan_url);
                    userEntity.setAuthReportJsonUrl(jsonPath);  //多头JSON格式报告URL
                    authUserService.update(userEntity);

                    //扣费
                    sysFunDetailsService.saveFunDetailsByDuoTou(userEntity);
                });
            } else {
                logger.warn("Not found order id: {}", partner_order_id);
            }

        } else {
            try {
                String prefix = StringUtils.contains(partner_order_id, "-") ?
                        StringUtils.substringBefore(partner_order_id, "-") : "MDT";

                String otherHost = env.getProperty(String.format("youdun.otherHost%1$s", prefix));

                if (StringUtils.isNotBlank(otherHost)) {
                    logger.debug("{}={}", String.format("youdun.otherHost%1$s", prefix), otherHost);


                    final HttpResponse res = HttpUtils.doPost(otherHost, MapUtils.EMPTY_MAP, params);
                    String responseStr = EntityUtils.toString(res.getEntity(), Constant.ENCODING_UTF8);
                    logger.info("callback result: {}", responseStr);
                } else {
                    logger.warn("not found redirect site for prefix: {}", prefix);
                }

            } catch (Exception e) {
                logger.error("failed to send callback", e);
            }
        }

        return "success";
    }
}
