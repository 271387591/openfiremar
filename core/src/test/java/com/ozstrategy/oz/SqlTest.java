package com.ozstrategy.oz;

import com.jolbox.bonecp.BoneCPDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Created by lihao on 1/13/15.
 */
public class SqlTest {
    public static void main(String[] s){
        User u=new User();
        u.setName("sdfqqqqq111");
        BoneCPDataSource dataSource=new BoneCPDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/ehrdb?createDatabaseIfNotExist=true&&useUnicode=true&&characterEncoding=utf-8&&autoReconnect=true");
        dataSource.setUsername("root");
        dataSource.setPassword("Passw0rd");
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
//        jdbcTemplate.update(sql.get("sql").toString(),(Object[])sql.get("values"));
//        jdbcTemplate.update("insert user(id,name,sex) VALUES (?,?,?)",null,"sdfd","ddd");
        
        
        
//        System.out.println(sql.get("sql").toString());
//        SqlBuilder.BEGIN();
//        SqlBuilder.UPDATE("sss");
//        SqlBuilder.SET("sss=?,fff=?");
//        SqlBuilder.WHERE("id=?");
//        System.out.print(SqlBuilder.SQL());
        Map<String,Object> map=jdbcTemplate.queryForMap("select * from user where id=7");
        try {
            BeanUtils.populate(u,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        u.setName("7777ssss");
        u.setSex("www");

        delete(jdbcTemplate, u);


        
        
        


    }
    static public BaseEntity delete(JdbcTemplate jdbcTemplate,BaseEntity entity){
        Object[] objects=entity.delete();
        String sql=objects[0].toString();
        Object[] args=(Object[])objects[1];
        jdbcTemplate.update(sql,args);
        return entity;
    }
    static public BaseEntity save(JdbcTemplate jdbcTemplate,BaseEntity entity){
        Object[] objects=entity.insert();
        final String sql=objects[0].toString();
        final Object[] args=(Object[])objects[1];
        final int len=args.length;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator(){
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                for(int i=0;i<len;i++){
                    StatementCreatorUtils.setParameterValue(ps, i+1, SqlTypeValue.TYPE_UNKNOWN, args[i]);
                }
                return ps;
            }
        },keyHolder);
        Long generatedId = keyHolder.getKey().longValue();
        entity.setId(generatedId);
        return entity;
    }
    static public BaseEntity update(JdbcTemplate jdbcTemplate,BaseEntity entity){
        Object[] objects=entity.update();
        String sql=objects[0].toString();
        Object[] args=(Object[])objects[1];
        jdbcTemplate.update(sql,args);
        return entity;
    }
    
}
