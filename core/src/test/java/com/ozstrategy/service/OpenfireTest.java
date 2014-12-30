package com.ozstrategy.service;

import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.model.project.Project;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lihao on 12/30/14.
 */
public class OpenfireTest extends BaseManagerTestCase {
    @Autowired
    private ProjectDao projectDao;
    
    @Test
    public void testGetProject(){
        List<Project> projects = projectDao.listProjects(new HashMap<String, Object>(), RowBounds.DEFAULT);
        for(Project project : projects){
            System.out.println("name=="+project.getName());
        }
        
    }
    
}
