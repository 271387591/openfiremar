package com.ozstrategy.webapp.command.userrole;

import com.ozstrategy.model.userrole.Feature;
import com.ozstrategy.model.userrole.Role;
import com.ozstrategy.model.userrole.RoleFeature;
import com.ozstrategy.model.userrole.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UserCommand {

    private boolean accountLocked;
    private Date createDate;
    private boolean enabled;
    private String firstName;
    private String lastName;
    private Long id;
    private String password;
    private String roleDisplayName;
    private Long roleId;
    private List<Long> roleIds = new ArrayList<Long>();
    private String roleName;
    private List<SimpleRoleCommand> simpleRoles = new ArrayList<SimpleRoleCommand>();
    private String username;
    private Boolean admin = false;
    private Set<String> features = new LinkedHashSet<String>();
    private String defaultRoleName;
    private String defaultRoleDisplayName;
    private Long defaultRoleId;
    private String fullName;
    private String email;
    private String mobile;
    private String gender;
    private Integer taskCount;
    private String nickName;
    private String userNo;
    private Boolean authentication;

    public UserCommand() {
    }

    public UserCommand(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.enabled = user.isEnabled();
        this.createDate = user.getCreateDate();
        this.accountLocked = user.getAccountLocked();
        this.nickName = user.getNickName();
        this.admin = user.isAdmin();
        this.userNo=user.getUserNo();
        this.authentication=user.getAuthentication();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.mobile = user.getMobile();
        this.gender = user.getGender();
        
      
        if ((user.getRoles() != null) && (user.getRoles().size() > 0)) {
            for (Role role : user.getRoles()) {
                this.roleId = role.getId();
                this.roleName = role.getName();
                this.roleDisplayName = role.getDisplayName();
                SimpleRoleCommand simpleRoleCommand = new SimpleRoleCommand();
                simpleRoleCommand.setId(roleId);
                simpleRoleCommand.setDisplayName(roleDisplayName);
                simpleRoleCommand.setName(roleName);
                this.simpleRoles.add(simpleRoleCommand);
            }
        }
    }

    public UserCommand populate(List<RoleFeature> roleFeatures) {
        for (RoleFeature rf : roleFeatures) {
            Feature feature = rf.getFeature();
            this.features.add(feature.getName());
        }
        return this;
    }

    public UserCommand populateFeatures(List<Feature> features) {
        for (Feature feature : features) {
            this.features.add(feature.getName());
        }
        return this;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public void setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<SimpleRoleCommand> getSimpleRoles() {
        return simpleRoles;
    }

    public void setSimpleRoles(List<SimpleRoleCommand> simpleRoles) {
        this.simpleRoles = simpleRoles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public String getDefaultRoleName() {
        return defaultRoleName;
    }

    public void setDefaultRoleName(String defaultRoleName) {
        this.defaultRoleName = defaultRoleName;
    }

    public String getDefaultRoleDisplayName() {
        return defaultRoleDisplayName;
    }

    public void setDefaultRoleDisplayName(String defaultRoleDisplayName) {
        this.defaultRoleDisplayName = defaultRoleDisplayName;
    }

    public Long getDefaultRoleId() {
        return defaultRoleId;
    }

    public void setDefaultRoleId(Long defaultRoleId) {
        this.defaultRoleId = defaultRoleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }
} 
