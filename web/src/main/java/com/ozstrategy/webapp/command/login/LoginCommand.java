package com.ozstrategy.webapp.command.login;

import com.ozstrategy.model.userrole.Feature;
import com.ozstrategy.model.userrole.Role;
import com.ozstrategy.model.userrole.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lihao on 7/4/14.
 */
public class LoginCommand {
    private Long id;
    private String username;
    private String openfireUsername;
    private String email;
    private String mobile;
    private String nickName;
    private Boolean  authentication=Boolean.FALSE;
    private String   gender;
    private String sessionId;
    private Long projectId;
    private String projectName;
    private Boolean manager;
    private List<LoginRoleCommand> roles=new ArrayList<LoginRoleCommand>();
    private List<LoginFeatureCommand> features=new ArrayList<LoginFeatureCommand>();

    public LoginCommand() {
    }

    public LoginCommand(User user){
        this.id= user.getId();
        this.username= user.getUsername();
        this.email= user.getEmail();
        this.mobile= user.getMobile();
        this.authentication=user.getAuthentication();
        this.gender=user.getGender();
        this.nickName=user.getNickName();
        this.projectId=user.getProject()!=null?user.getProject().getId():null;
        this.projectName=user.getProject()!=null?user.getProject().getName():null;
        this.manager=user.getManager();
        this.openfireUsername=user.getUsername()+"_"+user.getId();
        Set<Role> roleSet=user.getRoles();
        if(roleSet!=null && !roleSet.isEmpty()){
            for(Role role : roleSet){
                this.roles.add(new LoginRoleCommand(role));
            }
        }
    }
    public LoginCommand populateFeatures(List<Feature> features) {
        for (Feature feature : features) {
            this.features.add(new LoginFeatureCommand(feature));
        }
        return this;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<LoginRoleCommand> getRoles() {
        return roles;
    }

    public void setRoles(List<LoginRoleCommand> roles) {
        this.roles = roles;
    }

    public List<LoginFeatureCommand> getFeatures() {
        return features;
    }

    public void setFeatures(List<LoginFeatureCommand> features) {
        this.features = features;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public String getOpenfireUsername() {
        return openfireUsername;
    }

    public void setOpenfireUsername(String openfireUsername) {
        this.openfireUsername = openfireUsername;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
