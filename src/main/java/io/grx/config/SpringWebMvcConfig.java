package io.grx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class SpringWebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 此功能解决vue打包后的项目运行
         */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
    }
}
