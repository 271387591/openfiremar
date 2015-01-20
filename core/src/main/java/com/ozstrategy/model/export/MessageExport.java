package com.ozstrategy.model.export;

import com.ozstrategy.model.BaseEntity;
import com.ozstrategy.model.BaseObject;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import javax.persistence.Table;
import java.util.Date;

/**
 * Created by lihao on 1/11/15.
 */
@Table(name = "ext_messageExport")
public class MessageExport extends BaseObject {
    private Long id;
    private String exportor;
    private ExportType type;
    private String filePath;
    private Date executeDate;

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
