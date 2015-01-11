package com.ozstrategy.model.system;

import com.ozstrategy.model.BaseObject;

/**
 * Created by lihao on 1/8/15.
 */
public class JobLog extends BaseObject {
    private Long id;
    private String job;
    private Boolean success;
    private String exception;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
