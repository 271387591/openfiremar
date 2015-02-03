package com.ozstrategy.job;

import com.ozstrategy.model.system.JobLog;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.service.system.ApplicationConfigManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by lihao on 1/8/15.
 */
public class LuceneIndexJob {
    @Autowired
    private HistoryMessageManager historyMessageManager;
    @Autowired
    private ApplicationConfigManager applicationConfigManager;
    private Log log= LogFactory.getLog(getClass());
    public void writeIndex() throws Exception{
        try{
            historyMessageManager.addIndex();
            JobLog jobLog=new JobLog();
            jobLog.setJob(getClass().getSimpleName());
            jobLog.setLastUpdateDate(new Date());
            jobLog.setCreateDate(new Date());
            jobLog.setSuccess(Boolean.TRUE);
            applicationConfigManager.insertJobLog(jobLog);
            System.out.println("添加索引完成");
        }catch (Exception e){
            log.error("write index fail",e);
            JobLog jobLog=new JobLog();
            jobLog.setJob(getClass().getSimpleName());
            jobLog.setLastUpdateDate(new Date());
            jobLog.setCreateDate(new Date());
            jobLog.setSuccess(Boolean.FALSE);
            jobLog.setException(e.getClass().getSimpleName());
            applicationConfigManager.insertJobLog(jobLog);
        }
    }
}
