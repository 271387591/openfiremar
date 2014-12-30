package com.ozstrategy.webapp.command.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;
import com.ozstrategy.webapp.command.BaseObjectCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lihao on 12/30/14.
 */
public class ProjectCommand extends BaseObjectCommand {
    private Long id;
    private String serialNumber;
    private String name;
    private String description;
    private Integer peoples;
    private String  activationCode;
    private List<ProjectUserCommand> users=new ArrayList<ProjectUserCommand>();
    public ProjectCommand() {
    }
    public ProjectCommand(Project project) {
        super(project);
        this.id=project.getId();
        this.name=project.getName();
        this.description=project.getDescription();
        this.peoples=project.getPeoples();
        this.activationCode=project.getActivationCode();
        Set<ProjectUser> userSet=project.getUsers();
        if(userSet!=null && userSet.size()>0){
            for(ProjectUser user : userSet){
                this.users.add(new ProjectUserCommand(user));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPeoples() {
        return peoples;
    }

    public void setPeoples(Integer peoples) {
        this.peoples = peoples;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public List<ProjectUserCommand> getUsers() {
        return users;
    }

    public void setUsers(List<ProjectUserCommand> users) {
        this.users = users;
    }
}
