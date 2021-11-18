package com.teamred.checkmate.data.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Note {
    private String title;
    private String[] tags;
    private String content;
    private Date createDate;
    private String author;
    private Integer number;

    public Note(String title, String[] tags, String content) {
        this.title = title;
        this.tags = tags;
        this.content = content;
        this.createDate = new Date();
        this.number = new Random().nextInt(20);
    }

    public Note() {
        this.createDate = new Date();
        this.number = new Random().nextInt(20);
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
