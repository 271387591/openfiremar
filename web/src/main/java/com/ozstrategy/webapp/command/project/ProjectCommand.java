package com.ozstrategy.webapp.command.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lihao on 12/30/14.
 */
public class ProjectCommand extends AppProjectCommand {
    
    private List<ProjectUserCommand> users=new ArrayList<ProjectUserCommand>();
    public ProjectCommand() {
    }
    public ProjectCommand(Project project) {
        super(project);
        Set<ProjectUser> userSet=project.getUsers();
        if(userSet!=null && userSet.size()>0){
            for(ProjectUser user : userSet){
                this.users.add(new ProjectUserCommand(user));
            }
        }
    }
    public List<ProjectUserCommand> getUsers() {
        return users;
    }

    public void setUsers(List<ProjectUserCommand> users) {
        this.users = users;
    }

}
