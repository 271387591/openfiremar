package com.ozstrategy.service.openfire.impl;

import com.ozstrategy.Constants;
import com.ozstrategy.jdbc.message.HistoryMessageDao;
import com.ozstrategy.dao.system.ApplicationConfigDao;
import com.ozstrategy.model.openfire.HistoryMessage;
import com.ozstrategy.model.system.ApplicationConfig;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.util.LuceneUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
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

    public List<HistoryMessage> listHistoryMessages(Map<String, Object> map, Integer start, Integer limit) throws Exception {
        String message = ObjectUtils.toString(map.get("message"));
        String fromNick = ObjectUtils.toString(map.get("fromNick"));
        String toNick = ObjectUtils.toString(map.get("toNick"));
        String startTimeStr = ObjectUtils.toString(map.get("startTime"));
        String endTimeStr = ObjectUtils.toString(map.get("endTime"));
        Long startTime = null;
        Long endTime = null;
        if (StringUtils.isNotEmpty(startTimeStr)) {
            Date sDate = DateUtils.parseDate(startTimeStr, new String[]{Constants.YMDHMS, Constants.YMD});
            startTime = sDate.getTime();
        }
        if (StringUtils.isNotEmpty(endTimeStr)) {
            Date sDate = DateUtils.parseDate(endTimeStr, new String[]{Constants.YMDHMS, Constants.YMD});
            endTime = sDate.getTime();
        }
        return LuceneUtils.search(message, fromNick, toNick, startTime, endTime, limit, start);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addIndex(Long index_max_id) throws Exception {
        Long id=historyMessageDao.addIndex(index_max_id);
        applicationConfigDao.put(ApplicationConfig.index_max_id, id.toString());
    }
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Date startTime, Date endTime)  throws Exception{
        historyMessageDao.delete(startTime,endTime);
        LuceneUtils.deleteIndex(startTime.getTime(),endTime.getTime());
        
    }

    public Integer listHistoryMessagesCount(Map<String, Object> map) throws Exception {
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

    public Long maxId() {
        return historyMessageDao.maxId();
    }

}
