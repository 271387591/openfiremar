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
    public List<MessageExport> list(Map<String, Object> map, Integer start, Integer limit) {
        return messageExportDao.list(map,new RowBounds(start,limit));
    }

    public Integer listCount(Map<String, Object> map) {
        return messageExportDao.listCount(map);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save(MessageExport messageExport) {
        messageExportDao.save(messageExport);
    }

    public MessageExport getById(Long id) {
        return messageExportDao.get(id);
    }

    public void exportMessage(Date startTime, Date endTime, File folder) throws Exception {
        historyMessageDao.export(startTime,endTime,folder);
    }

    public boolean checkExportDataExist(Date startTime, Date endTime) throws Exception {
        return historyMessageDao.checkExportDataExist(startTime,endTime);
    }
}
