package com.ozstrategy.util;

import com.ozstrategy.oz.UniversalDao;
import com.ozstrategy.oz.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.Date;

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
        SqlSession sqlSession = getSessionFactory().openSession();
        UniversalDao<User,Long> userMapper = sqlSession.getMapper(UniversalDao.class);
        User user=new User();
        user.setName("lihao");
        user.setSex("1");
        user.setCreateDate(new Date());
        user.setLastUpdateDate(new Date());
        userMapper.save(user);
        System.out.println(user.getId());
        sqlSession.commit();

    }
}
