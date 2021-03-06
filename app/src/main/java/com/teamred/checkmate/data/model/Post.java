package com.teamred.checkmate.data.model;

import java.util.Date;
import java.util.List;

public class Post {
    private String postTitle;
    private List<String> tags;
    private String content;
    private Date createDate;
    private String author;
    private String postID;
    private String subtopic;
    private String onenoteAppURL;
    private String onenoteWebURL;
//    private Integer number;

    public Post(String postID, String title, String subtopic, List<String> tags, String author, String content, String onenoteWebURL, String onenoteAppURL) {
        this.postID = postID;
        this.postTitle = title;
        this.tags = tags;
        this.content = content;
        this.author = author; // Author will be the USERNAME, not name, not email, USERNAME.
        this.createDate = new Date();
        this.subtopic = subtopic;
        this.onenoteAppURL = onenoteWebURL;
        this.onenoteWebURL = onenoteWebURL;
    }

    public Post() {
        this.createDate = new Date();
//        this.number = new Random().nextInt(20);
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public void setSubtopic(String subtopic) {
        this.subtopic = subtopic;
    }

    public String getOnenoteAppURL() {
        return onenoteAppURL;
    }

    public void setOnenoteAppURL(String onenoteAppURL) {
        this.onenoteAppURL = onenoteAppURL;
    }

    public String getOnenoteWebURL() {
        return onenoteWebURL;
    }

    public void setOnenoteWebURL(String onenoteWebURL) {
        this.onenoteWebURL = onenoteWebURL;
    }
}
