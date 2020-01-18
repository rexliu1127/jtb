package io.grx.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Context 工具类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月29日 下午11:45:51
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
	public static ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		if (applicationContext != null) {
			return applicationContext.getBean(name);
		}
		return null;
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		if (applicationContext != null) {
			return applicationContext.getBean(name, requiredType);
		}
		return null;
	}

	public static boolean containsBean(String name) {
		return applicationContext != null && applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext != null && applicationContext.isSingleton(name);
	}

	public static Class<? extends Object> getType(String name) {
		if (applicationContext != null) {
			return applicationContext.getType(name);
		}
		return null;
	}

}