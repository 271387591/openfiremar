package com.ozstrategy.dao.project;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;
import com.ozstrategy.model.userrole.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by lihao on 12/29/14.
 */
public interface ProjectUserDao {
    List<User> listUserByProjectId(Long projectId);
    List<Project> listProjectByUserId(Long userId);
    void save(ProjectUser projectUser);
    List<ProjectUser> listProjectUserByUserId(Long userId);
    List<ProjectUser> listProjectUserByProjectId(Long projectId);
    void removeByUserId(Long userId);
    void removeByProjectId(Long projectId);
    Integer listUsersByProjectIdCount(Long projectId);
    List<User> listUsersByProjectId(Long projectId,RowBounds rowBounds);
}
