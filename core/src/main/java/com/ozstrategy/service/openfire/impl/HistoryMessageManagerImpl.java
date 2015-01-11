package com.ozstrategy.service.openfire.impl;

import com.ozstrategy.dao.openfire.HistoryMessageDao;
import com.ozstrategy.dao.system.ApplicationConfigDao;
import com.ozstrategy.model.openfire.HistoryMessage;
import com.ozstrategy.model.system.ApplicationConfig;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.util.LuceneUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
@Service("historyMessageManager")
public class HistoryMessageManagerImpl implements HistoryMessageManager {
    @Autowired
    private HistoryMessageDao historyMessageDao;
    @Autowired
    private ApplicationConfigDao applicationConfigDao;
    private static final Integer maxCount = 20000;

    public List<HistoryMessage> listHistoryMessages(Map<String, Object> map, Integer page, Integer limit, Boolean search) throws Exception {
        if (search) {
            String message = ObjectUtils.toString(map.get("message"));
            String fromNick = ObjectUtils.toString(map.get("fromNick"));
            String toNick = ObjectUtils.toString(map.get("toNick"));
            String startTimeStr = ObjectUtils.toString(map.get("startTime"));
            String endTimeStr = ObjectUtils.toString(map.get("endTime"));
            Long startTime = null;
            Long endTime = null;
            if (StringUtils.isNotEmpty(startTimeStr)) {
                Date sDate = DateUtils.parseDate(startTimeStr, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
                startTime = sDate.getTime();
            }
            if (StringUtils.isNotEmpty(endTimeStr)) {
                Date sDate = DateUtils.parseDate(endTimeStr, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
                endTime = sDate.getTime();
            }
            return LuceneUtils.search(message, fromNick, toNick, startTime, endTime, limit, page);
        }
        return historyMessageDao.listHistoryMessages(map, new RowBounds(limit, page));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addIndex(Long index_max_id, Long max_id) throws Exception {
        List<HistoryMessage> historyMessages = historyMessageDao.listHistoryMessageLimitId(index_max_id, new RowBounds(0, maxCount));
        if (historyMessages != null && !historyMessages.isEmpty()) {
            Long existId = LuceneUtils.addIndex(historyMessages);
            if (existId >= max_id && existId != 0) {
                applicationConfigDao.put(ApplicationConfig.index_max_id, existId.toString());
                return;
            }
            addIndex(existId, max_id);
        }
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Date startTime, Date endTime)  throws Exception{
        historyMessageDao.delete(startTime,endTime);
        LuceneUtils.deleteIndex(startTime.getTime(),endTime.getTime());
        
    }

    public Integer listHistoryMessagesCount(Map<String, Object> map, Boolean search) throws Exception {
        if (search) {
            String message = ObjectUtils.toString(map.get("message"));
            String fromNick = ObjectUtils.toString(map.get("fromNick"));
            String toNick = ObjectUtils.toString(map.get("toNick"));
            String startTimeStr = ObjectUtils.toString(map.get("startTime"));
            String endTimeStr = ObjectUtils.toString(map.get("endTime"));
            Long startTime = null;
            Long endTime = null;
            if (StringUtils.isNotEmpty(startTimeStr)) {
                startTime = NumberUtils.toLong(startTimeStr);
            }
            if (StringUtils.isNotEmpty(endTimeStr)) {
                endTime = NumberUtils.toLong(endTimeStr);
            }
            return LuceneUtils.count(message, fromNick, toNick, startTime, endTime);
        }
        return historyMessageDao.listHistoryMessagesCount(map);
    }

    public Long maxId() {
        return historyMessageDao.maxId();
    }

}
