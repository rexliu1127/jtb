package io.grx.config;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.grx.modules.sys.oauth2.OAuth2Filter;
import io.grx.modules.sys.oauth2.OAuth2Realm;

/**
 * Shiro配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-20 18:33
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST,DispatcherType.FORWARD);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/contact/**", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/sys/register", "anon");
        filterMap.put("/sys/access_token", "anon");
        filterMap.put("/sms_code*", "anon");
        filterMap.put("/upload*", "anon");
        filterMap.put("/**/*.css", "anon");
        filterMap.put("/**/*.js", "anon");
        filterMap.put("/**/*.html", "anon");
        filterMap.put("/**/*.png", "anon");
        filterMap.put("/**/*.jpg", "anon");
        filterMap.put("/**/*.svg", "anon");
        filterMap.put("/**/*.gif", "anon");
        filterMap.put("/**/*.woff", "anon");
        filterMap.put("/**/*.ttf", "anon");
        filterMap.put("/fonts/**", "anon");
        filterMap.put("/plugins/**", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/wx_*", "anon");
        filterMap.put("/rcpt/**", "anon");
//        filterMap.put("/jxl/**", "anon");
//        filterMap.put("/tongdun/**", "anon");
        filterMap.put("/auth/**", "anon");
        filterMap.put("/tj/**", "anon");
        filterMap.put("/bqs/**", "anon");
        filterMap.put("/yx/**", "anon");
        filterMap.put("/yd/**", "anon");
        filterMap.put("/flow/**", "anon");
        filterMap.put("/integration/**", "anon");
        filterMap.put("/spread/**", "anon");
        filterMap.put("/in_pay", "anon");
        filterMap.put("/api/v1/**", "anon");	//移动端api
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/autha/machine_contact/**", "anon");
        filterMap.put("/", "anon");
        filterMap.put("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
