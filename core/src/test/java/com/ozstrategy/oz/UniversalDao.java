package com.ozstrategy.oz;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;

/**
 * Created by lihao on 1/15/15.
 */
public interface UniversalDao<T extends BaseEntity,PK extends Serializable> {
    @InsertProvider(type = SqlHelper.class, method = "insert")
    @SelectKey(keyProperty = "id",resultType = Long.class,keyColumn = "id",before = false,statement = {"SELECT LAST_INSERT_ID() AS id"})
    public int save(T entity);
    @UpdateProvider(type = SqlHelper.class,method = "update")
    public int update(T entity);
    @DeleteProvider(type = SqlHelper.class,method = "delete")
    public int delete(T entity);
}
