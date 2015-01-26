package com.ozstrategy.model.openfire;

import com.ozstrategy.model.userrole.User;
import com.ozstrategy.util.OpenfireUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Created by lihao on 12/17/14.
 */
public class OpenfireUser implements Serializable{
    private String username;
    private String plainPassword;
    private String encryptedPassword;
    private String name;
    private String email;
    private String creationDate;
    private String modificationDate;
    public OpenfireUser(){}
    public OpenfireUser copy(User user){
        this.username=user.getUsername()+"_"+user.getId();
        this.encryptedPassword=user.getPassword();
        this.name=user.getNickName();
        this.email=user.getEmail();
        this.creationDate= OpenfireUtils.dateToMillis(user.getCreateDate());
        this.modificationDate=OpenfireUtils.dateToMillis(user.getLastUpdateDate());
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OpenfireUser user = (OpenfireUser) o;
        return new EqualsBuilder()
                .append(username, user.username)
                .append(email,user.email)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(username)
                .append(email)
                .hashCode();

    }
}
