package com.ozstrategy.oz;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Created by lihao on 1/13/15.
 */
@Table(name = "user")
public class User extends BaseEntity {
    @Column(name = "name")
    private String name;
    @Column(name = "sex")
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
