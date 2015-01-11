package com.ozstrategy.model;

import java.io.Serializable;
import java.util.Date;


public abstract class BaseObject extends AbstractBaseObject implements Serializable {
    private static final long serialVersionUID = -5137348947151932448L;

    protected Date createDate;

    protected Date lastUpdateDate;

    public BaseObject() {
        this.createDate = new Date();
        this.lastUpdateDate = createDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final BaseObject other = (BaseObject) obj;

        if (this.createDate == null) {
            if (other.getCreateDate() != null) {
                return false;
            }
        } else if (!this.createDate.equals(other.getCreateDate())) {
            return false;
        }

        return true;
    } // end method equals

    public Date getCreateDate() {
        return createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 31;
        result = (PRIME * result)
                + ((this.createDate == null) ? 0 : this.createDate.hashCode());

        return result;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

} 
