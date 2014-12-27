package com.ozstrategy.webapp.security;


import com.ozstrategy.webapp.command.BaseResultCommand;
import com.ozstrategy.webapp.command.JsonReaderSingleResponse;
import com.ozstrategy.webapp.command.login.LoginCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lihao on 7/1/14.
 */
public class WebAuthenticationFailLoggerHandler extends WebAuthenticationLoggerHandler implements AuthenticationFailureHandler {
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        if(StringUtils.equals(request.getMethod(),"GET")){
            BaseResultCommand command=new BaseResultCommand("请求方式错误，不支持GET请求",false);
            response.getWriter().print(objectMapper.writeValueAsString(command));
            return;
        }
        BaseResultCommand command=new BaseResultCommand("无效用户名或密码，请重试。",false);
        response.getWriter().print(objectMapper.writeValueAsString(command));
    }
}
