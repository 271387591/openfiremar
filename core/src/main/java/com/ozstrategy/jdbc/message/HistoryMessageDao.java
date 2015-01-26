package com.ozstrategy.jdbc.message;

import com.ozstrategy.model.openfire.HistoryMessage;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageDao {
    Long maxId();
    void delete(Date startTime, Date endTime,Long projectId);
    Long addIndex(Long index_max_id) throws Exception;
    void exportMessage(Date startTime,Date endTime,File folder,Long projectId) throws Exception;
    void exportVoice(Date startTime,Date endTime,File folder,Long projectId) throws Exception;
    Integer countTime(Date startTime,Date endTime) throws Exception;
    boolean checkExportDataExist(Date startTime,Date endTime,Long projectId);

    List<HistoryMessage> listHistoryMessagesFromDb(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listHistoryMessagesFromDbCount(Map<String,Object> map)throws Exception;
    
    List<HistoryMessage> listHistoryMessagesStore(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listHistoryMessagesStoreCount(Map<String,Object> map)throws Exception;

    List<HistoryMessage> listManagerMessages(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listManagerMessagesCount(Map<String,Object> map)throws Exception;

    void deleteMessage(Long projectId,String messageId);
    
    
    
}
