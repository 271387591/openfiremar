package com.ozstrategy.service.export.impl;

import com.ozstrategy.dao.export.MessageExportDao;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.service.export.MessageExportManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/11/15.
 */
@Service("messageExportManager")
public class MessageExportManagerImpl implements MessageExportManager {
    @Autowired
    private MessageExportDao messageExportDao;
    @Autowired
    private HistoryMessageDao historyMessageDao;
    public List<MessageExport> list(Map<String, Object> map, Integer start, Integer limit)  throws Exception{
        return messageExportDao.list(map,new RowBounds(start,limit));
    }

    public Integer listCount(Map<String, Object> map) throws Exception {
        return messageExportDao.listCount(map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save(MessageExport messageExport) throws Exception {
        messageExportDao.save(messageExport);
    }

    public MessageExport getById(Long id) throws Exception {
        return messageExportDao.get(id);
        
    }

    public void exportMessage(Date startTime, Date endTime, File folder,Long projectId) throws Exception {
        historyMessageDao.exportMessage(startTime, endTime, folder, projectId);
    }

    public void exportVoice(Date startTime, Date endTime, File folder, Long projectId) throws Exception {
        historyMessageDao.exportVoice(startTime,endTime,folder,projectId);
    }

    public boolean checkExportDataExist(Date startTime, Date endTime,Long projectId) throws Exception {
        return historyMessageDao.checkExportDataExist(startTime,endTime,projectId);
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(MessageExport messageExport) throws Exception {
        messageExportDao.delete(messageExport);
    }
}
