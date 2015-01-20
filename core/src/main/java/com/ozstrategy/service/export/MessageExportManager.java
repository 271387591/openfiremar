package com.ozstrategy.service.export;

import com.ozstrategy.model.export.MessageExport;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/11/15.
 */
public interface MessageExportManager {
    List<MessageExport> list(Map<String,Object> map,Integer start,Integer limit);
    Integer listCount(Map<String,Object> map);
    void save(MessageExport messageExport);
    MessageExport getById(Long id);
    void exportMessage(Date startTime, Date endTime,File folder) throws Exception;
    boolean checkExportDataExist(Date startTime,Date endTime) throws Exception;
}
