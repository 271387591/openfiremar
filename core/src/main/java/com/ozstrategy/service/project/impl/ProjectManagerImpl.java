package com.ozstrategy.service.project.impl;

import com.ozstrategy.dao.openfire.OpenfireMucRoomDao;
import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.dao.userrole.UserDao;
import com.ozstrategy.model.openfire.OpenfireMucRoom;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.project.ProjectManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 12/30/14.
 */
@Service("projectManager")
public class ProjectManagerImpl implements ProjectManager {
    @Autowired
    private ProjectDao projectDao;
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
        OpenfireMucRoom room=new OpenfireMucRoom().copy(project);
        openfireMucRoomDao.save(room);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void update(Project project) {
        projectDao.update(project);
        openfireMucRoomDao.update(new OpenfireMucRoom().copy(project));
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Project project) {
        openfireMucRoomDao.delete(project.getId());
        projectDao.delete(project);
    }
}
