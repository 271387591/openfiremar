package com.ozstrategy.webapp.security;

import com.ozstrategy.model.userrole.Feature;
import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.FeatureManager;
import com.ozstrategy.webapp.command.JsonReaderSingleResponse;
import com.ozstrategy.webapp.command.login.LoginCommand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: liuqian
 * Date: 13-7-3
 * Time: PM3:07
 * To change this template use File | Settings | File Templates.
 */
public class WebAuthenticationSuccessLoggerHandler extends WebAuthenticationLoggerHandler implements AuthenticationSuccessHandler {
    @Autowired
    private FeatureManager featureManager = null;
    
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response, Authentication authentication)
    throws IOException, ServletException {
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      response.setContentType("text/html;charset=UTF-8");
      String platform=request.getParameter("platform");
      if(StringUtils.equals(PLATFORM,platform)){
          response.sendRedirect("dispatcherPage.action");
          return;
      }
      User user = (User)authentication.getPrincipal();
      user  = userManager.getUserByUsername(user.getUsername());
      if (user == null) {
        return;
      }
      String sessionId= UUID.randomUUID().toString();
      AppSessionManager.put(user.getUsername(),sessionId);
      
      LoginCommand command = new LoginCommand(user);
      command.setSessionId(sessionId);
      List<Feature> roleFeatures = featureManager.getUserFeaturesByUsername(request.getRemoteUser());
      command = command.populateFeatures(roleFeatures);
      JsonReaderSingleResponse<LoginCommand> jsonReaderSingleResponse=new JsonReaderSingleResponse<LoginCommand>(command);
      String result=objectMapper.writeValueAsString(jsonReaderSingleResponse);
      response.getWriter().print(result);
  }
}
