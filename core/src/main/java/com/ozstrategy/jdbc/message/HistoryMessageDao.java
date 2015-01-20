package com.ozstrategy.jdbc.message;

import java.io.File;
import java.util.Date;

/**
 * Created by lihao on 1/7/15.
 */
public interface HistoryMessageDao {
    Long maxId();
    void delete(Date startTime, Date endTime);
    Long addIndex(Long index_max_id) throws Exception;
    void export(Date startTime,Date endTime,File folder) throws Exception;
    Integer countTime(Date startTime,Date endTime) throws Exception;
    boolean checkExportDataExist(Date startTime,Date endTime);
}
