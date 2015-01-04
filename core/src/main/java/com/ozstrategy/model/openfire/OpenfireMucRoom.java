package com.ozstrategy.model.openfire;

import com.ozstrategy.model.project.Project;
import com.ozstrategy.util.OpenfireUtils;

import java.io.Serializable;

/**
 * Created by lihao on 12/31/14.
 */
public class OpenfireMucRoom implements Serializable {
    private Long serviceID;
    private Long roomID;
    private String creationDate;
    private String modificationDate;
    private String name;
    private String naturalName;
    private  String description;
    private String lockedDate;
    private String emptyDate;
    private Short canChangeSubject;
    private Integer maxUsers;
    private Short publicRoom;
    private Short moderated;
    private Short membersOnly;
    private Short canInvite;
    private String roomPassword;
    private Short canDiscoverJID;
    private Short logEnabled;
    private String subject;
    private Short rolesToBroadcast;
    private Short useReservedNick;
    private Short canChangeNick;
    private Short canRegister;
    public OpenfireMucRoom copy(Project project){
        this.serviceID=1L;
        this.roomID=project.getId();
        this.creationDate= OpenfireUtils.dateToMillis(project.getCreateDate());
        this.modificationDate= OpenfireUtils.dateToMillis(project.getLastUpdateDate());
        this.name=project.getName();
        this.naturalName=project.getName();
        this.subject=project.getName();
        this.maxUsers=1000;
        this.description="";
        this.lockedDate="000000000000000";
        this.emptyDate= OpenfireUtils.dateToMillis(project.getCreateDate());
        this.canChangeSubject=1;
        this.publicRoom=1;
        this.moderated=0;
        this.membersOnly=0;
        this.canInvite=1;
        this.canDiscoverJID=0;
        this.logEnabled=0;
        this.rolesToBroadcast=7;
        this.useReservedNick=0;
        this.canRegister=1;
        this.canChangeNick=1;
        return this;
    }

    public Long getServiceID() {
        return serviceID;
    }

    public void setServiceID(Long serviceID) {
        this.serviceID = serviceID;
    }

    public Long getRoomID() {
        return roomID;
    }

    public void setRoomID(Long roomID) {
        this.roomID = roomID;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNaturalName() {
        return naturalName;
    }

    public void setNaturalName(String naturalName) {
        this.naturalName = naturalName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(String lockedDate) {
        this.lockedDate = lockedDate;
    }

    public String getEmptyDate() {
        return emptyDate;
    }

    public void setEmptyDate(String emptyDate) {
        this.emptyDate = emptyDate;
    }

    public Short getCanChangeSubject() {
        return canChangeSubject;
    }

    public void setCanChangeSubject(Short canChangeSubject) {
        this.canChangeSubject = canChangeSubject;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Short getPublicRoom() {
        return publicRoom;
    }

    public void setPublicRoom(Short publicRoom) {
        this.publicRoom = publicRoom;
    }

    public Short getModerated() {
        return moderated;
    }

    public void setModerated(Short moderated) {
        this.moderated = moderated;
    }

    public Short getMembersOnly() {
        return membersOnly;
    }

    public void setMembersOnly(Short membersOnly) {
        this.membersOnly = membersOnly;
    }

    public Short getCanInvite() {
        return canInvite;
    }

    public void setCanInvite(Short canInvite) {
        this.canInvite = canInvite;
    }

    public String getRoomPassword() {
        return roomPassword;
    }

    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

    public Short getCanDiscoverJID() {
        return canDiscoverJID;
    }

    public void setCanDiscoverJID(Short canDiscoverJID) {
        this.canDiscoverJID = canDiscoverJID;
    }

    public Short getLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(Short logEnabled) {
        this.logEnabled = logEnabled;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Short getRolesToBroadcast() {
        return rolesToBroadcast;
    }

    public void setRolesToBroadcast(Short rolesToBroadcast) {
        this.rolesToBroadcast = rolesToBroadcast;
    }

    public Short getUseReservedNick() {
        return useReservedNick;
    }

    public void setUseReservedNick(Short useReservedNick) {
        this.useReservedNick = useReservedNick;
    }

    public Short getCanChangeNick() {
        return canChangeNick;
    }

    public void setCanChangeNick(Short canChangeNick) {
        this.canChangeNick = canChangeNick;
    }

    public Short getCanRegister() {
        return canRegister;
    }

    public void setCanRegister(Short canRegister) {
        this.canRegister = canRegister;
    }
}
