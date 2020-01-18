package io.grx.wx.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.grx.common.service.SmsCodeService;
import io.grx.common.service.impl.LuosimaoService;
import io.grx.common.utils.CharUtils;
import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;
import io.grx.modules.tx.service.TxUserTokenService;
import io.grx.wx.utils.RestUtils;
import io.grx.wx.utils.WechatUtils;

@Controller
@RequestMapping("/rcpt")
public class WechatSsoController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(WechatSsoController.class);

    @Autowired
    private WechatUtils wechatUtils;

    @Autowired
    private RestUtils restUtils;

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserTokenService txUserTokenService;

    @Autowired
    private SmsCodeService smsCodeService;

    @Autowired
    private LuosimaoService luosimaoService;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String wxLogin(Model model, String unionId, String openId,
                          String nickName, String headImgUrl, String returnUrl, String inviter) {
        model.addAttribute("returnUrl", returnUrl);
        model.addAttribute("unionId", unionId);
        model.addAttribute("openId", openId);
        model.addAttribute("nickName", nickName);
        model.addAttribute("headImgUrl", headImgUrl);
        model.addAttribute("inviterCode", inviter);

        model.addAttribute("needCaptcha", luosimaoService.needCaptcha());
        model.addAttribute("captchaKey", luosimaoService.getSiteKey());

        return TX_TEMPLATE_PATH + "login";
    }

    @RequestMapping(value = "/wx_callback.html")
    public String callback(Model model, HttpServletRequest request,
                           HttpServletResponse response, String returnUrl,
                           String code, String state) {
        try {
            String authResult = restUtils.get(wechatUtils.buildAuthUri(code), null);
            logger.info("auth result: " + authResult);
            JSONObject authJson = new JSONObject(authResult);

            String openId = checkAndGetValue(authJson, "openid");
            String unionId = checkAndGetValue(authJson, "unionid");
            String nickName = StringUtils.EMPTY;
            String headImgUrl = StringUtils.EMPTY;

            if (StringUtils.isNotBlank(openId)) {

                TxUserEntity user = null;
                if (HttpContextUtils.isWechatClient(request)) {
                    user = txUserService.queryByWechatId(openId);
                } else {
                    user = txUserService.queryByUnionId(unionId);
                }

                if (user == null) {
                    String accessToken = authJson.getString("access_token");

                    String userInfo = restUtils.get(wechatUtils.buildUserInfoUrl(accessToken, openId), null);
                    logger.info("User info: " + userInfo);

                    JSONObject userInfoJson = new JSONObject(userInfo);

                    nickName = CharUtils.toValid3ByteUTF8String(checkAndGetValue(userInfoJson, "nickname"));
                    headImgUrl = checkAndGetValue(userInfoJson, "headimgurl");

                } else {
                    String token = txUserTokenService.createToken(user.getUserId());
                    HttpContextUtils.addCookie(response, "txTokenId", token, 60 * 60 * 24 * 7);

                    if (StringUtils.isNotBlank(returnUrl)) {
                        return "redirect:" + returnUrl;
                    } else {
                        return "redirect:/rcpt/index.html";
                    }
                }
            }

            String originalQuery = "";
            if (StringUtils.containsIgnoreCase(returnUrl, "/index.html")) {
                originalQuery = StringUtils.substringAfter(returnUrl, "?");
            }

            return "redirect:/rcpt/login.html?returnUrl=" + StringUtils.defaultString(returnUrl)
                    + "&unionId=" + StringUtils.defaultString(unionId)
                    + "&openId=" + StringUtils.defaultString(openId)
                    + "&nickName=" + URLEncoder.encode(StringUtils.defaultString(nickName), Constant.ENCODING_UTF8)
                    + "&headImgUrl=" + StringUtils.defaultString(headImgUrl)
                    + "&" + originalQuery;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String checkAndGetValue(JSONObject json, String key) throws Exception {
        if (json.has(key)) {
            return json.getString(key);
        }
        return StringUtils.EMPTY;
    }
}
