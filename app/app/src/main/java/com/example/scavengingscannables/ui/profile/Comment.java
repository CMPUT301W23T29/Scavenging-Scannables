package com.example.scavengingscannables.ui.profile;

public class Comment {
    private String name;
    private String comment;


    public Comment(String Name, String Comment) {
        this.name = Name;
        this.comment = Comment;
    }
    public String getName() {
        return name;
    }
    public String getComment() {
        return comment;
    }

    public void setName(String username) {
        this.name = username;
    }
    public void setComment(String userComment) {
        this.comment = userComment;
    }
}