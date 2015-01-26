package com.ozstrategy.webapp.command;

import com.ozstrategy.model.BaseEntity;

import java.util.Date;

/**
 * Created by lihao on 8/8/14.
 */
public class BaseEntityCommand {
    private Date createDate;
    private Date lastUpdateDate;
    public BaseEntityCommand(){
        
    }
    public BaseEntityCommand(BaseEntity baseObject){
        this.createDate=baseObject.getCreateDate();
        this.lastUpdateDate=baseObject.getLastUpdateDate();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
