package com.ozstrategy.util;

import com.ozstrategy.model.export.MessageExport;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * Created by lihao on 1/11/15.
 */
public class MybatisTest {
    private static SqlSessionFactory getSessionFactory() {
        SqlSessionFactory sessionFactory = null;
        String resource = "mybatis-config111.xml";
        try {
            sessionFactory = new SqlSessionFactoryBuilder().build(Resources
                    .getResourceAsReader(resource));
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
        return sessionFactory;
    }

    public static void main(String[] args) {
//        SqlSession sqlSession = getSessionFactory().openSession();
//        UniversalDao<User,Long> userMapper = sqlSession.getMapper(UniversalDao.class);
        MessageExport user=new MessageExport();
        System.out.println(user.returnInsertSql());
        System.out.println(user.returnUpdateSql());
        System.out.println(user.returnDeleteSql());
        

    }
}
