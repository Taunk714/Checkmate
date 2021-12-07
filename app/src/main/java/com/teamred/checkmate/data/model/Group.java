package com.teamred.checkmate.data.model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.teamred.checkmate.data.AlgoliaDataSource;
import com.teamred.checkmate.data.FireStoreDataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group {
    private String objectID;
    private String groupName;
    private String description;
    private Date createDate;
    private Date updateDate;
    private List<String> subTopics = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String creator; // creatorUsername on Shirene's
    private String creatorId;
    private Integer status = 0;
    private Integer numMember = 1;
    private Integer numView = 1;


    public Group() {
    }

    /**
     * @param groupName       groupname
     * @param tags            tags array
     * @param creatorUsername creator's username (firebase)
     * @param description     description of group
     */
    public Group(String groupDocumentID, String groupName, ArrayList<String> tags, String creatorUsername, String description) {
        this.objectID = groupDocumentID;
        this.groupName = groupName;
        this.creator = creatorUsername;
        this.description = description;
        this.createDate = new Date();
        this.updateDate = new Date();
        this.tags = tags;
        this.status = 0;
    }

    public static boolean isJoined(String groupId, List<String> joined) {
        for (String s : joined) {
            if (s.equals(groupId)) {
                return true;
            }
        }
        return false;
    }

    public static void removeGroup(User user, String groupId) {
        user.leaveGroup(groupId);
    }

    public static void joinGroup(User user, String groupId) {
        user.joinGroup(groupId);
    }

    public static void updateView(Group group) {
        FireStoreDataSource.updateGroupView(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AlgoliaDataSource.getInstance().updateGroup(group.objectID, "numView", group.numView.toString());
            }
        });
    }

    public static void updateMember(Group group) {
        FireStoreDataSource.updateGroupMember(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                AlgoliaDataSource.getInstance().updateGroup(group.objectID, "numMember", group.numMember.toString());
            }
        });

    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
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

    public List<String> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(List<String> subTopics) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
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

    public Integer getNumView() {
        return numView;
    }

    public void setNumView(Integer numView) {
        this.numView = numView;
    }

    public String getCreatorId() {
        return creatorId;
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
