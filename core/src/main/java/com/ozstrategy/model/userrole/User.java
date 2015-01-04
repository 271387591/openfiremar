package com.ozstrategy.model.userrole;

import com.ozstrategy.Constants;
import com.ozstrategy.model.BaseObject;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class User extends BaseObject implements UserDetails {
    private Long id;
    private Boolean accountExpired = Boolean.FALSE;
    private Boolean accountLocked = Boolean.FALSE;
    private Boolean credentialsExpired = Boolean.FALSE;
    private Boolean enabled = Boolean.TRUE;
    private Address address = new Address();
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String passwordHint;
    private String phoneNumber;
    private String username;
    private Integer version;
    private String nickName;
    private String userNo;
    private Boolean authentication = Boolean.FALSE;
    private String website;
    private String gender;
    private String mobile;
    private Set<Role> roles = new HashSet<Role>();
    private Role defaultRole;
    private Set<ProjectUser> projectUsers = new HashSet<ProjectUser>();
    private String fullName;
    private Project defaultProject;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public void addRole(Role role) {
        getRoles().add(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        authorities.addAll(roles);

        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAccountNonExpired() {
        return !getAccountExpired();
    }

    public boolean isAccountNonLocked() {
        return !getAccountLocked();
    }

    public boolean isCredentialsNonExpired() {
        return !getCredentialsExpired();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAdmin() {
        for (Object obj : getRoles()) {
            Role role = (Role) obj;

            if (Constants.ADMIN_ROLE.equals(role.getName())) {
                return true;
            }
        }
        return false;
    }

    public String getFullName() {
        return firstName + lastName;
    }

    public Role getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(Role defaultRole) {
        this.defaultRole = defaultRole;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Set<ProjectUser> getProjectUsers() {
        return projectUsers;
    }

    public void setProjectUsers(Set<ProjectUser> projectUsers) {
        this.projectUsers = projectUsers;
    }

    public Boolean getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public Project getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(Project defaultProject) {
        this.defaultProject = defaultProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        User user = (User) o;
        return new EqualsBuilder()
                .append(id, user.id)
                .append(username, user.username)
                .append(mobile, user.mobile)
                .append(email, user.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(username)
                .append(mobile)
                .append(email)
                .hashCode();

    }
} 
