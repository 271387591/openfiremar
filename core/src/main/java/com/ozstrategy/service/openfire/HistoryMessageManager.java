package com.ozstrategy.service.openfire;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageManager {
    Long maxId();
    Long addIndex()throws Exception;
    void delete(Date startTime,Date endTime,Long projectId) throws Exception;
    
    void deleteMessage(Long projectId,String messageId) throws Exception;
    
    List<Map<String,String>> search(String message,Date startDate, Date endDate, Long fromId, Long projectId,Long manager, Long deleted,Integer start, Integer limit) throws Exception;
    
    List<Map<String,Object>> getHistory(Long projectId,Integer start,Integer limit) throws Exception;
    Integer getHistoryCount(Long projectId);
    
    
}
