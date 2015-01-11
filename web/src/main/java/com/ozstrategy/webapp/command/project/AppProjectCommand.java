package com.ozstrategy.webapp.command.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.webapp.command.BaseObjectCommand;

/**
 * Created by lihao on 1/5/15.
 */
public class AppProjectCommand extends BaseObjectCommand {
    protected Long id;
    protected String serialNumber;
    protected String name;
    protected String description;
    protected String  activationCode;

    public AppProjectCommand() {
    }

    public AppProjectCommand(Project project){
        super(project);
        this.id=project.getId();
        this.name=project.getName();
        this.description=project.getDescription();
        this.activationCode=project.getActivationCode();
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

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
