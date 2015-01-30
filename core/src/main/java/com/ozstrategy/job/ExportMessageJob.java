package com.ozstrategy.job;

import com.ozstrategy.Constants;
import com.ozstrategy.model.export.ExportType;
import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.model.project.Project;
import com.ozstrategy.service.export.MessageExportManager;
import com.ozstrategy.service.project.ProjectManager;
import com.ozstrategy.util.FileHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ozstrategy.Constants.exportFileDir;

/**
 * Created by lihao on 1/20/15.
 */
public class ExportMessageJob {

    @Autowired
    private MessageExportManager messageExportManager;
    @Autowired
    private ProjectManager projectManager;
    
    private Log log= LogFactory.getLog(getClass());
    public void exportMessage() throws Exception{
        List<Project> projects=projectManager.listAllProjects();
        if(projects!=null && projects.size()>0){
            for(Project project : projects){
                try{
                    Calendar calendar=Calendar.getInstance();
                    calendar.add(Calendar.MONTH,-1);
                    Date sDate=calendar.getTime();
                    Date eDate=new Date();
                    String attachFilesDirStr = Constants.imDataDir + "/"+exportFileDir+"/";
                    attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
                    File fileDir = new File(attachFilesDirStr);
                    if (fileDir.exists() == false) {
                        fileDir.mkdir();
                    }
                    String           folderName          = UUID.randomUUID().toString();
                    File finalUploadedFile   = new File(fileDir, folderName);
                    if(!finalUploadedFile.exists()){
                        finalUploadedFile.mkdir();
                    }
                    messageExportManager.exportMessage(sDate,eDate,finalUploadedFile,project.getId());
                    String finalExportZipFileName= "工程("+project.getName()+")聊天记录"+ DateFormatUtils.format(new Date(), Constants.YMDHMS);
                    File zipFile = FileHelper.fileToZip(finalUploadedFile, fileDir, finalExportZipFileName);
                    FileHelper.deleteDirectory(finalUploadedFile);
                    MessageExport messageExport=new MessageExport();
                    messageExport.setCreateDate(new Date());
                    messageExport.setLastUpdateDate(new Date());
                    messageExport.setType(ExportType.MessagePicture);
                    messageExport.setExecuteDate(new Date());
                    messageExport.setFilePath(zipFile.getAbsolutePath());
                    messageExport.setProjectId(project.getId());
                    messageExportManager.save(messageExport);
                    Thread.currentThread().sleep(3000);
                    
                }catch (Exception e){
                    log.error("export message job fail",e);
                    MessageExport messageExport=new MessageExport();
                    messageExport.setCreateDate(new Date());
                    messageExport.setLastUpdateDate(new Date());
                    messageExport.setType(ExportType.MessagePicture);
                    messageExport.setExecuteDate(new Date());
                    messageExport.setExceptions(e.getMessage());
                    messageExport.setProjectId(project.getId());
                    messageExportManager.save(messageExport);
                }
                
            }
        }
    }
}
