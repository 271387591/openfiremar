package com.ozstrategy.webapp.controller.openfire;

import com.ozstrategy.model.openfire.HistoryMessage;
import com.ozstrategy.model.system.ApplicationConfig;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.service.system.ApplicationConfigManager;
import com.ozstrategy.webapp.command.BaseResultCommand;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lihao on 1/7/15.
 */
@Controller
@RequestMapping("historyMessageController.do")
public class HistoryMessageController extends BaseController {
    @Autowired
    private HistoryMessageManager historyMessageManager;
    @Autowired
    private ApplicationConfigManager applicationConfigManager;
    
    @RequestMapping(params = "method=list")
    @ResponseBody
    public JsonReaderResponse<HistoryMessage> list(HttpServletRequest request){
        Integer start=parseInteger(request.getParameter("start"));
        Integer limit=parseInteger(request.getParameter("limit"));
        Map<String,Object> map=requestMap(request);
        try{
            String value = applicationConfigManager.get(ApplicationConfig.index_max_id);
            Long index_max_id= NumberUtils.toLong(value);
            Long max_id=historyMessageManager.maxId();
            if(index_max_id<max_id){
                historyMessageManager.addIndex(index_max_id);
            }
            List<HistoryMessage> projects=historyMessageManager.listHistoryMessages(map, start, limit);
            int count = historyMessageManager.listHistoryMessagesCount(map);
            return new JsonReaderResponse<HistoryMessage>(projects,"",count);
        }catch (Exception e){
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),"",0);
    }
    @RequestMapping(params = "method=listFromDb")
    @ResponseBody
    public JsonReaderResponse<HistoryMessage> listFromDb(HttpServletRequest request){
        try{
            Integer start=parseInteger(request.getParameter("start"));
            Integer limit=parseInteger(request.getParameter("limit"));
            if(StringUtils.isEmpty(request.getParameter("projectId"))){
                return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),"缺少projectId",0);
            }
            Map<String,Object> map=requestMap(request);
           
            List<HistoryMessage> projects=historyMessageManager.listHistoryMessagesFromDb(map, start, limit);
            int count = historyMessageManager.listHistoryMessagesFromDbCount(map);
            return new JsonReaderResponse<HistoryMessage>(projects,"",count);
        }catch (Exception e){
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),false,0,"");
    }
    @RequestMapping(params = "method=listManagerMessages")
    @ResponseBody
    public JsonReaderResponse<HistoryMessage> listManagerMessages(HttpServletRequest request){
        try{
            Integer start=parseInteger(request.getParameter("start"));
            Integer limit=parseInteger(request.getParameter("limit"));
            if(StringUtils.isEmpty(request.getParameter("projectId"))){
                return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),"缺少projectId",0);
            }
            Map<String,Object> map=requestMap(request);
           
            List<HistoryMessage> projects=historyMessageManager.listManagerMessages(map, start, limit);
            int count = historyMessageManager.listManagerMessagesCount(map);
            return new JsonReaderResponse<HistoryMessage>(projects,"",count);
        }catch (Exception e){
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),false,0,"");
    }
    
    @RequestMapping(params = "method=deleteMessage")
    @ResponseBody
    public BaseResultCommand deleteMessage(HttpServletRequest request){
        try{
            Long projectId=parseLong(request.getParameter("projectId"));
            String messageId=request.getParameter("messageId");
            if(projectId==null){
                return new BaseResultCommand("缺少projectId",false);
            }
            if(StringUtils.isEmpty(messageId)){
                return new BaseResultCommand("缺少messageId",false);
            }
            historyMessageManager.deleteMessage(projectId,messageId);
            return new BaseResultCommand("",true);
        }catch (Exception e){
            logger.error("list HistoryMessage",e);
            
        }
        return new BaseResultCommand("删除消息失败",false);
    }
    
    @RequestMapping(params = "method=listStore")
    @ResponseBody
    public JsonReaderResponse<HistoryMessage> listStore(HttpServletRequest request){
        try{
            Integer start=parseInteger(request.getParameter("start"));
            Integer limit=parseInteger(request.getParameter("limit"));
            if(StringUtils.isEmpty(request.getParameter("projectId"))){
                return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),"缺少projectId",0);
            }
            Map<String,Object> map=requestMap(request);
           
            List<HistoryMessage> projects=historyMessageManager.listHistoryMessagesStore(map, start, limit);
            int count = historyMessageManager.listHistoryMessagesStoreCount(map);
            return new JsonReaderResponse<HistoryMessage>(projects,"",count);
        }catch (Exception e){
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<HistoryMessage>(Collections.<HistoryMessage>emptyList(),false,0,"");
    }
    
    
    @RequestMapping(params = "method=delete")
    @ResponseBody
    public BaseResultCommand delete(HttpServletRequest request){
        try{
            Date sDate=DateUtils.parseDate(request.getParameter("startTime"), new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
            Date eDate=DateUtils.parseDate(request.getParameter("endTime"), new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
            Long projectId=parseLong(request.getParameter("projectId"));
            historyMessageManager.delete(sDate,eDate,projectId);
            return new BaseResultCommand("",true);
        }catch (Exception e){
            logger.error("delete message",e);
        }
        return new BaseResultCommand(getMessage("globalRes.removeFail",request),Boolean.FALSE);
    }
}
