package com.example.scavengingscannables;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private String userId;
    private String userEmail;
    private String userName;
    private String userPhoneNumber;
    private ArrayList<Integer> owned;



    // create constructor to set the values for all the parameters of the each single view
    public User(String UserId, String UserEmail, String UserName, String UserPhoneNumber, ArrayList<Integer> Owned) {
        userId = UserId;
        userEmail = UserEmail;
        userName = UserName;
        userPhoneNumber = UserPhoneNumber;
        owned = Owned;
    }

    public String getUserId() {
        return userId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserName() {
        return userName;
    }
    public ArrayList<Integer> getOwned(){return owned;}
}