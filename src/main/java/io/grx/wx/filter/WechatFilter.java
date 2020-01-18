package io.grx.wx.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.grx.wx.utils.WechatUtils;
import io.grx.common.utils.HttpContextUtils;
import io.grx.common.utils.UUIDGenerator;
import io.grx.modules.tx.entity.TxUserEntity;
import io.grx.modules.tx.service.TxUserService;

//
//@Order(1)
//@WebFilter(filterName = "wechatFilter", urlPatterns = "/wx/*")
public class WechatFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TxUserService txUserService;

    @Autowired
    private WechatUtils wechatUtils;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        TxUserEntity userEntity = null;
        String unionId = HttpContextUtils.getCookieValue(request, "unionId", "");
        if (StringUtils.isNotBlank(unionId)) {
            userEntity = txUserService.queryByWechatId(unionId);
            if (userEntity != null) {
                request.setAttribute("TX_USER", userEntity);
            }
        }

        if (userEntity == null) {
            request.setAttribute("returnUrl", request.getRequestURI() + "/" + request.getQueryString());

            String stateId = UUIDGenerator.getUUID();

            String loginUrl = wechatUtils.buildLoginUri(null, stateId);
            logger.info("Redirect to wechat login: " + loginUrl);
            ((HttpServletResponse) servletResponse).sendRedirect(loginUrl);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
