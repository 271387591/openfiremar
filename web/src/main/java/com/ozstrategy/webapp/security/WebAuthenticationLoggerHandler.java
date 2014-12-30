package com.ozstrategy.webapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozstrategy.service.userrole.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Locale;

/**
 * Created by lihao on 12/17/14.
 */
public abstract class WebAuthenticationLoggerHandler {
    @Autowired
    protected UserManager userManager;
    protected ObjectMapper objectMapper=new ObjectMapper();
    protected static String PLATFORM="PC";
    
    
}
