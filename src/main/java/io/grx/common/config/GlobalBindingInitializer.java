package io.grx.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalBindingInitializer {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @InitBinder
    public void registerCustomEditors(WebDataBinder binder, WebRequest request) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
