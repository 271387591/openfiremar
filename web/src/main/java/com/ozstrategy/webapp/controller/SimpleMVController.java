package com.ozstrategy.webapp.controller;

import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.FeatureManager;
import com.ozstrategy.service.userrole.UserManager;
import com.ozstrategy.webapp.command.userrole.UserCommand;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SimpleMVController implements InitializingBean {
    //~ Instance fields --------------------------------------------------------------------------------------------------

    private final transient Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private UserManager userManager = null;
    @Autowired
    private FeatureManager featureManager = null;
    


    public void afterPropertiesSet() throws Exception {
    }


    @RequestMapping("/dispatcherPage.action")
    public ModelAndView dispatcherPage(HttpServletRequest request, HttpServletResponse response) {
        String username=request.getRemoteUser();
        if (username == null || "".equals(username)) {
            return new ModelAndView("redirect:login");
        }
//        String url = user.getDefaultRole().getSystemView().getUrl();
//        if (StringUtils.isNotEmpty(url)) {
//            return new ModelAndView("redirect:" + url);
//        }
        return new ModelAndView("redirect:home");
    }
    @RequestMapping("/desktop")
    public ModelAndView desktop(HttpServletRequest request){
        
        return new ModelAndView("redirect:desktop");
    }

    @RequestMapping("/desktopRes.js")
    public ModelAndView getGlobalRes(HttpServletRequest request, HttpServletResponse response) {
//        String projectId=request.getParameter("projectId");
//        Long id=null;
//        if(StringUtils.isNotEmpty(projectId)){
//            id=Long.parseLong(projectId);
//        }
//        User user = userManager.getUserByUsername(request.getRemoteUser(),id);
//      
        UserCommand command = pupUser(request);
        
        return new ModelAndView("res/desktopRes", "command", command);
    }
    private  UserCommand pupUser(HttpServletRequest request){
        String projectId=request.getParameter("projectId");
        Long id=null;
        if(StringUtils.isNotEmpty(projectId)){
            id=Long.parseLong(projectId);
        }
        User user = userManager.getUserByUsername(request.getRemoteUser(),id);
        if(user==null){
            return new UserCommand();
        }
        UserCommand command = new UserCommand(user);
        return command;
    }
} 
