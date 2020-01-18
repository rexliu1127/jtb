package io.grx.common.mybatis.interceptor;

import java.util.Properties;

import io.grx.common.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grx.common.utils.ReflectUtil;
import io.grx.common.utils.ShiroUtils;

@Intercepts({@Signature(
        type= Executor.class,
        method = "update",
        args = {MappedStatement.class,Object.class})})
public class UpdateDefaultParamInterceptor implements Interceptor {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String KEY_MERCHANT_NO = "merchantNo";

    @Override
    public Object intercept(final Invocation invocation) throws Throwable {
        Object paramObj = invocation.getArgs()[1];
        if (paramObj != null) {
            String merchantNo = (String) ReflectUtil.getVal(paramObj, KEY_MERCHANT_NO);
            if (StringUtils.isBlank(merchantNo)) {
                if (StringUtils.startsWith(paramObj.getClass().getSimpleName(), "TX")) {
                    ReflectUtil.setVal(paramObj, KEY_MERCHANT_NO, Constant.DEFAULT_MERCHANT_NO);
                } else {
                    ReflectUtil.setVal(paramObj, KEY_MERCHANT_NO, ShiroUtils.getMerchantNo());
                }
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(final Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(final Properties properties) {

    }
}
