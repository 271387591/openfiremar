package com.ozstrategy.job;

import com.ozstrategy.service.export.MessageExportManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lihao on 1/20/15.
 */
public class ExportMessageJob {

    @Autowired
    private MessageExportManager messageExportManager;
    private Log log= LogFactory.getLog(getClass());
    public void exportMessage(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        Date sDate=calendar.getTime();
        Date eDate=new Date();
        
        
    }
    public static void main(String[] a){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH,-1);
        System.out.println("上个月是："+new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(calendar.getTime()));
        File file=new File("/Users/lihao/Downloads/openf/export");
        if(!file.exists()) {
            boolean mk = file.mkdirs();
            System.out.println(mk);
        }
    }
}
