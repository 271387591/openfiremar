package com.ozstrategy.model.export;

import com.ozstrategy.model.BaseEntity;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lihao on 1/11/15.
 */
@Table(name = "ext_messageExport")
public class MessageExport extends BaseEntity {
    @Column
    private String exportor;
    @Column
    private ExportType type;
    @Column
    private String filePath;
    @Column
    private Date executeDate;
    @Column
    private String exceptions;
    @Column
    private Long projectId;

    public String getExportor() {
        return exportor;
    }

    public void setExportor(String exportor) {
        this.exportor = exportor;
    }

    public ExportType getType() {
        return type;
    }

    public void setType(ExportType type) {
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

    public String getExceptions() {
        return exceptions;
    }

    public void setExceptions(String exceptions) {
        this.exceptions = exceptions;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MessageExport that = (MessageExport) o;

        return new EqualsBuilder()
                .append(id,that.id)
                .append(filePath,that.filePath)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(filePath)
                .hashCode();
    }
}
