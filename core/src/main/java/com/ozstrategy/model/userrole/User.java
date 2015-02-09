package com.ozstrategy.model.userrole;

import com.ozstrategy.Constants;
import com.ozstrategy.model.BaseEntity;
import com.ozstrategy.model.project.Project;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "ext_user")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Boolean accountExpired = Boolean.FALSE;
    @Column
    private Boolean accountLocked = Boolean.FALSE;
    @Column
    private Boolean credentialsExpired = Boolean.FALSE;
    @Column
    private Boolean enabled = Boolean.TRUE;
    @Column
    private String email;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String password;
    @Column
    private String phoneNumber;
    @Column
    private String username;
    @Column
    private Integer version;
    @Column
    private String nickName;
    @Column
    private String userNo;
    @Column
    private Boolean authentication = Boolean.FALSE;
    @Column
    private String gender;
    @Column
    private String mobile;
    @JoinTable(
            name               = "ext_userrole",
            joinColumns        = { @JoinColumn(name = "userId") },
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    @ManyToMany(
            fetch   = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    @Transient
    private Set<Role> roles = new HashSet<Role>();
    @Transient
    private String fullName;
    @Column
    private Long projectId;
    @Transient
    private Project project;
    @Column
    private Boolean manager;

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

   

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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
