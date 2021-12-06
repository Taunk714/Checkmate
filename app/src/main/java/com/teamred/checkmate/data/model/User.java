package com.teamred.checkmate.data.model;

import com.google.firebase.firestore.DocumentId;
import com.teamred.checkmate.data.SavedPost;

public class User {

    private String uid;

    private String name;
    private String username;
    private String photoUrl;
    private String[] groupJoined;
    private SavedPost[] savedPosts;
    private String email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String[] getGroupJoined() {
        return groupJoined;
    }

    public void setGroupJoined(String[] groupJoined) {
        this.groupJoined = groupJoined;
    }

    public SavedPost[] getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(SavedPost[] savedPosts) {
        this.savedPosts = savedPosts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
