package com.ozstrategy.service.system.impl;

import com.ozstrategy.dao.system.ApplicationConfigDao;
import com.ozstrategy.model.system.JobLog;
import com.ozstrategy.service.system.ApplicationConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lihao on 1/7/15.
 */
@Service("applicationConfigManager")
public class ApplicationConfigManagerImpl implements ApplicationConfigManager {
    @Autowired
    private ApplicationConfigDao applicationConfigDao;

    public String get(String key) {
        return applicationConfigDao.get(key);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void put(String key, String value) {
        applicationConfigDao.put(key, value);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void insertJobLog(JobLog jobLog) {
        applicationConfigDao.insertJobLog(jobLog);
    }
}
