package com.ozstrategy.lucene;

import com.ozstrategy.service.openfire.HistoryMessageManager;

/**
 * Created by lihao on 2/8/15.
 */
public class AddIndexInstance {
    private volatile static AddIndexInstance instance=null;
    private HistoryMessageManager historyMessageManager;
    private AddIndexInstance(){}
    public static AddIndexInstance getInstance(){
        if(instance==null){
            synchronized (LuceneInstance.class){
                if(instance==null){
                    instance=new AddIndexInstance();
                }
            }
        }
        return instance;
    }
    synchronized public void addIndex() throws Exception{
        historyMessageManager.addIndex();
    }

    public HistoryMessageManager getHistoryMessageManager() {
        return historyMessageManager;
    }

    public void setHistoryMessageManager(HistoryMessageManager historyMessageManager) {
        this.historyMessageManager = historyMessageManager;
    }
}
