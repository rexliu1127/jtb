package io.grx.wx.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.grx.common.utils.HttpContextUtils;
import io.grx.wx.annotation.WxJsSign;
import io.grx.wx.utils.WechatUtils;

@Component
public class WechatSignInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private WechatUtils wechatUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpContextUtils.isWechatClient(request)) {
            WxJsSign annotation = null;
            if (handler instanceof HandlerMethod) {
                annotation = ((HandlerMethod) handler).getMethodAnnotation(WxJsSign.class);
            }

            if (annotation != null) {
                Map<String, String> parameters = wechatUtils.sign(HttpContextUtils.getUriFromRequest(request));

                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }

        return true;
    }

}
