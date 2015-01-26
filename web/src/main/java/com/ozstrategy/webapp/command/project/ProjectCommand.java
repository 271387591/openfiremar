package com.ozstrategy.webapp.command.project;

import com.ozstrategy.model.project.Project;

/**
 * Created by lihao on 12/30/14.
 */
public class ProjectCommand extends AppProjectCommand {
    private Integer userCount;
    
    public ProjectCommand() {
    }
    public ProjectCommand(Project project) {
        super(project);
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }
}
