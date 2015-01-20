package com.ozstrategy.webapp.controller.export;

import com.ozstrategy.Constants;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.service.export.MessageExportManager;
import com.ozstrategy.util.FileHelper;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.command.export.MessageExportCommand;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.ozstrategy.Constants.exportFileDir;

/**
 * Created by lihao on 1/11/15.
 */
@Controller
@RequestMapping("messageExportController.do")
public class MessageExportController extends BaseController {
    
    @Autowired
    private MessageExportManager messageExportManager;
    private static Map<String,Boolean> detailMap=new ConcurrentHashMap<String, Boolean>();
    private static Map<String,Boolean> finishedMap=new ConcurrentHashMap<String, Boolean>();
    private ExecutorService executorService= Executors.newFixedThreadPool(10);
    
    

    @RequestMapping(params = "method=list")
    @ResponseBody
    public JsonReaderResponse<MessageExportCommand> list(HttpServletRequest request){
        List<MessageExportCommand> commands=new ArrayList<MessageExportCommand>();
        Integer start=parseInteger(request.getParameter("start"));
        Integer limit=parseInteger(request.getParameter("limit"));
        Map<String,Object> map=requestMap(request);
        List<MessageExport> projects=messageExportManager.list(map, start, limit);
        if(projects!=null && projects.size()>0){
            for(MessageExport project : projects){
                commands.add(new MessageExportCommand(project));
            }
        }
        int count = messageExportManager.listCount(map);
        return new JsonReaderResponse<MessageExportCommand>(commands,"",count);
    }
    @RequestMapping(params = "method=pullNotification")
    @ResponseBody
    public Map<String,Boolean> pullExportNotification(HttpServletRequest request){
        String username=request.getRemoteUser();
        Boolean result=detailMap.get(username);
        Boolean finished=finishedMap.get(username);
        Map<String,Boolean> map=new HashMap<String, Boolean>();
        map.put("state",result);
        map.put("finished",finished);
        map.put("success",true);
        return map;
    }
    
    @RequestMapping(params = "method=export")
    @ResponseBody
    public Map<String,Object> export(final HttpServletRequest request, HttpServletResponse response){
        final String username=request.getRemoteUser();
        final String startTime=request.getParameter("startTime");
        final String endTime=request.getParameter("endTime");
        Map<String,Object> result=new HashMap<String, Object>();
        Date ssDate= null;
        Date eeDate=null;
        try {
            ssDate = DateUtils.parseDate(startTime, new String[]{Constants.YMD, Constants.YMDHMS});
            eeDate= DateUtils.parseDate(endTime,new String[]{Constants.YMD,Constants.YMDHMS});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date sDate=ssDate; 
        final Date eDate=eeDate; 
        
        finishedMap.put(username,false);
        try {
            if(!messageExportManager.checkExportDataExist(sDate,eDate)){
                result.put("success",false);
                result.put("message","没有数据可导出");
                return result;
            }
        } catch (Exception e) {
            result.put("success",false);
            result.put("message","查询数据错误");
            return result;
        }
        class Exportor implements Runnable{
            public void run(){
                File finalUploadedFile=null;
                try{
                    
                    String attachFilesDirStr = request.getRealPath("/") + "/"+exportFileDir+"/";
                    attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
                    File fileDir = new File(attachFilesDirStr);
                    if (fileDir.exists() == false) {
                        fileDir.mkdir();
                    }
                    String           folderName          = UUID.randomUUID().toString();
                    finalUploadedFile   = new File(fileDir, folderName);
                    if(!finalUploadedFile.exists()){
                        finalUploadedFile.mkdir();
                    }
                    messageExportManager.exportMessage(sDate,eDate,finalUploadedFile);
                    String finalExportZipFileName= "聊天记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS);
                    File zipFile = FileHelper.fileToZip(finalUploadedFile, fileDir, finalExportZipFileName);
                    FileHelper.deleteDirectory(finalUploadedFile);
                    MessageExport messageExport=new MessageExport();
                    messageExport.setCreateDate(new Date());
                    messageExport.setLastUpdateDate(new Date());
                    messageExport.setType(ExportType.MessagePicture);
                    messageExport.setExportor(username);
                    messageExport.setExecuteDate(new Date());
                    messageExport.setFilePath(zipFile.getAbsolutePath());
                    messageExportManager.save(messageExport);
                    detailMap.put(username,true);
                    finishedMap.put(username,true);
                }catch (Exception e){
                    logger.error("export message fail",e);
                    FileHelper.deleteDirectory(finalUploadedFile);
                    detailMap.put(username,false);
                    finishedMap.put(username,true);
                }

            }
        }
        
        try{
            executorService.execute(new Exportor());
            result.put("success",true);
            result.put("message","");
            return result;
        }catch (Exception e){
            logger.error("export start thread fail", e);
            detailMap.put(username, false);
        }
        result.put("success",false);
        result.put("message","导出数据异常");
        finishedMap.put(username,true);
        return result;
    }
    
    
    @RequestMapping(params = "method=download")
    public void download(HttpServletRequest request, HttpServletResponse response){
        OutputStream outputStream=null;
        try{
            String id=request.getParameter("id");
            if(StringUtils.isNotEmpty(id)){
                MessageExport messageExport=messageExportManager.getById(parseLong(id));
                if(messageExport!=null){
                    String filePath=messageExport.getFilePath();
                    File file=new File(filePath);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    int             len;
                    byte[]          readBuffer      = new byte[2048];
                    String finalExportZipFileName=new String(file.getName().getBytes("UTF-8"),"ISO8859-1");
                    response.setCharacterEncoding("UTF-8");
                    response.addHeader("Content-Disposition", "Attachment;FileName=" + finalExportZipFileName);
                    response.addHeader("Content-Length",""+file.length());
                    response.setContentType("APPLICATION/ZIP");
//                    response.setContentType("application/octet-stream");
                    outputStream=response.getOutputStream();

                    while ((len = fileInputStream.read(readBuffer, 0, 2048)) != -1) {
                        outputStream.write(readBuffer, 0, len);
                    }
                    fileInputStream.close();
                    outputStream.flush();
                    outputStream.close();
                }
            }
        }catch (Exception e){
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    
}
