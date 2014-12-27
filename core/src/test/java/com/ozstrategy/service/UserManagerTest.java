package com.ozstrategy.service;

import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.UserManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagerTest extends BaseManagerTestCase {
    private Log log = LogFactory.getLog(UserManagerTest.class);
    @Autowired
    private UserManager userManager;

    
    
    

    @Test
    public void testGetUser() throws Exception {
        
        Map<String,Object> map=new HashMap<String, Object>();
//        map.put("username","admin");
        long begin = System.nanoTime();
        List<User> users1=userManager.listUsers(map, 0, 200);
        if(users1!=null && users1.size()>0){
            for(User user1 : users1){
                System.out.println(user1.getUsername());
            }
        }
        Integer count=userManager.listUsersCount(map);
        System.out.println("count=="+count);
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
