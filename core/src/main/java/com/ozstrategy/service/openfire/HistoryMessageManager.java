package com.ozstrategy.service.openfire;

import com.ozstrategy.model.openfire.HistoryMessage;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageManager {
    List<HistoryMessage> listHistoryMessages(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listHistoryMessagesCount(Map<String,Object> map)throws Exception;
    Long maxId();
    void addIndex(Long index_max_id)throws Exception;
    void delete(Date startTime,Date endTime) throws Exception;
    
}