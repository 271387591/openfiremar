package com.ozstrategy.webapp.controller.openfire;

import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.openfire.HistoryMessageManager;
import com.ozstrategy.service.userrole.UserManager;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
    private UserManager userManager;
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
    public JsonReaderResponse<Map<String,Object>> listStore(HttpServletRequest request) throws Exception{
        try{
            Integer start=parseInteger(request.getParameter("start"));
            Integer limit=parseInteger(request.getParameter("limit"));
            Long projectId=parseLong(request.getParameter("projectId"));
            String formNick=request.getParameter("fromNick");
            String startTime=request.getParameter("startTime");
            String endTime=request.getParameter("endTime");
            String message=request.getParameter("message");
            Long manager=parseLong(request.getParameter("manager"));
            Long deleted=parseLong(request.getParameter("deleted"));
            if(projectId==null){
                return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),false,0,"缺少projectId");
            }
            Long fromId=null;
            if(StringUtils.isNotEmpty(formNick)){
                User user=userManager.getUserByNickName(projectId,formNick);
                if(user==null){
                    return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),false,0,"昵称不存在");
                }
                fromId=user.getId();
            }
            Date sDate=null;
            Date eDate=null;
            if(StringUtils.isNotEmpty(startTime)){
                sDate=DateUtils.parseDate(startTime, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
            }
            if(StringUtils.isNotEmpty(endTime)){
                eDate=DateUtils.parseDate(endTime, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
            }
            Long maxId=historyMessageManager.addIndex();
            if(maxId==0L){
                return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),true,0,"");
            }
           
            List<Map<String,String>> projects=historyMessageManager.search(message,sDate, eDate, fromId, projectId,manager,deleted,start, limit);
            List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
            if(projects!=null && projects.size()>0){
                for(Map<String,String> map:projects){
                    Map<String,Object> newMap=new HashMap<String, Object>();
                    newMap.putAll(map);
                    Long createDate=NumberUtils.toLong(map.get("createDate"));
                    Date date=new Date(createDate);
                    newMap.put("createDate",date);
                    list.add(newMap);
                }
            }
            int count=0;
            if(projects!=null && projects.size()>0){
                Map<String,String> map=projects.get(0);
                count=NumberUtils.toInt(map.get("total"));
            }
            return new JsonReaderResponse<Map<String,Object>>(list,"",count);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),false,0,"数据查询失败");
    }
    @RequestMapping(params = "method=getHistory")
    @ResponseBody
    public JsonReaderResponse<Map<String,Object>> getHistory(HttpServletRequest request) throws Exception{
        try{
            Integer start=parseInteger(request.getParameter("start"));
            Integer limit=parseInteger(request.getParameter("limit"));
            Long projectId=parseLong(request.getParameter("projectId"));
            if(projectId==null){
                return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),false,0,"缺少projectId");
            }
            List<Map<String,Object>> projects=historyMessageManager.getHistory(projectId, start, limit);
            Integer count=historyMessageManager.getHistoryCount(projectId);
            return new JsonReaderResponse<Map<String,Object>>(projects,"",count);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("list HistoryMessage",e);
            
        }
        return new JsonReaderResponse<Map<String,Object>>(Collections.<Map<String,Object>>emptyList(),false,0,"数据查询失败");
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
