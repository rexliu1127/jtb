package io.grx.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;


/**
 * 生成验证码配置
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-20 19:22
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "5");
        properties.put("kaptcha.textproducer.font.names", "Arial");
        properties.put("kaptcha.textproducer.char.length", "4");
        properties.put("kaptcha.background.clear.from", "white");
        properties.put("kaptcha.image.width", "120");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.textproducer.font.size", "32");
        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
