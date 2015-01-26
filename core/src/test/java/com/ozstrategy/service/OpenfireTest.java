package com.ozstrategy.service;

import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.model.project.Project;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lihao on 12/30/14.
 */
public class OpenfireTest extends BaseManagerTestCase {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private HistoryMessageDao historyMessageDao;
    
    
    
    
    @Test
    public void testGetProject(){
        List<Project> projects = projectDao.listProjects(new HashMap<String, Object>(), RowBounds.DEFAULT);
        for(Project project : projects){
            System.out.println("name=="+project.getName());
        }
        
    }
    @Test
    @Rollback(value = false)
    public void testSaveProject(){
        Project project=new Project();
        project.setName("1");
        project.setDescription("ddd");
        project.setSerialNumber("111");
        project.setActivationCode("111");
        project.setCreateDate(new Date());
        project.setLastUpdateDate(new Date());
        projectDao.save(project);
    }
    @Test
    @Rollback(value = false)
    public void testUpdateProject(){
        Project project=projectDao.getProjectById(4L);
        project.setName("1222");
        project.setDescription("ddd1111");
        project.setSerialNumber("111ssss");
        project.setActivationCode("111dddd");
        project.setCreateDate(new Date());
        project.setLastUpdateDate(new Date());
        projectDao.update(project);
    }
    
    @Test
    @Rollback(value = false)
    public void testDeleteProject(){
        Project project=projectDao.getProjectById(3L);
        project.setName("1");
        project.setDescription("ddd");
        project.setSerialNumber("111");
        project.setActivationCode("111");
        project.setCreateDate(new Date());
        project.setLastUpdateDate(new Date());
        projectDao.delete(project);
    }
    
    
    @Test
    public void testJdbc(){
        MessageExport messageExport=new MessageExport();
        messageExport.setExecuteDate(new Date());
        messageExport.setFilePath("dd");
        messageExport.setExportor("dsfd");
        messageExport.setType(ExportType.MessagePicture);
        System.out.println();
    }
    @Test
    public void testExport() throws Exception{
        Date startTime= DateUtils.parseDate("2014-12-12 12:12:12",new String[]{"yyyy-MM-dd HH:mm:ss"});
        Date endTime= new Date();
        File file=new File("/Users/lihao/Downloads/export");
        if(!file.exists()){
            file.mkdir();
        }

        historyMessageDao.exportVoice(startTime,endTime,file,7L);
        
        System.out.println(5000>>3);
        System.out.println(5000<<3);
    }
    
}
