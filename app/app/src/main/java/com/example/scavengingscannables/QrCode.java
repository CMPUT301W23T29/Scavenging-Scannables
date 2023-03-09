package com.example.scavengingscannables;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class QrCode {

    private int qrId;

    private String score;
    private String nameText;
    private HashMap<String,String> comments;
    private ArrayList<String> ownedBy;

    //Currently set location to [x,y]
    private ArrayList<Double> location;
    //private Location location;

    // create constructor to set the values for all the parameters of the each single view

    public QrCode(){}

    public QrCode(int qrId, String score, String nameText, HashMap<String, String> comments, ArrayList<String> ownedBy, ArrayList<Double> location) {
        this.qrId = qrId;
        this.score = score;
        this.nameText = nameText;
        this.comments = comments;
        this.ownedBy = ownedBy;
        this.location = location;
    }

    public int getQrId() {
        return qrId;
    }

    public String getScore() {
        return score;
    }

    public String getNameText() {
        return nameText;
    }

    public HashMap<String, String> getComments() {
        return comments;
    }

    public ArrayList<String> getOwnedBy() {
        return ownedBy;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }
}