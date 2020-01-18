package io.grx.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HttpContextUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpContextUtils.class);

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static String getRequestBaseUrl() {
        final HttpServletRequest request = getHttpServletRequest();

        StringBuilder sb = new StringBuilder(request.getScheme() + "://" +
                request.getServerName() +
                ("http".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 80
                        || "https".equalsIgnoreCase(request.getScheme()) && request.getServerPort() == 443 ? ""
                        : ":" + request.getServerPort()));
        String contextPath = request.getContextPath();
        if (StringUtils.length(contextPath) > 1) {
            sb.append(contextPath);
        }
        return sb.toString();
    }

    public static String getUriFromRequest(final HttpServletRequest request) {
        final String url = ((HttpServletRequest) request).getRequestURL().toString();
        final String queryString = ((HttpServletRequest) request).getQueryString();

        final StringBuilder result = new StringBuilder(url);
        if (StringUtils.isNotBlank(queryString)) {
            result.append("?").append(queryString);
        }
        return result.toString();
    }

    public static void addCookie(HttpServletResponse response, String key, String value, int maxAge) {
        Cookie cookie = new Cookie(key, value);

        cookie.setMaxAge(maxAge);
        cookie.setPath(getHttpServletRequest().getContextPath());

        response.addCookie(cookie);
        logger.debug("Set cookie. key={}, value={}, maxAge={}", key, value, maxAge);
    }

    public static String getCookieValue(HttpServletRequest request, String key, String defaultValue) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (StringUtils.equalsIgnoreCase(c.getName(), key)) {
                    return c.getValue();
                }
            }
        }
        return defaultValue;
    }

    public static String getCookieValue(String key, String defaultValue) {
        HttpServletRequest request = getHttpServletRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (StringUtils.equalsIgnoreCase(c.getName(), key)) {
                    return c.getValue();
                }
            }
        }
        return defaultValue;
    }

    public static boolean isWechatClient(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return StringUtils.contains(userAgent, "micromessenger");
    }

    public static boolean isAndroidOrIos(HttpServletRequest request) {
        return isAndroidClient(request) || isIosClient(request);
    }

    public static boolean isMobileWechat(HttpServletRequest request) {
        return isAndroidOrIos(request) && isWechatClient(request);
    }

    public static boolean isAndroidClient(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        return StringUtils.contains(userAgent, "android");
    }

    public static boolean isIosClient(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();

        return StringUtils.contains(userAgent, "iphone")
                || StringUtils.contains(userAgent, "ipod")
                || StringUtils.contains(userAgent, "ipad");
    }

    public static boolean isMidouAppClient(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent").toLowerCase();

        return StringUtils.contains(userAgent, "app/midou");
    }
}

