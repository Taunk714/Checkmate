package com.teamred.checkmate.data.model;

import java.util.Arrays;
import java.util.Date;

public class Note {
    private String title;
    private String[] tags;
    private String content;
    private Date createDate;
    private String author;

    public Note(String title, String[] tags, String content) {
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.createDate = new Date();
    }

    public Note() {
        this.createDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
