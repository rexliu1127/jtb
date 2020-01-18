package io.grx.modules.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import io.grx.common.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.ShiroUtils;
import io.grx.modules.auth.entity.AuthUserEntity;
import io.grx.modules.auth.service.AuthUserService;
import io.grx.modules.auth.service.AuthUserTokenService;
import io.grx.modules.opt.entity.ChannelEntity;
import io.grx.modules.opt.service.ChannelService;

@Component
public class AuthUserInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthUserTokenService authUserTokenService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private ChannelService channelService;

    public static final String HEADER_CHANNEL_KEY = "CHANNEL-KEY";
    public static final String HEADER_AUTH_USER_TOKEN = "AUTH-USER-TOKEN";


    public static final String PARAM_CHANNEL_ID = "channelId";

    public static final String COOKIE_AUTH_TOKEN_ID = "token";
    public static final String COOKIE_CHANNEL_KEY = "channelKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.contains(request.getServletPath(), "callback.html")) {
            return true;
        }

        ChannelEntity channel = handleAuthChannel(request, response);

        if (StringUtils.containsIgnoreCase(request.getRequestURI(), "login")
                && !StringUtils.containsIgnoreCase(request.getRequestURI(), "_qq_")) {
//            HttpContextUtils.addCookie(response, COOKIE_CHANNEL_KEY, "", 0);
            return true;
        }
        String uri = request.getRequestURI();


        if(StringUtils.containsIgnoreCase(uri, "auth/product")||
                StringUtils.containsIgnoreCase(uri,"checkToken") ||
                StringUtils.containsIgnoreCase(uri,"auth/invite.html") ||
                StringUtils.containsIgnoreCase(uri,"register")||
                StringUtils.containsIgnoreCase(uri,"downapp.html")||
                StringUtils.containsIgnoreCase(uri,"index.html")) {
        	return true;
        }


/*
        if (channel == null) {
            response.sendRedirect(HttpContextUtils.getRequestBaseUrl() + "/auth/login.html");
            return false;
        }

        String channelKeyInCookie = HttpContextUtils.getCookieValue(COOKIE_CHANNEL_KEY, "");
        if (StringUtils.isNotBlank(channelKeyInCookie) && !StringUtils.equalsIgnoreCase(channelKeyInCookie, channel.getChannelKey())) {
            response.sendRedirect(HttpContextUtils.getRequestBaseUrl() + "/auth/login.html?" + request.getQueryString());
            return false;
        }
*/
        return handleAuthUser(request, response, channel);
    }

    private boolean handleAuthUser(HttpServletRequest request, HttpServletResponse response, ChannelEntity channel)
            throws Exception {
        String authTokenId = request.getHeader(HEADER_AUTH_USER_TOKEN);
        if (StringUtils.isBlank(authTokenId)) {
            authTokenId = request.getHeader(COOKIE_AUTH_TOKEN_ID);
        }
        if (StringUtils.isBlank(authTokenId)) {
            authTokenId = HttpContextUtils.getCookieValue(request, COOKIE_AUTH_TOKEN_ID, "");
        }

        logger.debug("authTokenId=" +authTokenId);
        AuthUserEntity userEntity = null;
        if (StringUtils.isNotBlank(authTokenId)) {
            Long userId = authUserTokenService.getUserIdByToken(authTokenId);

            if (userId != null) {
                userEntity = authUserService.queryObject(userId);

                if(userEntity != null){

                    return true;
                }else{

                    return false;
                }

                /*
                if (userEntity != null) {
                    if (channel != null && !StringUtils.equalsIgnoreCase(userEntity.getMerchantNo(), channel.getMerchantNo())) {
                        HttpContextUtils.addCookie(response, COOKIE_AUTH_TOKEN_ID, "", 0);

                        logger.info("cookie user is not same as merchant no. userId: {}, mobile: {}, merchantNo: {}, channelId: {}",
                                userEntity.getUserId(), userEntity.getMobile(), userEntity.getMerchantNo(), channel.getChannelId());
                        userEntity = null;

                        return false;
                    } else {
                        ShiroUtils.setAuthUser(userEntity);
                        request.setAttribute("AUTH_USER", userEntity);
                    }
                }*/
            }
        }
/*
        if (userEntity == null) {
            response.sendRedirect(HttpContextUtils.getRequestBaseUrl() + "/auth/login.html?" + request.getQueryString());
            return false;
        }
*/
        return true;
    }

    private ChannelEntity handleAuthChannel(HttpServletRequest request, HttpServletResponse response) {

        String channelKey = request.getHeader(HEADER_CHANNEL_KEY);
        if (StringUtils.isBlank(channelKey)) {
             channelKey = request.getParameter(PARAM_CHANNEL_ID);
            logger.debug("CHANNEL_KEY in parameter: {}", channelKey);
        } else {
            logger.debug("CHANNEL_KEY in header: {}", channelKey);
        }

        if (StringUtils.isBlank(channelKey)) {
            channelKey = HttpContextUtils.getCookieValue(COOKIE_CHANNEL_KEY, "");
            logger.debug("CHANNEL_KEY in cookie: {}", channelKey);
        } else {
            HttpContextUtils.addCookie(response, COOKIE_CHANNEL_KEY, channelKey, 60 * 60 * 24);
        }

        ChannelEntity channel = null;
        if (StringUtils.isNotBlank(channelKey)) {
            channel = channelService.queryByKey(channelKey);

            if (channel != null && channel.getStatus() != null && channel.getStatus()  == 0) {
                channel = null;
            }

            if (channel != null) {
                ShiroUtils.setSessionAttribute(Constant.KEY_CHANNEL, channel);
                request.setAttribute(Constant.KEY_CHANNEL, channel);
            }

        }

        return channel;
    }
}
