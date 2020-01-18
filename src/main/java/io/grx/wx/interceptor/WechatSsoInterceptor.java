package io.grx.wx.interceptor;

import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.grx.common.utils.Constant;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;
import io.grx.modules.tx.service.TxUserTokenService;
import io.grx.wx.utils.WechatUtils;

@Component
public class WechatSsoInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TxUserService txUserService;

    @Autowired
    private TxUserTokenService txUserTokenService;

    @Autowired
    private WechatUtils wechatUtils;

    @Value("${wechat.enabled}")
    private boolean wechatLoginEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.contains(request.getServletPath(), "login")
                || StringUtils.contains(request.getServletPath(), "callback")) {
            return true;
        }

        if (!checkUser(request, response)) {
            return false;
        }

//        WxJsSign annotation = null;
//        if(handler instanceof HandlerMethod) {
//            annotation = ((HandlerMethod) handler).getMethodAnnotation(WxJsSign.class);
//        }
//
//        if (annotation != null) {
//            Map<String, String> parameters = wechatUtils.sign(HttpContextUtils.getUriFromRequest(request));
//
//            for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                request.setAttribute(entry.getKey(), entry.getValue());
//            }
//        }

        return true;
    }

    private boolean checkUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TxUserEntity userEntity = null;
//        String unionId = HttpContextUtils.getCookieValue(request, "unionId", "");
//        if (StringUtils.isNotBlank(unionId)) {
//            userEntity = txUserService.queryByWechatId(unionId);
//            if (userEntity != null) {
//                request.setAttribute("TX_USER", userEntity);
//                ShiroUtils.setTxUser(userEntity);
//            }
//        }
        if (logger.isDebugEnabled()) {
            logCookies(request);
        }
        String token = HttpContextUtils.getCookieValue(request, "txTokenId", "");
        if (StringUtils.isNotBlank(token)) {
            Long userId = txUserTokenService.getUserIdByToken(token);
            if (userId != null) {
                userEntity = txUserService.queryObject(userId);
                if (userEntity != null) {
                    request.setAttribute("TX_USER", userEntity);
                    ShiroUtils.setTxUser(userEntity);
                }
            }
        }

        if (userEntity == null || StringUtils.isBlank(userEntity.getName())) {
            String returnUrl = StringUtils.substringAfter(request.getRequestURI(), request.getContextPath());
            if (!StringUtils.startsWith(returnUrl, "/")) {
                returnUrl = "/" + returnUrl;
            }
            if (StringUtils.isNotBlank(request.getQueryString())) {
                returnUrl = returnUrl + "?" + request.getQueryString();
            }

            if (userEntity == null) {

                if (HttpContextUtils.isWechatClient(request) && wechatLoginEnabled) {
                    String stateId = UUIDGenerator.getUUID();

                    String loginUrl = wechatUtils.buildLoginUri(returnUrl, stateId);
                    logger.info("Redirect to wechat login in interceptor: " + loginUrl);
                    response.sendRedirect(loginUrl);
                } else {
                    String contextPath = request.getContextPath();
                    String loginUrl = contextPath + "/rcpt/login.html?returnUrl="
                            + URLEncoder.encode(returnUrl, Constant.ENCODING_UTF8);
                    if (StringUtils.isNotBlank(request.getQueryString())) {
                        loginUrl += "&" + request.getQueryString();
                    }
                    logger.debug("route to login: {}", loginUrl);
                    response.sendRedirect(loginUrl);
                }

                return false;
            }


            // if empty name
            if (!StringUtils.contains(request.getServletPath(), "index")
                    && !StringUtils.contains(request.getServletPath(), "bind_bank")) {
                String bindBankUrl = request.getContextPath() + "/rcpt/bind_bank.html";
                response.sendRedirect(bindBankUrl);
                return false;
            }
        }

        return true;
    }

    private void logCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.debug("cookie key={}, path={}, value={}", cookie.getName(), cookie.getPath(), cookie.getValue());
            }
        }
    }
}
