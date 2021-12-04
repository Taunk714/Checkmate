package com.teamred.checkmate.data;

import java.util.Date;

public class SavedPost {
    private String postId;
    private Date lastReview;
    private Boolean isAuthor;
    private Boolean isStarred;
    private Boolean isLiked;

    public SavedPost(){}

    public SavedPost(String postId, Date lastReview, Boolean isAuthor, Boolean isStarred, Boolean isLiked) {
        this.postId = postId;
        this.lastReview = lastReview;
        this.isAuthor = isAuthor;
        this.isStarred = isStarred;
        this.isLiked = isLiked;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public Date getLastReview() {
        return lastReview;
    }

    public void setLastReview(Date lastReview) {
        this.lastReview = lastReview;
    }

    public Boolean getAuthor() {
        return isAuthor;
    }

    public void setAuthor(Boolean author) {
        isAuthor = author;
    }

    public Boolean getStarred() {
        return isStarred;
    }

    public void setStarred(Boolean starred) {
        isStarred = starred;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }
}
