package com.example.scavengingscannables;

import android.location.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class QrCode {

    private String qrId;

    private String score;
    private String nameText;
    private HashMap<String,String> comments;
    private ArrayList<String> ownedBy;

    private String visualLink;


    //Currently set location to [x,y]
    private ArrayList<Double> location;
    //private Location location;

    public QrCode(){}

    public QrCode(String qrId, String score, String nameText, HashMap<String, String> comments, ArrayList<String> ownedBy, ArrayList<Double> location) {
        this.qrId = qrId;
        this.score = score;
        this.nameText = nameText;
        this.comments = comments;
        this.ownedBy = ownedBy;
        this.location = location;
        GenerateVisualLink();
    }

    private void GenerateVisualLink() {
        String hash = this.qrId;
        String seed = hash.substring(0, this.qrId.getBytes().length);
        String linkTemplate = "https://picsum.photos/" + seed + "/seed/200/";
        this.visualLink = linkTemplate;
    }

    public String getQrId() {
        return qrId;
    }

    public String getScore() {
        return score;
    }

    public String getNameText() {
        return nameText;
    }
    public String getVisualLink() {
        return visualLink;
    }

    //public String getComment() {return comment;}
    public HashMap<String, String> getComments() {
        return comments;
    }

    public void setComments(HashMap<String, String> newComments) {
        this.comments = newComments;
    }

    public ArrayList<String> getOwnedBy() {
        return ownedBy;
    }

    public ArrayList<Double> getLocation() {
        return location;
    }
}