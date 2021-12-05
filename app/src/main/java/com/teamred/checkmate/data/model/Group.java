package com.teamred.checkmate.data.model;

import java.util.Date;

public class Group {
    private String groupDocumentID;
    private String groupName;
    private String[] tags;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String[] subTopics = new String[]{};
    private String creatorUsername;
    private Integer status = 0;
    private Integer memberNum = 1;

    public Group(){}
    /**
     *
     * @param groupDocumentID docrefid
     * @param groupName groupname
     * @param tags tags array
     * @param creatorUsername creator's username (firebase)
     * @param description description of group
     */
    public Group(String groupDocumentID, String groupName, String[] tags, String creatorUsername, String description) {
        this.groupDocumentID = groupDocumentID;
        this.groupName = groupName;
        this.tags = tags;
        this.creatorUsername = creatorUsername;
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

    public String getGroupDocumentID() { return groupDocumentID; }

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

    public String[] getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(String[] subTopics) {
        this.subTopics = subTopics;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
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
