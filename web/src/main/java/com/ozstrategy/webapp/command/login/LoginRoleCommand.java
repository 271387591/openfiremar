package com.ozstrategy.webapp.command.login;

import com.ozstrategy.model.userrole.Role;

/**
 * Created by lihao on 12/17/14.
 */
public class LoginRoleCommand {
    private Long id;
    private String name;
    private String displayName;
    private String description;
    public LoginRoleCommand(){}
    public LoginRoleCommand(Role role){
        this.id= role.getId();
        this.name=role.getName();
        this.displayName=role.getDisplayName();
        this.description=role.getDescription();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
