package io.grx.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.grx.common.utils.HttpContextUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.grx.common.utils.Constant;
import io.grx.common.utils.ShiroUtils;

import io.grx.common.utils.Constant;

/**
 * 在url中找出商家的merchantNo
 * 每个商家使用二级域名merchantNo.xxx.com
 */
@Component
public class MerchantNoInterceptor extends HandlerInterceptorAdapter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            ShiroUtils.setMerchantNo(Constant.DEFAULT_MERCHANT_NO);
        } catch (Throwable t) {
            logger.info("uri has error: {}", request.getRequestURI());
        }

        // check machineId
        String machineId = request.getHeader("MACHINE-ID");
        logger.debug("MACHINE-ID id in http header: {}", machineId);
        if (StringUtils.isBlank(machineId)) {
            machineId = request.getParameter(Constant.COOKIE_MACHINE_ID);
            logger.debug("MACHINE-ID in parameter: {}", machineId);
        }

        if (StringUtils.isNotBlank(machineId)) {
            HttpContextUtils.addCookie(response, Constant.COOKIE_MACHINE_ID, machineId, 60 * 60 * 24 * 30);
        } else {
            machineId = HttpContextUtils.getCookieValue(Constant.COOKIE_MACHINE_ID, "");
            logger.debug("MACHINE-ID in cookie: {}", machineId);
        }
        request.setAttribute(Constant.COOKIE_MACHINE_ID, machineId);

        if (StringUtils.isNotBlank(machineId)) {
            if (HttpContextUtils.isAndroidClient(request)) {
                request.setAttribute("isAndroidApp", true);
            } else if (HttpContextUtils.isIosClient(request)) {
                request.setAttribute("isIosApp", true);
            }
        }

        return true;
    }
}
