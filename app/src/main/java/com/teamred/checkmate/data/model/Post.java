package com.teamred.checkmate.data.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Post {
    private String title;
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
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.author = author;
        this.createDate = new Date();
        this.subtopic = subtopic;
        this.onenoteAppURL = onenoteWebURL;
        this.onenoteWebURL = onenoteWebURL;
    }

    public Post() {
        this.createDate = new Date();
//        this.number = new Random().nextInt(20);
    }

    public String getOnenoteAppURL() {
        return onenoteAppURL;
    }

    public String getOnenoteWebURL() {
        return onenoteWebURL;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public String getPostID() { return postID; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getsubtopic() {
        return subtopic;
    }

    //    public Integer getNumber() {
//        return number;
//    }

//    public void setNumber(Integer number) {
//        this.number = number;
//    }
}
