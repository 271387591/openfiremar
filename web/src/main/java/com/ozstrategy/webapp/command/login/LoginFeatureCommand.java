package com.ozstrategy.webapp.command.login;

import com.ozstrategy.model.userrole.Feature;

/**
 * Created by lihao on 12/17/14.
 */
public class LoginFeatureCommand {
    protected String name;
    protected Long id;
    public   LoginFeatureCommand(){}
    public   LoginFeatureCommand(Feature feature){
        this.id=feature.getId();
        this.name=feature.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
