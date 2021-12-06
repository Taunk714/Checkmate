package com.teamred.checkmate.data.model;

import java.util.Date;

public class Group {
    private String id;
    private String groupName;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String[] subTopics = new String[]{};
    private String creator;
    private String creatorId;
    private Integer status = 0;
    private Integer numMember = 1;
    private Integer numView = 1;


    public Group() {}

    public Group(String groupName, String[] tags, String description) {
        this.groupName = groupName;
        this.description = description;
        this.createDate = new Date();
        this.updateDate = new Date();
        this.status = 0;
    }

    public Group(String groupName, String[] tags, String description, Date createDate, Date updateDate) {
        this.groupName = groupName;
        this.description = description;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.status = 0;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String[] getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(String[] subTopics) {
        this.subTopics = subTopics;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNumMember() {
        return numMember;
    }

    public void setNumMember(Integer numMember) {
        this.numMember = numMember;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Integer getNumView() {
        return numView;
    }

    public void setNumView(Integer numView) {
        this.numView = numView;
    }

    public void addView(){
        this.numView++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
