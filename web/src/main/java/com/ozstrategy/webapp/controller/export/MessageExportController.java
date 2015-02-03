package com.ozstrategy.webapp.controller.export;

import com.ozstrategy.Constants;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.export.MessageExportManager;
import com.ozstrategy.service.project.ProjectManager;
import com.ozstrategy.util.FileHelper;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.command.export.MessageExportCommand;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    private ProjectManager projectManager;
    
    private static Map<String,Boolean> detailMap=new ConcurrentHashMap<String, Boolean>();
    private static Map<String,Boolean> finishedMap=new ConcurrentHashMap<String, Boolean>();
    private ExecutorService executorService= Executors.newFixedThreadPool(10);
    private static final int maxVoiceItem=18000;
    private static final int maxMessageItem=100;
    
    

    @RequestMapping(params = "method=list")
    @ResponseBody
    public JsonReaderResponse<MessageExportCommand> list(HttpServletRequest request){
        List<MessageExportCommand> commands=new ArrayList<MessageExportCommand>();
        Integer start=parseInteger(request.getParameter("start"));
        Integer limit=parseInteger(request.getParameter("limit"));
        Map<String,Object> map=requestMap(request);
        List<MessageExport> projects= null;
        try {
            projects = messageExportManager.list(map, start, limit);
        } catch (Exception e) {
            logger.error("get message export fail");
        }
        if(projects!=null && projects.size()>0){
            for(MessageExport project : projects){
                commands.add(new MessageExportCommand(project));
            }
        }
        int count = 0;
        try {
            count = messageExportManager.listCount(map);
        } catch (Exception e) {
            logger.error("get message export count fail");
        }
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
    public Map<String,Object> export(final HttpServletRequest request, HttpServletResponse response) throws Exception{
        final String username=request.getRemoteUser();
        final String startTime=request.getParameter("startTime");
        final String endTime=request.getParameter("endTime");
        final Long projectId=parseLong(request.getParameter("projectId"));
        final String type=request.getParameter("type");
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
            if(!messageExportManager.checkExportDataExist(sDate,eDate,projectId)){
                result.put("success",false);
                result.put("message",getZhMessage("projectRes.noDataExport"));
                return result;
            }
        } catch (Exception e) {
            result.put("success",false);
            result.put("message",getZhMessage("projectRes.queryDataError"));
            return result;
        }
        String contextPath= request.getRealPath("/") + "/"+exportFileDir+"/";
        final String attachFilesDirStr = FilenameUtils.normalize(contextPath);
        final File fileDir = new File(attachFilesDirStr);
        if (!fileDir.exists()) {
            boolean r = fileDir.mkdir();
            if(!r){
                MessageExport messageExport=new MessageExport();
                messageExport.setCreateDate(new Date());
                messageExport.setLastUpdateDate(new Date());
                messageExport.setType(ExportType.valueOf(type));
                messageExport.setExportor(username);
                messageExport.setExecuteDate(new Date());
                messageExport.setProjectId(projectId);
                logger.error("create export dir fail");
                try {
                    messageExportManager.save(messageExport);
                } catch (Exception e1) {
                }
                detailMap.put(username,false);
                finishedMap.put(username,true);
                throw new Exception("create export file dir fail.");
            }
        }
        class Exportor implements Runnable{
            public void run(){
                File finalUploadedFile=null;
                try{
                    
                    String           folderName          = UUID.randomUUID().toString();
                    finalUploadedFile   = new File(fileDir, folderName);
                    if(!finalUploadedFile.exists()){
                        finalUploadedFile.mkdir();
                    }
                    Project project=projectManager.getProjectById(projectId);
                    String finalExportZipFileName=null;
                    if(StringUtils.equals(ExportType.MessagePicture.name(),type)){
                        messageExportManager.exportMessage(sDate,eDate,finalUploadedFile,project.getId());
                        File[] files=finalUploadedFile.listFiles();
                        int len=files.length;
                        int index=0;
                        if(len>maxMessageItem){
                            File tempFile=null;
                            String targetMultiFileName  = UUID.randomUUID().toString();
                            File targetMultiFile=new File(fileDir,targetMultiFileName);
                            targetMultiFile.mkdir();
                            for(int i=0;i<len;i++){
                                File file=files[i];
                                if(i % maxMessageItem==0){
                                    if(tempFile!=null){
                                        index++;
                                        finalExportZipFileName= "工程("+project.getName()+")聊天记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS)+index;
                                        FileHelper.fileToZip(tempFile, targetMultiFile, finalExportZipFileName);
                                        FileHelper.deleteDirectory(tempFile);
                                    }
                                    String tempDir=UUID.randomUUID().toString();
                                    tempFile=new File(finalUploadedFile,tempDir);
                                    tempFile.mkdir();
                                }
                                if(tempFile!=null){
                                    FileUtils.copyFileToDirectory(file,tempFile);
                                }
                            }
                            if(tempFile!=null){
                                index++;
                                finalExportZipFileName= "工程("+project.getName()+")聊天记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS)+index;
                                FileHelper.fileToZip(tempFile, targetMultiFile, finalExportZipFileName);
                                FileHelper.deleteDirectory(tempFile);
                            }
                            FileHelper.deleteDirectory(finalUploadedFile);
                            MessageExport messageExport=new MessageExport();
                            messageExport.setCreateDate(new Date());
                            messageExport.setLastUpdateDate(new Date());
                            messageExport.setType(ExportType.valueOf(type));
                            messageExport.setExportor(username);
                            messageExport.setExecuteDate(new Date());
                            messageExport.setFilePath(targetMultiFile.getAbsolutePath());
                            messageExport.setProjectId(projectId);
                            messageExport.setMultiFile(Boolean.TRUE);
                            messageExportManager.save(messageExport);
                            detailMap.put(username,true);
                            finishedMap.put(username,true);
                        }else{
                            finalExportZipFileName= "工程("+project.getName()+")聊天记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS);
                            File zipFile = FileHelper.fileToZip(finalUploadedFile, fileDir, finalExportZipFileName);
                            FileHelper.deleteDirectory(finalUploadedFile);
                            MessageExport messageExport=new MessageExport();
                            messageExport.setCreateDate(new Date());
                            messageExport.setLastUpdateDate(new Date());
                            messageExport.setType(ExportType.valueOf(type));
                            messageExport.setExportor(username);
                            messageExport.setExecuteDate(new Date());
                            messageExport.setFilePath(zipFile.getAbsolutePath());
                            messageExport.setProjectId(projectId);
                            messageExportManager.save(messageExport);
                            detailMap.put(username,true);
                            finishedMap.put(username,true);
                            
                        }
                    }else if(StringUtils.equals(ExportType.Voice.name(),type)){
                        messageExportManager.exportVoice(sDate, eDate, finalUploadedFile, project.getId());
                        File[] files=finalUploadedFile.listFiles();
                        int len=files.length;
                        int index=0;
                        if(len>maxVoiceItem){
                            File tempFile=null;
                            String targetMultiFileName  = UUID.randomUUID().toString();
                            File targetMultiFile=new File(fileDir,targetMultiFileName);
                            targetMultiFile.mkdir();
                            for(int i=0;i<len;i++){
                                File file=files[i];
                                if(i % maxVoiceItem==0){
                                    if(tempFile!=null){
                                        index++;
                                        finalExportZipFileName= "工程("+project.getName()+")语音记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS)+index;
                                        FileHelper.fileToZip(tempFile, targetMultiFile, finalExportZipFileName);
                                        FileHelper.deleteDirectory(tempFile);
                                    }
                                    String tempDir=UUID.randomUUID().toString();
                                    tempFile=new File(finalUploadedFile,tempDir);
                                    tempFile.mkdir();
                                }
                                if(tempFile!=null){
                                    FileUtils.copyFileToDirectory(file,tempFile);
                                }
                            }
                            if(tempFile!=null){
                                index++;
                                finalExportZipFileName= "工程("+project.getName()+")语音记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS)+index;
                                FileHelper.fileToZip(tempFile, targetMultiFile, finalExportZipFileName);
                                FileHelper.deleteDirectory(tempFile);
                            }
                            FileHelper.deleteDirectory(finalUploadedFile);
                            MessageExport messageExport=new MessageExport();
                            messageExport.setCreateDate(new Date());
                            messageExport.setLastUpdateDate(new Date());
                            messageExport.setType(ExportType.valueOf(type));
                            messageExport.setExportor(username);
                            messageExport.setExecuteDate(new Date());
                            messageExport.setFilePath(targetMultiFile.getAbsolutePath());
                            messageExport.setProjectId(projectId);
                            messageExport.setMultiFile(Boolean.TRUE);
                            messageExportManager.save(messageExport);
                            detailMap.put(username,true);
                            finishedMap.put(username,true);
                        }else{
                            finalExportZipFileName= "工程("+project.getName()+")语音记录"+DateFormatUtils.format(new Date(),Constants.YMDHMS);
                            File zipFile = FileHelper.fileToZip(finalUploadedFile, fileDir, finalExportZipFileName);
                            FileHelper.deleteDirectory(finalUploadedFile);
                            MessageExport messageExport=new MessageExport();
                            messageExport.setCreateDate(new Date());
                            messageExport.setLastUpdateDate(new Date());
                            messageExport.setType(ExportType.valueOf(type));
                            messageExport.setExportor(username);
                            messageExport.setExecuteDate(new Date());
                            messageExport.setFilePath(zipFile.getAbsolutePath());
                            messageExport.setProjectId(projectId);
                            messageExportManager.save(messageExport);
                            detailMap.put(username,true);
                            finishedMap.put(username,true);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    MessageExport messageExport=new MessageExport();
                    messageExport.setCreateDate(new Date());
                    messageExport.setLastUpdateDate(new Date());
                    messageExport.setType(ExportType.valueOf(type));
                    messageExport.setExportor(username);
                    messageExport.setExecuteDate(new Date());
                    messageExport.setProjectId(projectId);
                    logger.error("export message fail",e);
                    try {
                        messageExportManager.save(messageExport);
                        FileHelper.deleteDirectory(finalUploadedFile);
                    } catch (Exception e1) {
                    }
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
        result.put("message",getZhMessage("projectRes.exportDataException"));
        finishedMap.put(username,true);
        return result;
    }
    @ExceptionHandler(value = Exception.class)
    public ModelAndView exception(){
        return new  ModelAndView("error");
    } 
    
    @RequestMapping(value = "multiDownload")
    public ModelAndView downloadMultiFile(HttpServletRequest request) throws Exception{
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        Long id=parseLong(request.getParameter("id"));
        MessageExport messageExport=messageExportManager.getById(id);
        if(messageExport!=null){
            String filePath=messageExport.getFilePath();
            String host=request.getServerName();
            String contextPath=request.getContextPath();
            if(StringUtils.isNotEmpty(filePath)){
                File fileDir=new File(filePath);
                File[] files=fileDir.listFiles();
                int len=files.length;
                for(int i=0;i<len;i++){
                    File file=files[i];
                    String name=file.getName();
                    String httpPath="http://"+host+contextPath+"/"+exportFileDir+"/"+name;
                    Map<String,String> map=new HashMap<String, String>();
                    map.put("id",i+"");
                    map.put("httpPath",httpPath);
                    map.put("name",name);
                    list.add(map);
                }
                return new ModelAndView("multiDownload","data",list);
            }
        }
        return new ModelAndView("multiDownload","data",list);
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
