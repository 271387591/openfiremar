package com.ozstrategy.model.system;

import java.io.Serializable;

/**
 * Created by lihao on 1/7/15.
 */
public class ApplicationConfig implements Serializable{
    public static final String index_max_id="index_max_id";
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
