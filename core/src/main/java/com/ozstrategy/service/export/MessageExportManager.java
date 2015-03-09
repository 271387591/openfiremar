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
    List<MessageExport> list(Map<String,Object> map,Integer start,Integer limit) throws Exception;
    Integer listCount(Map<String,Object> map) throws Exception;
    void save(MessageExport messageExport) throws Exception;
    MessageExport getById(Long id) throws Exception;
    void exportMessage(Date startTime, Date endTime,File folder,Long projectId) throws Exception;
    void exportVoice(Date startTime, Date endTime,File folder,Long projectId) throws Exception;
    boolean checkExportDataExist(Date startTime,Date endTime,Long projectId) throws Exception;
    void delete(MessageExport messageExport) throws Exception;
}
