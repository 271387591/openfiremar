package com.ozstrategy.model.project;

import com.ozstrategy.model.BaseObject;
import com.ozstrategy.model.userrole.User;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by lihao on 12/29/14.
 */
public class ProjectUser extends BaseObject implements Serializable {
    private Long id;
    private User user;
    private Project project;
    private Boolean manager=Boolean.FALSE;
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Boolean getManager() {
        return manager;
    }

    public void setManager(Boolean manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProjectUser that = (ProjectUser) o;

        return new EqualsBuilder()
                .append(id,that.id)
                .append(user, that.user)
                .append(project,that.project)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(user)
                .append(project)
                .hashCode();
    }
}
