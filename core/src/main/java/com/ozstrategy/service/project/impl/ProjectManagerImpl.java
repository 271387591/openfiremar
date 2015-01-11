package com.ozstrategy.service.project.impl;

import com.ozstrategy.dao.openfire.OpenfireMucRoomDao;
import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.dao.project.ProjectUserDao;
import com.ozstrategy.dao.userrole.UserDao;
import com.ozstrategy.model.openfire.OpenfireMucRoom;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.model.project.ProjectUser;
import com.ozstrategy.service.project.ProjectManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lihao on 12/30/14.
 */
@Service("projectManager")
public class ProjectManagerImpl implements ProjectManager {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProjectUserDao projectUserDao;
    @Autowired
    private UserDao userDao;
    
    
    @Autowired
    private OpenfireMucRoomDao openfireMucRoomDao;
    
    public List<Project> listProjects(Map<String, Object> map, Integer start, Integer limit) {
        return projectDao.listProjects(map,new RowBounds(start,limit));
    }

    public List<Project> listAllProjects() {
        return projectDao.listProjects(new HashMap<String, Object>(),RowBounds.DEFAULT);
    }

    public Integer listProjectsCount(Map<String, Object> map) {
        return projectDao.listProjectsCount(map);
    }

    public Project getProjectById(Long id) {
        return projectDao.getProjectById(id);
    }

    public Project getProjectByName(String name) {
        return projectDao.getProjectByName(name);
    }

    public Project getProjectBySno(String serialNumber) {
        return projectDao.getProjectBySno(serialNumber);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save(Project project) {
        projectDao.save(project);
        Set<ProjectUser> projectUsers=project.getUsers();
        if(projectUsers!=null && projectUsers.size()>0){
            for(ProjectUser projectUser : projectUsers){
                projectUser.setProject(project);
                projectUserDao.save(projectUser);
            }
        }
        OpenfireMucRoom room=new OpenfireMucRoom().copy(project);
        openfireMucRoomDao.save(room);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void update(Project project) {
        projectDao.update(project);
        projectUserDao.removeByProjectId(project.getId());
        Set<ProjectUser> projectUsers=project.getUsers();
        if(projectUsers!=null && projectUsers.size()>0){
            for(ProjectUser projectUser : projectUsers){
                projectUser.setProject(project);
                projectUserDao.save(projectUser);
            }
        }
        openfireMucRoomDao.update(new OpenfireMucRoom().copy(project));
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        projectUserDao.removeByProjectId(id);
        projectDao.delete(id);
        openfireMucRoomDao.delete(id);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void saveProjectUser(List<ProjectUser> projectUsers) {
        if(projectUsers!=null && projectUsers.size()>0){
            for(ProjectUser projectUser : projectUsers){
                projectUserDao.removeUser(projectUser.getUser().getId(),projectUser.getProject().getId());
                projectUserDao.save(projectUser);
            }
        }
        
        
    }
    @Transactional(rollbackFor = Throwable.class)
    public void updateManager(Long userId, Long projectId,Boolean manager) {
        projectUserDao.updateManager(userId,projectId,manager);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void removeUser(Set<Long> userIds, Long projectId) {
        if(userIds!=null && userIds.size()>0){
            for(Long userId:userIds){
                projectUserDao.removeUser(userId,projectId);
            }
        }
    }
}
