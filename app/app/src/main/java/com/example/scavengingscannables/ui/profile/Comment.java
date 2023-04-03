package com.example.scavengingscannables.ui.profile;

/**
 * Class which holds information about each comment
 */
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

}