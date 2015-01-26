package com.ozstrategy.webapp.controller.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.project.ProjectManager;
import com.ozstrategy.service.userrole.UserManager;
import com.ozstrategy.webapp.command.BaseResultCommand;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.command.project.AppProjectCommand;
import com.ozstrategy.webapp.command.project.ProjectCommand;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lihao on 12/30/14.
 */
@Controller
@RequestMapping("projectController.do")
public class ProjectController extends BaseController {
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;
    
    
    @RequestMapping(params = "method=listAllProjects")
    @ResponseBody
    public JsonReaderResponse<AppProjectCommand> listAllProjects(HttpServletRequest request){
        List<AppProjectCommand> commands=new ArrayList<AppProjectCommand>();
        List<Project> projects=projectManager.listAllProjects();
        if(projects!=null && projects.size()>0){
            for(Project project : projects){
                commands.add(new AppProjectCommand(project));
            }
        }
        return new JsonReaderResponse<AppProjectCommand>(commands,true,"");
    }
    @RequestMapping(params = "method=listProjects")
    @ResponseBody
    public JsonReaderResponse<ProjectCommand> listProjects(HttpServletRequest request){
        List<ProjectCommand> commands=new ArrayList<ProjectCommand>();
        Integer start=parseInteger(request.getParameter("start"));
        Integer limit=parseInteger(request.getParameter("limit"));
        Map<String,Object> map=requestMap(request);
        List<Project> projects=projectManager.listProjects(map, start, limit);
        if(projects!=null && projects.size()>0){
            for(Project project : projects){
                ProjectCommand command= new ProjectCommand(project);
                Integer userCount=userManager.getUserCountByProjectId(project.getId());
                command.setUserCount(userCount);
                commands.add(command);
            }
        }
        int count = projectManager.listProjectsCount(map);
        return new JsonReaderResponse<ProjectCommand>(commands,"",count);
    }
    
    
    @RequestMapping(params = "method=saveProject")
    @ResponseBody
    public BaseResultCommand saveProject(HttpServletRequest request){
        return saveOrUpdate(request,true);
    }
    @RequestMapping(params = "method=updateProject")
    @ResponseBody
    public BaseResultCommand updateProject(HttpServletRequest request){
        return saveOrUpdate(request,false);
    }
    @RequestMapping(params = "method=deleteProject")
    @ResponseBody
    public BaseResultCommand deleteProject(HttpServletRequest request){
        String ids=request.getParameter("id");
        Long id=parseLong(ids);
        Project project=projectManager.getProjectById(id);
        if(project!=null){
            projectManager.delete(project);
            return new BaseResultCommand("",true);
        }else{
            return new BaseResultCommand("工程未找到",false);
        }      
    }
    @RequestMapping(params = "method=saveProjectUser")
    @ResponseBody
    public BaseResultCommand saveProjectUser(HttpServletRequest request){
        String userIds=request.getParameter("userIds");
        String projectId=request.getParameter("projectId");
        Project project=null;
        if(StringUtils.isNotEmpty(projectId)){
            project=projectManager.getProjectById(parseLong(projectId));
        }
        return new BaseResultCommand("",true);
    }
    @RequestMapping(params = "method=updateManager")
    @ResponseBody
    public BaseResultCommand updateManager(HttpServletRequest request){
        String userId=request.getParameter("userId");
        String projectId=request.getParameter("projectId");
        String manager=request.getParameter("manager");
        if(StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(projectId)){
            return new BaseResultCommand("",true);
        }
        return new BaseResultCommand("操作失败",true);
    }
    @RequestMapping(params = "method=removeUser")
    @ResponseBody
    public BaseResultCommand removeUser(HttpServletRequest request){
        String userIds=request.getParameter("userIds");
        String projectId=request.getParameter("projectId");
        if(StringUtils.isNotEmpty(userIds) && StringUtils.isNotEmpty(projectId)){
            Set<Long> ids=new HashSet<Long>();
            String[] userIdss=userIds.split(",");
            for(String userId:userIdss){
                ids.add(parseLong(userId));
            }
            return new BaseResultCommand("",true);
        }
        return new BaseResultCommand("操作失败",true);
    }
    
    
    
    
    private BaseResultCommand saveOrUpdate(HttpServletRequest request,boolean save){
        Project project        = null;
        String id=request.getParameter("id");
        String name=request.getParameter("name");
        String description=request.getParameter("description");
        String activationCode=request.getParameter("activationCode");
        String usersIds=request.getParameter("usersIds");
        String serialNumber=request.getParameter("serialNumber");
        String managerIds=request.getParameter("managerIds");
        String[] amanagerIds=new String[0];
        if(StringUtils.isNotEmpty(managerIds)){
            amanagerIds=managerIds.split(",");
        }

        if(!save){
            if(checkIsNotNumber(id)){
                return new BaseResultCommand(getMessage("message.error.id.null",request),Boolean.FALSE);
            }
        }
        if(checkIsEmpty(name)){
            return new BaseResultCommand(getMessage("message.error.name.null",request),Boolean.FALSE);
        }
        if(save){
            if(projectManager.getProjectByName(name)!=null){
                return new BaseResultCommand(getMessage("message.error.project.exist",request),Boolean.FALSE);
            }
            if(projectManager.getProjectBySno(serialNumber)!=null){
                return new BaseResultCommand(getMessage("message.error.serialNumber.exist",request),Boolean.FALSE);
            }
        }else{
            Project tproject=projectManager.getProjectByName(name);
            if(tproject!=null){
                if(!StringUtils.equals(id,tproject.getId().toString())){
                    return new BaseResultCommand(getMessage("message.error.project.exist",request),Boolean.FALSE);
                }
            }
            tproject=projectManager.getProjectByName(serialNumber);
            if(tproject!=null){
                if(!StringUtils.equals(id,tproject.getId().toString())){
                    return new BaseResultCommand(getMessage("message.error.serialNumber.exist",request),Boolean.FALSE);
                }
            }
        }
        try{
            if(save){
                project=new Project();
                project.setCreateDate(new Date());
            }else{
                project=projectManager.getProjectById(parseLong(id));
            }
            project.setName(name);
            project.setDescription(description);
            project.setActivationCode(activationCode);
            project.setLastUpdateDate(new Date());
            project.setSerialNumber(serialNumber);
            if(save){
                projectManager.save(project);
            }else{
                projectManager.update(project);
            }
            return new BaseResultCommand("",true);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return new BaseResultCommand(getMessage("message.error.saveuser.fail",request),Boolean.FALSE);
    }


}
