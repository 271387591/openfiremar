package com.ozstrategy.webapp.controller.openfire;

import com.ozstrategy.webapp.controller.BaseController;
import com.ozstrategy.webapp.security.AppSessionManager;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by lihao on 12/28/14.
 */
@Controller
@RequestMapping("openfireController.do")
public class OpenfireController extends BaseController {
    private static final String fileDir="pictures";
    @RequestMapping(params = "method=upload")
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String sessionId = request.getParameter("sessionId");
        response.setContentType("text/html;charset=utf-8");
        if(!StringUtils.equals(AppSessionManager.get(username),sessionId)){
            try {
                response.getWriter().print("{success:false,msg:'上传出错!请先登录。'}");
            } catch (IOException e) {
            }
            return null;
        }

        String attachFilesDirStr = request.getRealPath("/") + "/"+fileDir+"/";
        String host=request.getRemoteHost();
        String serverName=request.getServerName();
        String uri = request.getRequestURI();
        String addr=request.getRemoteAddr();
        String path=request.getServletPath();
        String contextPath=request.getContextPath();
        String url=request.getRequestURL().toString();
        attachFilesDirStr= FilenameUtils.normalize(attachFilesDirStr);
        File fileDir = new File(attachFilesDirStr);
        if (fileDir.exists() == false) {
            fileDir.mkdir();
        }
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
                String httpPath="http://"+host+contextPath+"/"+fileDir+"/"+str+"."+ext;
                
                response.getWriter().print("{success:true,msg:'上传成功!',path:"+httpPath+"}");
            }
        } catch (Exception e) {
            logger.error("upload:", e);
            try {
                response.getWriter().print("{success:false,msg:'上传出错!'}");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return null;
    }
}
