package com.ozstrategy.service.project;

import com.ozstrategy.model.project.Project;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 12/30/14.
 */
public interface ProjectManager {
    List<Project> listProjects(Map<String,Object> map, Integer start,Integer limit) throws Exception;
    List<Project> listAllProjects() throws Exception;;
    Integer listProjectsCount(Map<String,Object> map) throws Exception;;
    Project getProjectById(Long id) throws Exception;;
    Project getProjectByName(String name) throws Exception;;
    Project getProjectBySno(String serialNumber) throws Exception;;
    void save(Project project) throws Exception;;
    void update(Project project) throws Exception;;
    void delete(Project project) throws Exception;;
    
    
}
