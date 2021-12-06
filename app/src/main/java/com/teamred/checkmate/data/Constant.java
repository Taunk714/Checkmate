package com.teamred.checkmate.data;

import com.teamred.checkmate.data.model.User;

public class Constant {

    private static Constant instance = new Constant();

    private User currentUser;

    private Constant(){

    }

    public static Constant getInstance() {
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
