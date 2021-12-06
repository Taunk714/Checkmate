package com.teamred.checkmate.data.model;

import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.FireStoreDataSource;

import java.util.ArrayList;
import java.util.Date;

public class Group {
    private String objectID;
    private String groupDocumentID;
    private String groupName;
    private String description;
    private Date createDate;
    private Date updateDate;
    private String[] subTopics = new String[]{};
    private String[] tags;
    private String creator; // creatorUsername on Shirene's
    private String creatorId;
    private Integer status = 0;
    private Integer numMember = 1;
    private Integer numView = 1;


    public Group() {
    }

    /**
     * @param groupDocumentID docrefid
     * @param groupName       groupname
     * @param tags            tags array
     * @param creatorUsername creator's username (firebase)
     * @param description     description of group
     */
    public Group(String groupDocumentID, String groupName, String[] tags, String creatorUsername, String description) {
        this.groupDocumentID = groupDocumentID;
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

    public static boolean isJoined(String groupId, String[] joined) {
        for (String s : joined) {
            if (s.equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    public static void removeGroup(User user, String groupId) {
        ArrayList<String> groups = new ArrayList<>();
        for (int i = 0; i < user.getGroupJoined().length; i++) {
            if (groupId.equals(user.getGroupJoined()[i])) {
                continue;
            }
            groups.add(user.getGroupJoined()[i]);
        }
        user.setGroupJoined(groups.toArray(new String[0]));
    }

    public static void joinGroup(User user, String groupId) {
        String[] group = new String[user.getGroupJoined().length + 1];
        System.arraycopy(user.getGroupJoined(), 0, group, 0, user.getGroupJoined().length);
        group[group.length - 1] = groupId;
        user.setGroupJoined(group);
    }

    public static void update(Group group) {
        FireStoreDataSource.updateGroup(group);
        AlgoliaDataSource.getInstance().updateGroup(group);
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getTags() {
        return tags;
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void addView() {
        this.numView++;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public int addMember() {
        numMember++;
        return numMember;
    }

    public int removeMember() {
        numMember--;
        return numMember;
    }
}
