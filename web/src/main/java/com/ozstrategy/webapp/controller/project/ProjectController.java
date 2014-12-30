package com.ozstrategy.webapp.controller.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.project.ProjectManager;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.command.project.ProjectCommand;
import com.ozstrategy.webapp.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihao on 12/30/14.
 */
@Controller
@RequestMapping("projectController.do")
public class ProjectController extends BaseController {
    @Autowired
    private ProjectManager projectManager;
    @RequestMapping(params = "method=listAllProjects")
    @ResponseBody
    public JsonReaderResponse<ProjectCommand> listAllProjects(HttpServletRequest request){
        List<ProjectCommand> commands=new ArrayList<ProjectCommand>();
        List<Project> projects=projectManager.listAllProjects();
        if(projects!=null && projects.size()>0){
            for(Project project : projects){
                commands.add(new ProjectCommand(project));
            }
        }
        return new JsonReaderResponse<ProjectCommand>(commands,true,"");
    }

}
