package com.ozstrategy.webapp.command.userrole;

import com.ozstrategy.webapp.command.project.ProjectCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihao on 1/1/15.
 */
public class AvailableRoleProjectCommand {
    private List<RoleCommand> roles=new ArrayList<RoleCommand>();
    private List<ProjectCommand> projects=new ArrayList<ProjectCommand>();
    public AvailableRoleProjectCommand(){
        
    }

    public List<RoleCommand> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleCommand> roles) {
        this.roles = roles;
    }

    public List<ProjectCommand> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectCommand> projects) {
        this.projects = projects;
    }
}
