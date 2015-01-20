package com.ozstrategy.oz;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lihao on 1/14/15.
 */
public class AbstractDao<T extends BaseEntity> {
    private JdbcTemplate jdbcTemplate;
    public T save(T entity){
        final Object[] objects=entity.insert();
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
    public T update(T entity){
        Object[] objects=entity.update();
        String sql=objects[0].toString();
        Object[] args=(Object[])objects[1];
        jdbcTemplate.update(sql,args);
        return entity;
    }
    public T delete(T entity){
        Object[] objects=entity.delete();
        String sql=objects[0].toString();
        Object[] args=(Object[])objects[1];
        jdbcTemplate.update(sql,args);
        return entity;
    }
}
