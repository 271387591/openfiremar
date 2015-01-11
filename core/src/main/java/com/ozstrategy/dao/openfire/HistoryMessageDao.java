package com.ozstrategy.dao.openfire;

import com.ozstrategy.model.openfire.HistoryMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageDao {
    List<HistoryMessage> listHistoryMessages(Map<String,Object> map,RowBounds rowBounds);
    Integer listHistoryMessagesCount(Map<String,Object> map);
    Long maxId();
    List<HistoryMessage> listHistoryMessageLimitId(@Param("id")Long id,RowBounds rowBounds); 
    Integer listHistoryMessageLimitIdCount(@Param("id")Long id);
    void delete(@Param("startTime")Date startTime, @Param("endTime")Date endTime);
}
