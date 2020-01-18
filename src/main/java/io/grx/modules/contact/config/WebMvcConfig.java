package io.grx.modules.contact.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.grx.common.interceptor.MerchantNoInterceptor;
import io.grx.modules.contact.interceptor.AuthorizationInterceptor;
import io.grx.modules.contact.resolver.LoginUserHandlerMethodArgumentResolver;
import io.grx.modules.auth.interceptor.AuthUserInterceptor;
import io.grx.wx.interceptor.WechatSignInterceptor;
import io.grx.wx.interceptor.WechatSsoInterceptor;

/**
 * MVC配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-20 22:30
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Autowired
    private LoginUserHandlerMethodArgumentResolver loginUserHandlerMethodArgumentResolver;

    @Autowired
    private WechatSsoInterceptor wechatSsoInterceptor;

    @Autowired
    private MerchantNoInterceptor merchantNoInterceptor;

    @Autowired
    private AuthUserInterceptor authUserInterceptor;

    @Autowired
    private WechatSignInterceptor wechatSignInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(merchantNoInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/contact/**");
        registry.addInterceptor(wechatSsoInterceptor).addPathPatterns("/rcpt/**");
        registry.addInterceptor(wechatSsoInterceptor).addPathPatterns("/in_pay");
        registry.addInterceptor(authUserInterceptor).addPathPatterns("/auth/**");
        registry.addInterceptor(wechatSignInterceptor).addPathPatterns("/rcpt/**");
        registry.addInterceptor(wechatSignInterceptor).addPathPatterns("/auth/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
    }
}