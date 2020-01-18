package io.grx.pay.controller;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import io.grx.pay.saaspay.SaasPayService;
import io.grx.pay.service.YeePayService;
import io.grx.pay.yeepay.YeepayService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.QRCodeUtils;
import io.grx.common.utils.R;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.pay.entity.PayRecordEntity;
import io.grx.modules.pay.entity.PayScanRecordEntity;
import io.grx.modules.pay.enums.PayScanStatus;
import io.grx.modules.pay.enums.PayStatus;
import io.grx.modules.pay.service.PayRecordService;
import io.grx.modules.pay.service.PayScanRecordService;
import io.grx.modules.tx.entity.TxBaseEntity;
import io.grx.modules.tx.entity.TxExtensionEntity;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.enums.ExtensionStatus;
import io.grx.modules.tx.enums.TxStatus;
import io.grx.modules.tx.service.TxBaseService;
import io.grx.modules.tx.service.TxExtensionService;
import io.grx.modules.tx.service.TxMessageService;
import io.grx.modules.tx.service.TxUserService;
import io.grx.pay.service.AllinPayService;
import io.grx.wx.annotation.WxJsSign;
import io.grx.wx.utils.RestUtils;
import io.grx.wx.utils.WechatUtils;
import net.jodah.expiringmap.ExpiringMap;

@Controller
public class WxPayController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxBaseService txBaseService;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private AllinPayService allinPayService;

    @Autowired
    private YeePayService yeePayService;

    @Autowired
    private PayRecordService payRecordService;

    @Autowired
    private PayScanRecordService payScanRecordService;

    @Autowired
    private SaasPayService saasPayService;

    @Value("${allinpay.name}")
    private String productName;

