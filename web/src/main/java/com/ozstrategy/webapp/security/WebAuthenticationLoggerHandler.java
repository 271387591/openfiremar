package com.ozstrategy.webapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozstrategy.service.userrole.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lihao on 12/17/14.
 */
public abstract class WebAuthenticationLoggerHandler {
    @Autowired
    protected UserManager userManager;
    @Autowired
    MessageSource messageSource;
    protected ObjectMapper objectMapper=new ObjectMapper();
    protected static String PLATFORM="PC";
    
    public String getMessage(String key,HttpServletRequest request) {
        return messageSource.getMessage(key, null, request.getLocale());
    }
    
}
