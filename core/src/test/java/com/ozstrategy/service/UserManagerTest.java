package com.ozstrategy.service;

import com.ozstrategy.dao.userrole.UserDao;
import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.UserManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.quartz.CronScheduleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

public class UserManagerTest extends BaseManagerTestCase {
    private Log log = LogFactory.getLog(UserManagerTest.class);
    @Autowired
    private UserManager userManager;
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    
    @Test
    public void testQuartz(){
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 10 3 ? * *");
        while (true){
            
        }
    }
    
    @Test
    @Rollback(value = false)
    public void testInsert() throws Exception{
        long start=System.currentTimeMillis();
        String sql="INSERT INTO ext_ofHistory (fromId, toId, message, type,createDate) VALUES (?, ?, ?, ?,?)";
        for(int i=0;i<1000000;i++){
            String msg="了深刻的解放军了"+i;
            jdbcTemplate.update(sql,"admin","user",msg,"groupchat",new Date());
        }
        long end=System.currentTimeMillis();
        System.out.println("time===="+(end-start));
    }

    @Test
    public void testGetUser() throws Exception {
        User user=userDao.getUserById(1L);
        System.out.println(user.getId());
        System.out.println("sdfdsf");
//        System.out.println(user.getDefaultRole().getDisplayName());
//        System.out.println(user.getRoles().size());
//        System.out.println("sd");
        
//        Map<String,Object> map=new HashMap<String, Object>();
//        map.put("username","admin");
//        long begin = System.nanoTime();
//        List<User> users1=userManager.listUsers(map, 0, 200);
//        if(users1!=null && users1.size()>0){
//            for(User user1 : users1){
//                System.out.println(user1.getUsername());
//            }
//        }
//        Integer count=userManager.listUsersCount(map);
//        System.out.println("count=="+count);
//        long end = System.nanoTime() - begin;
//        System.out.println("count :" + end);
//        begin = System.nanoTime();
//        List<User> users2=userManager.getUsers(map);
//        if(users2!=null && users2.size()>0){
//            for(User user1 : users2){
//                System.out.println(user1.getUsername());
//            }
//        }
//        end = System.nanoTime() - begin;
//        System.out.println("count :" + end);
//        begin = System.nanoTime();
//        List<User> users3=userManager.getUsers(map);
//        if(users3!=null && users3.size()>0){
//            for(User user1 : users3){
//                System.out.println(user1.getUsername());
//            }
//        }
//        end = System.nanoTime() - begin;
//        System.out.println("count :" + end);
        
        
        
    }
    
    
    

    
}
