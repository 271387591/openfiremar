package com.ozstrategy.dao.system;

import com.ozstrategy.model.system.JobLog;
import org.apache.ibatis.annotations.Param;

/**
 * Created by lihao on 1/7/15.
 */
public interface ApplicationConfigDao {
    String get(@Param("key")String key);
    void put(@Param("key")String key,@Param("value")String value);
    void insertJobLog(JobLog jobLog);
}
