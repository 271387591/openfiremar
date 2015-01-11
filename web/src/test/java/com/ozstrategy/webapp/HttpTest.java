package com.ozstrategy.webapp;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by lihao on 12/30/14.
 */
public class HttpTest {
    
    public static void main(String[] a){
        org.quartz.Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        

    }
}
