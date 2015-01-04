package com.ozstrategy.webapp.command.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;
import com.ozstrategy.model.userrole.User;

/**
 * Created by lihao on 12/30/14.
 */
public class ProjectUserCommand {
    private Long id;
    private String username;
    private String projectName;
    private Long projectId;
    private Long userId;
    private Boolean manager;
    private String nickName;
    public ProjectUserCommand(ProjectUser projectUser){
        this.id=projectUser.getId();
        this.manager=projectUser.getManager();
        User user=projectUser.getUser();
        if(user!=null){
            this.userId=user.getId();
            this.username=user.getUsername();
            this.nickName=user.getNickName();
        }
        Project project=projectUser.getProject();
        if(project!=null) {
            this.projectId=project.getId();
            this.projectName=project.getName();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
