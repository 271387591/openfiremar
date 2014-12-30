package com.ozstrategy.service.project.impl;

import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.project.ProjectManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
