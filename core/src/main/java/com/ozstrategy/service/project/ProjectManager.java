package com.ozstrategy.service.project;

import com.ozstrategy.model.project.Project;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 12/30/14.
 */
public interface ProjectManager {
    List<Project> listProjects(Map<String,Object> map, Integer start,Integer limit);
    List<Project> listAllProjects();
    Integer listProjectsCount(Map<String,Object> map);
    Project getProjectById(Long id);
    Project getProjectByName(String name);
    Project getProjectBySno(String serialNumber);
    void save(Project project);
    void update(Project project);
    void delete(Project project);
    
    
}
