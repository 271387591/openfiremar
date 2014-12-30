package com.ozstrategy.webapp.command.login;

import com.ozstrategy.model.project.ProjectUser;
import com.ozstrategy.model.userrole.Feature;
import com.ozstrategy.model.userrole.Role;
import com.ozstrategy.model.userrole.User;
import com.ozstrategy.webapp.command.project.ProjectUserCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lihao on 7/4/14.
 */
public class LoginCommand {
    private Long id;
    private String username;
    private String email;
    private String mobile;
    private String nickName;
    private Boolean  authentication=Boolean.FALSE;
    private String   gender;
    private List<LoginRoleCommand> roles=new ArrayList<LoginRoleCommand>();
    private List<LoginFeatureCommand> features=new ArrayList<LoginFeatureCommand>();
    private LoginRoleCommand defaultRole=null;
    private List<ProjectUserCommand> projects=new ArrayList<ProjectUserCommand>();

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
        Set<Role> roleSet=user.getRoles();
        if(roleSet!=null && !roleSet.isEmpty()){
            for(Role role : roleSet){
                this.roles.add(new LoginRoleCommand(role));
            }
        }
        Role role=user.getDefaultRole();
        if(role!=null){
            this.defaultRole=new LoginRoleCommand(role);
        }
        Set<ProjectUser> projectUsers=user.getProjectUsers();
        if(projectUsers!=null && projectUsers.size()>0){
            for(ProjectUser projectUser:projectUsers){
                this.projects.add(new ProjectUserCommand(projectUser));
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

    public LoginRoleCommand getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(LoginRoleCommand defaultRole) {
        this.defaultRole = defaultRole;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ProjectUserCommand> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectUserCommand> projects) {
        this.projects = projects;
    }
}
