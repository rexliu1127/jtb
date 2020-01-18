package io.grx.common.service;

import io.grx.common.utils.SpringContextUtils;
import org.apache.commons.lang.ClassUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
public class SmsServiceFactory {

    @Value("${sms.name}")
    private String name;

    private SmsService smsService;

    public SmsService getSmsService() {
        if (smsService == null) {
            synchronized (this) {
                if (smsService == null) {
                    smsService =  SpringContextUtils.getBean(name, SmsService.class);
                }
            }
        }

        return smsService;
    }
}
