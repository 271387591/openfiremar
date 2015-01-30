package com.ozstrategy.service.system;

import com.ozstrategy.model.system.JobLog;

/**
 * Created by lihao on 1/7/15.
 */
public interface ApplicationConfigManager {
    String get(String key) throws Exception;
    void put(String key,String value) throws Exception;
    void insertJobLog(JobLog jobLog) throws Exception;
}
