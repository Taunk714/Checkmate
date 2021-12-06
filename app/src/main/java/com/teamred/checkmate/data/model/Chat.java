package com.teamred.checkmate.data.model;

import java.security.PublicKey;
import java.util.Date;

public class Chat {
    private String currentUser;
    private String otherUser;
    String lastMessage;
    Date time = new Date();
//    String lastType;
    int unread = 0;

    public Chat(){}

    public Chat(String currentUser, String otherUser, String lastMessage) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.lastMessage = lastMessage;
//        this.lastType = lastType;
    }


    public Chat(String currentUser, String otherUser, String lastMessage,int unread) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.lastMessage = lastMessage;
//        this.lastType = lastType;
        this.unread = unread;
    }

    public Chat(String currentUser, String otherUser, String lastMessage, Date time, int unread) {
        this.currentUser = currentUser;
        this.otherUser = otherUser;
        this.lastMessage = lastMessage;
        this.time = time;
//        this.lastType = lastType;
        this.unread = unread;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

//    public String getLastType() {
//        return lastType;
//    }
//
//    public void setLastType(String lastType) {
//        this.lastType = lastType;
//    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
}
