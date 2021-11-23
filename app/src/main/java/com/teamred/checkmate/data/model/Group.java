package com.teamred.checkmate.data.model;

import java.util.Date;

public class Group {
    private String groupName;
    private String[] tags;
    private String description;
    private Date createDate;
    private Date updateDate;
    private Integer[] subTopics;
    private String creator;
    private Integer status = 0;
    private Integer memberNum = 1;

    public Group() {}

    public Group(String groupName, String[] tags, String description) {
        this.groupName = groupName;
        this.tags = tags;
        this.description = description;
        this.createDate = new Date();
        this.updateDate = new Date();
        this.status = 0;
    }

    public Group(String groupName, String[] tags, String description, Date createDate, Date updateDate) {
        this.groupName = groupName;
        this.tags = tags;
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

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
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

    public Integer[] getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(Integer[] subTopics) {
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

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }
}
