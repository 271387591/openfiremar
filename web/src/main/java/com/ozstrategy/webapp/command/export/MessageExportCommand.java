package com.ozstrategy.webapp.command.export;

import com.ozstrategy.model.export.MessageExport;
import com.ozstrategy.webapp.command.BaseEntityCommand;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * Created by lihao on 1/11/15.
 */
public class MessageExportCommand extends BaseEntityCommand {
    private Long id;
    private String exportor;
    private String type;
    private String filePath;
    private Date executeDate;
    private Boolean hasFile;
    protected Date createDate;
    protected Date lastUpdateDate;
    private Boolean multiFile=Boolean.FALSE;
    private Date startTime;
    private Date endTime;
    public MessageExportCommand(MessageExport export){
        this.id= export.getId();
        this.exportor= export.getExportor();
        this.type=export.getType()!=null?export.getType().name():null;
        this.executeDate= export.getExecuteDate();
        this.hasFile= StringUtils.isNotEmpty(export.getFilePath());
        this.createDate=export.getCreateDate();
        this.lastUpdateDate=export.getLastUpdateDate();
        this.multiFile=export.getMultiFile();
        this.startTime=export.getStartTime();
        this.endTime=export.getEndTime();
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExportor() {
        return exportor;
    }

    public void setExportor(String exportor) {
        this.exportor = exportor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public Boolean getHasFile() {
        return hasFile;
    }

    public void setHasFile(Boolean hasFile) {
        this.hasFile = hasFile;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Boolean getMultiFile() {
        return multiFile;
    }

    public void setMultiFile(Boolean multiFile) {
        this.multiFile = multiFile;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
