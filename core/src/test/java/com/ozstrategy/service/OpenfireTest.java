package com.ozstrategy.service;

import com.ozstrategy.dao.export.MessageExportDao;
import com.ozstrategy.dao.project.ProjectDao;
import com.ozstrategy.dao.userrole.UserDao;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.service.project.ProjectManager;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Created by lihao on 12/30/14.
 */
public class OpenfireTest extends BaseManagerTestCase {
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private ProjectManager projectManager;
    
    @Autowired
    private HistoryMessageDao historyMessageDao;
    
    @Autowired
    private HistoryMessageManager historyMessageManager;
    @Autowired
    private MessageExportDao messageExportDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private Properties variable;
    
    
    @Test
    public void testCache() throws Exception{
        Project project = projectManager.getProjectById(7L);
        System.out.println("id==="+project.getId());
        project = projectManager.getProjectById(7L);
        System.out.println("id==="+project.getId());
        
        
    }


    @Test
    public void testMb() throws Exception{
        Integer i= NumberUtils.toInt(variable.get("maxExportItem").toString());
        System.out.println(i);
        
    }
    @Test
    @Rollback(value = false)
    public void testDeleteByIds() throws Exception{
        List<Long> ids=new ArrayList<Long>();
        ids.add(9L);
        ids.add(8L);
        historyMessageDao.deleteByIds(ids);
        
    }
    
    @Test
    @Rollback(value = false)
    public void testAddIndex() throws Exception{
        historyMessageManager.addIndex();
        
    }
    
    @Test
    public void deleteMessage() throws Exception{
        Date startTime= DateUtils.parseDate("2014-12-11 23:12:12",new String[]{"yyyy-MM-dd HH:mm:ss"});
        Date endTime= new Date();
        historyMessageManager.delete(startTime,endTime,7L);
        
        
    }
    
    
    
    
    
    
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
        MessageExport messageExport=messageExportDao.get(8L);
        messageExport.setExecuteDate(new Date());
        messageExport.setFilePath("dd");
        messageExport.setExportor("dsfd");
        messageExport.setType(ExportType.MessagePicture);
        System.out.println();
    }
    @Test
    public void testExport() throws Exception{
        Date startTime= DateUtils.parseDate("2014-12-11 23:12:12",new String[]{"yyyy-MM-dd HH:mm:ss"});
        Date endTime= DateUtils.parseDate("2014-12-12 23:12:12",new String[]{"yyyy-MM-dd HH:mm:ss"});
//        Date endTime= new Date();
        File file=new File("/Users/lihao/Downloads/export");
        if(!file.exists()){
            file.mkdir();
        }
        System.out.println(startTime.getTime());

        historyMessageDao.exportMessage(startTime,endTime,file,7L);

//        historyMessageManager.addIndex();        1422370357865
        System.out.println(DateFormatUtils.format(new Date(1422346183395L),"yyyy-MM-dd HH:mm:ss"));
        
        System.out.println(5000>>3);
        System.out.println(5000<<3);
    }
    
}
