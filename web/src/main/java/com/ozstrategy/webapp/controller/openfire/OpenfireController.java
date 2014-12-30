package com.ozstrategy.webapp.controller.openfire;

import com.ozstrategy.model.userrole.User;
import com.ozstrategy.service.userrole.UserManager;
import com.ozstrategy.webapp.controller.BaseController;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserManager userManager;
    @RequestMapping(params = "method=upload")
    public ModelAndView upload(HttpServletRequest request, HttpServletResponse response){
        String username = request.getRemoteUser();
        User user = userManager.getUserByUsername(username);
        if(user!=null){
            String attachFilesDirStr = request.getRealPath("/") + "/attachFiles/";
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
                    response.setContentType("text/html;charset=utf-8");
                    response.getWriter().print("{success:true,msg:'上传成功!'}");
                }
            } catch (Exception e) {
                logger.error("upload:",e);
                response.setContentType("text/html;charset=utf-8");
                try {
                    response.getWriter().print("{success:false,msg:'上传出错!'}");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return null;
    }
}
