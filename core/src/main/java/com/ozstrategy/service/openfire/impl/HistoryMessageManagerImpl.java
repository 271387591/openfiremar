package com.ozstrategy.service.openfire.impl;

import com.ozstrategy.dao.system.ApplicationConfigDao;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.system.ApplicationConfig;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lihao on 1/7/15.
 */
@Service("historyMessageManager")
public class HistoryMessageManagerImpl implements HistoryMessageManager {
    @Autowired
    private HistoryMessageDao historyMessageDao;
    @Autowired
    private ApplicationConfigDao applicationConfigDao;
    private Integer deleteItem=50000;
    @Autowired
    private Properties variable;

    @Transactional(rollbackFor = Throwable.class)
    public Long addIndex() throws Exception {
        Long maxId=maxId();
        Long indexMaxId= maxIndex();
        if(indexMaxId<maxId){
            Long id=historyMessageDao.addIndex(indexMaxId);
            applicationConfigDao.put(ApplicationConfig.index_max_id, id.toString());
        }
        return maxId;
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Date startTime, Date endTime,Long projectId)  throws Exception{
        Map<String,Object> maxMinId=historyMessageDao.maxMinIdByTime(startTime,endTime,projectId);
        Long maxId=0L;
        Long minId=0L;
        if(maxMinId!=null && maxMinId.size()>0){
            maxId=NumberUtils.toLong(ObjectUtils.toString(maxMinId.get("max")))+1;
            if(maxId==null){
                maxId=0L;
            }
            minId=NumberUtils.toLong(ObjectUtils.toString(maxMinId.get("min")))-1;
            if(minId==null){
                minId=0L;
            }
            Long mId=minId;
            do {
                mId=delete(mId,maxId,projectId);
            }while (mId!=maxId);
            historyMessageDao.deleteIndex(startTime,endTime,projectId);
        }
    }
    private Long delete(Long minId,Long maxId,Long projectId) throws Exception{
        Long mId=0L;
        deleteItem=NumberUtils.toInt(variable.get("deleteItem").toString());
        List<Map<String,Object>> list=historyMessageDao.getIdByBetween(minId,maxId,projectId,deleteItem);
        if(list!=null && list.size()>0){
            List<Long> longs=new ArrayList<Long>();
            for(Map<String,Object> map : list){
                Long id=NumberUtils.toLong(ObjectUtils.toString(map.get("id")));
                longs.add(id);
                mId=id;
            }
            historyMessageDao.deleteByIds(longs);
        }else{
            mId=maxId;
        }
        return mId;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteMessage(Long projectId, String messageId) throws Exception {
        historyMessageDao.deleteMessage(projectId,messageId);
    }

    @Cacheable(value = "messageCache")
    public List<Map<String, String>> search(String message,Date startDate, Date endDate, Long fromId, Long projectId,Long manager, Long deleted,Integer start, Integer limit) throws Exception {
        return historyMessageDao.search(message, startDate, endDate, fromId, projectId, manager, deleted,start, limit);
    }

    public List<Map<String, Object>> getHistory(Long projectId, Integer start, Integer limit) throws Exception {
        return historyMessageDao.getHistory(projectId,start,limit);
    }

    public Integer getHistoryCount(Long projectId) {
        return historyMessageDao.getHistoryCount(projectId);
    }

    public Long maxIndex() throws Exception {
        Long indexMaxId= NumberUtils.toLong(applicationConfigDao.get(ApplicationConfig.index_max_id));
        return indexMaxId;
    }

    public Long maxId() {
        return historyMessageDao.maxId();
    }

}
