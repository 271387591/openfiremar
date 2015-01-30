package com.ozstrategy.webapp.controller.appstore;

import com.ozstrategy.Constants;
import com.ozstrategy.model.appstore.AppStore;
import com.ozstrategy.model.appstore.Platform;
import com.ozstrategy.service.appstore.AppStoreManager;
import com.ozstrategy.webapp.command.BaseResultCommand;
import com.ozstrategy.webapp.command.JsonReaderResponse;
import com.ozstrategy.webapp.command.appstore.AppStoreCommand;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lihao on 1/6/15.
 */
@Controller
@RequestMapping("appStoreController.do")
public class AppStoreController extends BaseController {
    private static final String fileUrl="app/download/{0}";
    @Autowired
    private AppStoreManager appStoreManager;
    @RequestMapping(params = "method=listAppStores")
    @ResponseBody
    public JsonReaderResponse<AppStoreCommand> listAppStores(HttpServletRequest request) throws Exception{
        List<AppStoreCommand> commands=new ArrayList<AppStoreCommand>();
        Integer start=parseInteger(request.getParameter("start"));
        Integer limit=parseInteger(request.getParameter("limit"));
        Map<String,Object> map=requestMap(request);
        List<AppStore> projects=appStoreManager.listAppStores(map, start, limit);
        if(projects!=null && projects.size()>0){
            for(AppStore project : projects){
                commands.add(new AppStoreCommand(project));
            }
        }
        int count = appStoreManager.listAppStoresCount(map);
        return new JsonReaderResponse<AppStoreCommand>(commands,"",count);
    }
    @RequestMapping(params = "method=save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response){
        String id=request.getParameter("id");
        String version = request.getParameter("version");
        String description = request.getParameter("description");
        String platform=request.getParameter("platform");
        response.setContentType("text/html;charset=utf-8");
        String attachFilesDirStr = Constants.imDataDir + "/"+Constants.appFileDir+"/";
        String host=request.getServerName();
        String contextPath=request.getContextPath();

        attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
        File fileDir = new File(attachFilesDirStr);
        if (fileDir.exists() == false) {
            fileDir.mkdir();
        }
        PrintWriter writer=null;
        try {
            writer=response.getWriter();
        } catch (IOException e) {
            
        }
        String path=null;
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Iterator list = multipartRequest.getFileNames();
            while (list.hasNext()) {
                String controlName = list.next().toString();
                MultipartFile file = multipartRequest.getFile(controlName);
                CommonsMultipartFile cmf = (CommonsMultipartFile) file;
                DiskFileItem fileItem = (DiskFileItem) cmf.getFileItem();
                String str = UUID.randomUUID().toString();
                String fileName = fileItem.getName();
                String ext = FilenameUtils.getExtension(fileName);
                attachFilesDirStr=attachFilesDirStr+"/"+str+"."+ext;
                attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
                File fileOnServer = new File(attachFilesDirStr);
                if (fileOnServer.exists()) {
                    str = UUID.randomUUID().toString();
                    attachFilesDirStr=attachFilesDirStr+"/"+str+"."+ext;
                    attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
                    fileOnServer = new File(attachFilesDirStr);
                }
                fileItem.write(fileOnServer);
                if(contextPath.indexOf("/")!=-1){
                    contextPath=contextPath.substring(0,contextPath.length());
                }
                AppStore appStore=null;
                if(StringUtils.isNotEmpty(id)){
                    appStore=appStoreManager.getAppStoreById(parseLong(id));
                    path=appStore.getFilePath();
                    File source=new File(path);
                    source.delete();
                }else{
                    appStore=new AppStore();
                    appStore.setCreateDate(new Date());
                }
                String httpPath="http://"+host+contextPath+"/"+ fileUrl;
                if(appStore.getId()!=null){
                    httpPath= MessageFormat.format(httpPath,appStore.getId());
                }
                appStore.setDescription(description);
                appStore.setFilePath(fileOnServer.getAbsolutePath());
                appStore.setLastUpdateDate(new Date());
                appStore.setPlatform(Platform.valueOf(platform));
                appStore.setVersion(version);
                appStore.setUrl(httpPath);
                appStoreManager.save(appStore);
                writer.print("{success:true,msg:'上传成功!'}");
            }
        } catch (Exception e) {
            logger.error("upload:", e);
            if(StringUtils.isNotEmpty(path)){
                File file=new File(path);
                file.delete();
            }
            writer.print("{success:false,msg:'上传出错!'}");
            e.printStackTrace();
        }
        writer.close();
        return null;
    }
    @RequestMapping(params = "method=download")
    public void download(HttpServletRequest request,HttpServletResponse response){
        OutputStream outputStream=null;
        try{
            String id=request.getParameter("id");
            if(StringUtils.isNotEmpty(id)){
                AppStore appStore=appStoreManager.getAppStoreById(parseLong(id));
                if(appStore!=null){
                    String filePath=appStore.getFilePath();
                    File file=new File(filePath);
                    FileInputStream fileInputStream = new FileInputStream(file);
                    int             len;
                    byte[]          readBuffer      = new byte[2048];
                    String finalExportZipFileName=filePath.substring(filePath.lastIndexOf("/"));
                    response.setCharacterEncoding("UTF-8");
                    response.addHeader("Content-Disposition", "Attachment;FileName=" + finalExportZipFileName);
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
    
    @RequestMapping(params = "method=delete")
    @ResponseBody
    public BaseResultCommand delete(HttpServletRequest request){
        String ids=request.getParameter("id");
        Long id=parseLong(ids);
        try{
            AppStore appStore=appStoreManager.getAppStoreById(id);
            if(appStore!=null){
                String path=appStore.getFilePath();
                appStoreManager.delete(id);
                File file=new File(path);
                boolean del = file.delete();
                if(del){
                    return new BaseResultCommand("",true);
                }
            }
        }catch (Exception e){
            logger.error("delete file fail",e);
        }
        return new BaseResultCommand(getMessage("globalRes.removeFail",request),false);
    }
}
