package com.ozstrategy.dao;

import com.ozstrategy.model.BaseEntity;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;

import java.io.Serializable;

/**
 * Created by lihao on 1/15/15.
 */
public interface UniversalDao<T extends BaseEntity,PK extends Serializable> {
    @InsertProvider(type = SqlHelper.class, method = "save")
    @SelectKey(keyProperty = "id",resultType = Long.class,keyColumn = "id",before = false,statement = {"SELECT LAST_INSERT_ID() AS id"})
    public int save(T entity);
//    public T update(T entity);
//    public T delete(T entity);
//    public T get(PK id);
//    boolean exists(PK id);
}
