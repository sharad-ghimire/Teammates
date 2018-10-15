package com.sharad.teammates.models;

public class Chat {
    private String message;
//    private String createdByUser;
    private Boolean whichUser;
    public Chat(){

    }

    public Chat(String message, Boolean whichUser) {
        this.message = message;
        this.whichUser = whichUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getWhichUser() {
        return whichUser;
    }

    public void setWhichUser(Boolean whichUser) {
        this.whichUser = whichUser;
    }
}
