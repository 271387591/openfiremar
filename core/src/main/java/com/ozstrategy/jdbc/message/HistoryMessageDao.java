package com.ozstrategy.jdbc.message;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageDao {
    Long maxId();
    Map<String,Object> maxMinIdByTime(Date startTime, Date endTime,Long projectId);
    
    void deleteByIds(List<Long> ids);
    List<Map<String, Object>> getIdByBetween(Long minId,Long maxId,Long projectId,Integer limit);
    
    void delete(Date startTime, Date endTime,Long projectId);
    void exportMessage(Date startTime,Date endTime,File folder,Long projectId) throws Exception;
    void exportVoice(Date startTime,Date endTime,File folder,Long projectId) throws Exception;
    boolean checkExportDataExist(Date startTime,Date endTime,Long projectId);
    
    void deleteMessage(Long projectId,String messageId);

    List<Map<String,String>> search(String message,Date startDate, Date endDate, Long fromId, Long projectId,Long manager, Long deleted,Long pillowTalk,Integer start, Integer limit) throws Exception;
    
    Long addIndex(Long minId)throws Exception;
    
    void deleteIndex(Date startDate,Date endDate,Long projectId) throws Exception;


    List<Map<String,Object>> getHistory(Long projectId, Integer manager,Integer roleB,Integer start,Integer limit) throws Exception;
    Integer getHistoryCount(Long projectId,Integer manager,Integer roleB);
    
    
    
}
