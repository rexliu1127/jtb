package io.grx.common.mybatis.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grx.common.utils.Constant;
import io.grx.common.utils.ShiroUtils;
import io.grx.common.utils.SpringContextUtils;
import io.grx.modules.sys.entity.SysUserEntity;
import io.grx.modules.sys.service.SysConfigService;
import io.grx.modules.sys.service.SysUserRoleService;

@Intercepts({@Signature(
        type= Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                CacheKey.class, BoundSql.class}),
        @Signature(
                type= Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class QueryDefaultParamInterceptor implements Interceptor {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String KEY_MERCHANT_NO = "_merchantNo";
    private static final String KEY_IS_CS = "_isCS";
    private static final String KEY_IS_DATA_PRIVACY = "_isDatePrivacy";

    SysUserRoleService sysUserRoleService;
    SysConfigService sysConfigService;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object parameterObject = invocation.getArgs()[1];

        Map<String, Object> params = null;
        if (parameterObject != null) {
            if (parameterObject instanceof Map) {
                params = (Map<String, Object>) parameterObject;
            }
        } else {
            params = new HashMap<String, Object>();
            Object[] args = invocation.getArgs();
            args[1] = params;
        }

        if (sysUserRoleService == null) {
            sysUserRoleService = (SysUserRoleService) SpringContextUtils.getBean
                    ("sysUserRoleService");
        }
        if (sysConfigService == null) {
            sysConfigService = (SysConfigService) SpringContextUtils.getBean
                    ("sysConfigService");
        }

        if (params != null) {
            if (ShiroUtils.isInitiated()) {
                try {
                    // add new parameter: _merchantNo
                    params.put(KEY_MERCHANT_NO, ShiroUtils.getMerchantNo());

                    SysUserEntity sysUser = ShiroUtils.getUserEntity();
                    if (sysUser != null) {
                        params.put("_sysUserId", sysUser.getUserId());
                        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(sysUser.getUserId());
                        if (roleIdList.contains(Constant.CS_ROLE_ID)) {
                            params.put(KEY_IS_CS, true);
                        }
                        if (roleIdList.contains(Constant.DATA_PRIVACY_ROLE_ID)) {
                            params.put(KEY_IS_DATA_PRIVACY, true);
                        }
                    }
                } catch (Exception e) {
                    logger.info("Failed to put default parameters", e.getMessage());
                }
            }

        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}