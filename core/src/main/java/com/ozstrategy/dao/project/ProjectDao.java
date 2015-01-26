package com.ozstrategy.dao.project;

import com.ozstrategy.dao.UniversalDao;
import com.ozstrategy.model.project.Project;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 12/29/14.
 */
public interface ProjectDao extends UniversalDao<Project,Long>{
    List<Project> listProjects(Map<String,Object> map, RowBounds rowBounds);
    Integer listProjectsCount(Map<String,Object> map);
    Project getProjectById(Long id);
    Project getProjectByName(String name);
    Project getProjectBySno(String serialNumber);
}