//    @Autowired
//    private CacheUtils cacheUtils;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private TxExtensionService txExtensionService;

    @Autowired
    private TxMessageService txMessageService;

    private static ExpiringMap<String, Object> PAY_CACHE = ExpiringMap.builder()
            .variableExpiration()
            .build();

    /**
     * 认证登录页
     *
     * @return
     */
    @RequestMapping(value = "/in_pay", method = RequestMethod.GET)
    public String login(Model model, String type, Long id, String returnUrl, String successUrl, String payType) {

        TxUserEntity user = ShiroUtils.getTxUser();
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("successUrl", successUrl);
        model.addAttribute("type", type);
        model.addAttribute("id", id);

//        if (HttpContextUtils.isMobileWechat(HttpContextUtils.getHttpServletRequest())) {
//            String loginUrl = wechatUtils.buildScanPayLoginUri(type + ":" + id + ":" + user.getUserId(),
//                    returnUrl, successUrl);
//            logger.info("login to get open id: {}", loginUrl);
//            return "redirect:" + loginUrl;
//        }
//
        return doPay(model, user, type, id, user.getWechatId(), null, payType, returnUrl, successUrl);
    }

    private String doPay(Model model, TxUserEntity user, String type, Long id, String openId, String uuid, String payType, String returnUrl, String successUrl) {
        boolean error = false;
        String errorMessage = "";
        String noticeType = "warn";

        long amount = 0;
        String orderNo = "";
        String trxId = UUIDGenerator.getUUID();
        String title = "";
        String remark = "";


        PayRecordEntity payRecordEntity = new PayRecordEntity();
        if (StringUtils.equalsIgnoreCase(type, "tx")) {
            TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(id);
            if (txBaseEntity == null) {
                errorMessage = "非法请求";
                error = true;
            }

            if (!error && txBaseEntity.getCreateUserId().equals(txBaseEntity.getBorrowerUserId())
                    && txBaseEntity.getStatus() != TxStatus.UNPAID && txBaseEntity.getStatus() != TxStatus.REJECTED) {
                noticeType = "info";
                errorMessage = "该订单已支付过";
                error = true;
            }

            if (!error && txBaseEntity.getCreateUserId().equals(txBaseEntity.getLenderUserId())
                    && txBaseEntity.getStatus() != TxStatus.NEW && txBaseEntity.getStatus() != TxStatus.REJECTED) {

                noticeType = "info";
                errorMessage = "该订单已支付过";
                error = true;
            }

            if (!error && (txBaseEntity.getFeeAmount() == null || txBaseEntity.getFeeAmount().doubleValue() <= 0)) {
                errorMessage = "非法请求";
                error = true;
            }

            amount = (long) (txBaseEntity.getFeeAmount().doubleValue() * 100);

            orderNo = "TX" + txBaseEntity.getTxUuid();

            title = productName;

            payRecordEntity.setTxId(txBaseEntity.getTxId());

            model.addAttribute("txId", txBaseEntity.getTxId());
            model.addAttribute("amount", txBaseEntity.getFeeAmount());
            model.addAttribute("orderNo", orderNo);
        } else if (StringUtils.equalsIgnoreCase(type, "ex")) {
            TxExtensionEntity txExtensionEntity = txExtensionService.queryObject(id);
            if (txExtensionEntity == null) {
                errorMessage = "非法请求";
                error = true;
            } else if (txExtensionEntity.getStatus() != ExtensionStatus.NEW) {
                noticeType = "info";
                errorMessage = "该订单已支付过";
                error = true;
            }

            if (!error && (txExtensionEntity.getFeeAmount() == null || txExtensionEntity.getFeeAmount().doubleValue() <= 0)) {
                errorMessage = "非法请求";
                error = true;
            }


            amount = (long) (txExtensionEntity.getFeeAmount().doubleValue() * 100);

            TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(txExtensionEntity.getTxId());

            orderNo = "EX" + txBaseEntity.getTxUuid() + StringUtils.leftPad(String.valueOf(txExtensionEntity
                    .getExtensionId()), 3, "0");

            title = productName;

            payRecordEntity.setExtensionId(txExtensionEntity.getExtensionId());

            model.addAttribute("extensionId", txExtensionEntity.getExtensionId());
            model.addAttribute("amount", txExtensionEntity.getFeeAmount());
            model.addAttribute("orderNo", orderNo);
        }

        if (!error) {
//            boolean isMobileWechat = HttpContextUtils.isMobileWechat(HttpContextUtils.getHttpServletRequest());
//            model.addAttribute("isMobileWechat", isMobileWechat);
//            if (isMobileWechat) {
//                model.addAttribute("payUuid", uuid);
//
//                Map<String, String> payInfo = (Map<String, String>) PAY_CACHE.get(orderNo);
//
//                if (payInfo != null) {
//                    model.addAttribute("payinfo", payInfo);
//                } else {
//                    try {
////                        Map<String, String> response = allinPayService.payByWx(amount, orderNo, title, remark, openId,
////                                HttpContextUtils.getRequestBaseUrl() + "/wx_pay_callback");
//                        Map<String, String> response = yeePayService.payByWx(amount, trxId, title, remark, openId,
//                                "",
//                                HttpContextUtils.getRequestBaseUrl() + "/wx_pay_callback");
//
//                        logger.info("pay response: {}", response);
//                        payRecordEntity.setAmount((int) amount);
//                        payRecordEntity.setOrderNo(orderNo);
//                        payRecordEntity.setTrxId(trxId);
//                        payRecordEntity.setAccountId(openId);
//                        payRecordEntity.setPayUserId(user.getUserId());
//
//                        if (!StringUtils.endsWithIgnoreCase(MapUtils.getString(response, "code"), "00000")) {
//                            error = true;
//
//                            errorMessage = MapUtils.getString(response, "message");
//                        }
//
//                        if (!error) {
//                            String payInfoStr = response.get("resultData");
//                            payInfo = new ObjectMapper().readValue(payInfoStr,
//                                    new TypeReference<Map<String, Object>>() {
//                                    });
//
//                            model.addAttribute("payinfo", payInfo);
//
////                            payRecordEntity.setTrxId(MapUtils.getString(response, "trxid"));
////                            payRecordEntity.setChnlTrxId(MapUtils.getString(response, "chnltrxid"));
////                            payRecordEntity.setRequestTrxStatus(MapUtils.getString(response, "trxstatus"));
////                            payRecordEntity.setFinTime(MapUtils.getString(response, "fintime"));
////                            payRecordEntity.setTrxId(MapUtils.getString(response, "trxid"));
//                            payRecordEntity.setCreateTime(new Date());
//
//                            payRecordService.save(payRecordEntity);
//
//                            PAY_CACHE.put(orderNo, payInfo, ExpiringMap.ExpirationPolicy.CREATED, 60 * 10, TimeUnit
//                                    .SECONDS);
////                        cacheUtils.put(orderNo, payInfoStr, 60 * 2);
//                        }
//
//                        model.addAttribute("payUuid", uuid);
//                    } catch (Exception e) {
//                        logger.error("Failed to pay", e);
//                        error = true;
//                        errorMessage = e.getMessage();
//                    }
//                }
//            } else {
//                String payUUid = UUIDGenerator.getUUID();
////                cacheUtils.put("scan_pay_id_" + payUUid, String.valueOf(id), 60 * 15);
////                cacheUtils.put("scan_pay_type_" + payUUid, type, 60 * 15);
////                cacheUtils.put("scan_pay_user_" + payUUid, String.valueOf(user.getUserId()), 60 * 15);
////                cacheUtils.put("scan_pay_status_" + payUUid, "0", 60 * 15);
////                cacheUtils.put("scan_pay_orderNo_" + payUUid, orderNo, 60 * 15);
//
//                PayScanRecordEntity scan = new PayScanRecordEntity();
//                scan.setPayId(payUUid);
//                scan.setOrderNo(orderNo);
//                scan.setOrderId(id);
//                scan.setPayType(type);
//                scan.setPayUserId(user.getUserId());
//                scan.setCreateTime(new Date());
//                payScanRecordService.save(scan);
//
//                model.addAttribute("payUuid", payUUid);
//            }

            payRecordEntity.setAmount((int) amount);
            payRecordEntity.setOrderNo(orderNo);
            payRecordEntity.setTrxId(trxId);
            payRecordEntity.setAccountId(openId);
            payRecordEntity.setPayUserId(user.getUserId());
            payRecordEntity.setCreateTime(new Date());
            payRecordService.save(payRecordEntity);

            String baseUrl = StringUtils.substringBefore(HttpContextUtils.getRequestBaseUrl(),
                    HttpContextUtils.getHttpServletRequest().getContextPath());
            Map<String, String> saasPayParams = saasPayService.payment(title, payType, amount, trxId, "",
                    baseUrl + successUrl,
                    HttpContextUtils.getRequestBaseUrl() + "/wx_pay_callback",
                    baseUrl + returnUrl);

            for (Map.Entry<String, String> entry : saasPayParams.entrySet()) {
                model.addAttribute(entry.getKey(), entry.getValue());
            }

            return "saas_pay";
        }

        if (error) {
            model.addAttribute("type", noticeType);
            model.addAttribute("title", "支付失败");
            model.addAttribute("message", errorMessage);
            return "notice";
        }
        return "pay_detail";
    }

    /**
     * @return
     */
    @RequestMapping(value = "/wx_pay_callback")
    public void wxPayCallback(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<String, String>();

        Enumeration headerNames = request.getParameterNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        logger.info("callback parameter map: {}", map);

//        String responseStr = map.get("response");
//        //开始解密
//        Map<String,String> jsonMap  = new HashMap<>();
//        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
//        dto.setCipherText(responseStr);
//
//        //不读取json文件的写法
//        //InternalConfig internalConfig = InternalConfig.Factory.getInternalConfig();
//        //PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
//        //PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
//
//        PrivateKey privateKey = YeepayService.getSecretKey();
//        PublicKey publicKey =YeepayService.getPublicKey();
//
//        dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
//        logger.info("decrypted result: {}", dto.getPlainText());
//        jsonMap = parseResponse(dto.getPlainText());
//
//        String trxId = jsonMap.get("orderId");
//        String uniqueOrderNo = jsonMap.get("uniqueOrderNo");
//        String status = jsonMap.get("status");
//        String paySuccessDate = jsonMap.get("paySuccessDate");
        String trxId = map.get("orderNo");
        String status = map.get("status");
        String payTime = map.get("payTime");

        PayRecordEntity payRecordEntity = payRecordService.queryByTrxId(trxId);
        if (payRecordEntity != null) {
            if (payRecordEntity.getStatus() != PayStatus.NEW) {
                logger.info("duplicated callback");
                return;
            }

            payRecordEntity.setPayTrxStatus(status);
            payRecordEntity.setPayTime(payTime);

            PAY_CACHE.remove(payRecordEntity.getOrderNo());

            if (StringUtils.equalsIgnoreCase(status, "1")) {

                payRecordEntity.setStatus(PayStatus.COMPLETE);
                payRecordService.update(payRecordEntity);

                TxUserEntity payUser = txUserService.queryObject(payRecordEntity.getPayUserId());
                if (payRecordEntity.getExtensionId() != null) {
                    TxExtensionEntity txExtensionEntity = txExtensionService.queryObject(payRecordEntity
                            .getExtensionId());

                    txExtensionService.updateStatus(txExtensionEntity, ExtensionStatus.CONFIRMED, payUser);

                    try {
                        txMessageService.sendMsgForConfirmingExtension(txExtensionEntity);
                    } catch (Throwable t) {
                        logger.error("failed to send TX message for confirm extension", t);
                    }

                } else if (payRecordEntity.getTxId() != null) {
                    TxBaseEntity txBaseEntity = txBaseService.queryObjectNoAcl(payRecordEntity.getTxId());

                    if (txBaseEntity.getStatus() == TxStatus.UNPAID) {
                        txBaseEntity.setStatus(TxStatus.NEW);
                        txBaseService.update(txBaseEntity);
                    } else if (txBaseEntity.getStatus() == TxStatus.NEW || txBaseEntity.getStatus() == TxStatus.REJECTED) {
                        txBaseService.confirmTx(txBaseEntity, payUser);
                    }

                }
            } else {
                payRecordEntity.setStatus(PayStatus.CANCELLED);
                payRecordService.update(payRecordEntity);
            }
        } else {
            logger.warn("Not found pay record by {}", map);
            throw new RuntimeException("The orderNo doesn't exist");
        }

        try {
//            response.getWriter().write("SUCCESS");
            response.getWriter().write("OK");
        } catch (Exception e) {
            throw new RuntimeException("Error in reponse: ", e);
        }
    }

    /**
     * 二维码
     */
    @RequestMapping("/scan_pay/{uuid}.png")
    public void getScanPayQrcode(HttpServletResponse response, @PathVariable("uuid") String uuid) throws
            ServletException, IOException {
        response.setContentType("image/png");

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();

            String text = HttpContextUtils.getRequestBaseUrl() + "/wx_scan_pay?uuid=" + uuid;
            QRCodeUtils.createQRImage(text, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    @RequestMapping("/wx_scan_pay")
    public String scanPay(String uuid) throws
            ServletException, IOException {
        PayScanRecordEntity scan = payScanRecordService.queryObject(uuid);
        String type = scan.getPayType();
        String idStr = String.valueOf(scan.getOrderId());
        String userIdStr = String.valueOf(scan.getPayUserId());

        String loginUrl = wechatUtils.buildScanPayLoginUri(type + ":" + idStr + ":" + userIdStr + ":" + uuid, null,
                null);
        logger.info("login to get open id: {}", loginUrl);
        return "redirect:" + loginUrl;
    }

//    @RequestMapping(value = "/wx_pay_login_callback")
//    public String callback(Model model, HttpServletRequest request,
//                           HttpServletResponse response, String returnUrl, String successUrl,
//                           String code, String state) {
//        try {
//
//            model.addAttribute("returnUrl", returnUrl);
//            model.addAttribute("successUrl", successUrl);
//
//            String authResult = restUtils.get(wechatUtils.buildAuthUri(code), null);
//            logger.info("auth result: " + authResult);
//            JSONObject authJson = new JSONObject(authResult);
//
//            String openId = checkAndGetValue(authJson, "openid");
//            logger.info("payer open id: {}", openId);
//
//            String[] values = StringUtils.split(state, ":");
//
//            String type = values[0];
//            Long id = NumberUtils.toLong(values[1]);
//            Long userId = NumberUtils.toLong(values[2]);
//
//            String uuid = null;
//            if (values.length > 3) {
//                uuid = values[3];
//            }
//
//            return doPay(model, txUserService.queryObject(userId), type, id, openId, uuid);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    private String checkAndGetValue(JSONObject json, String key) throws Exception {
        if (json.has(key)) {
            return json.getString(key);
        }
        return StringUtils.EMPTY;
    }

    @RequestMapping(value = "/wx_pay_status")
    @ResponseBody
    public Map<String, Object> checkPayStatus(String uuid) {

        PayScanRecordEntity scan = payScanRecordService.queryObject(uuid);

        if (scan == null || (System.currentTimeMillis() - scan.getCreateTime().getTime() > 15 * 60 * 1000)) {
            return R.error("支付请求已过期");
        }

        if (scan.getStatus() == PayScanStatus.COMPLETE) {
            return R.ok("支付成功").put("paid", true);
        }

        if (scan.getStatus() == PayScanStatus.PROCESSING) {
            return R.ok("正在支付").put("paid", false);
        }

        return R.error("支付失败");
    }

    /**
     * 认证登录页
     *
     * @return
     */
    @WxJsSign
    @RequestMapping(value = "/wx_pay_result", method = RequestMethod.GET)
    public String payResult(Model model, String type, String title,
                            String message, String forwardUrl, boolean showMore, String uuid) {
        model.addAttribute("type", StringUtils.defaultString(type, "info"));
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        model.addAttribute("showMore", showMore);
        model.addAttribute("forwardUrl", forwardUrl);
        model.addAttribute("isWechat", HttpContextUtils.isWechatClient(HttpContextUtils.getHttpServletRequest()));

        PayScanRecordEntity scan = payScanRecordService.queryObject(uuid);
        if (scan != null) {
            if (StringUtils.equalsIgnoreCase(type, "success")) {
                scan.setStatus(PayScanStatus.COMPLETE);
//            cacheUtils.put("scan_pay_status_" + uuid, "1", 60 * 15);
            } else {
                scan.setStatus(PayScanStatus.FAILED);
//            cacheUtils.put("scan_pay_status_" + uuid, "2", 60 * 15);
            }
            payScanRecordService.update(scan);
        }

        return "pay_result";
    }


    public static Map<String, String> parseResponse(String response){

        Map<String,String> jsonMap  = new HashMap<>();
        jsonMap	= JSON.parseObject(response,
                new com.alibaba.fastjson.TypeReference<TreeMap<String,String>>() {});

        return jsonMap;
    }
}
