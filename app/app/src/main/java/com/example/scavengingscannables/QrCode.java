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
    public QrCode(int QrId, String NameText, String Score, HashMap<String,String> Comments, ArrayList<String> OwnedBy,ArrayList<Double> qrLocation) {
        qrId = QrId;
        score = Score;
        nameText = NameText;
        comments = Comments;
        ownedBy = OwnedBy;
        location = qrLocation;
    }

    public QrCode(){}

    public int getQrId() {
        return qrId;
    }
    public String getScore() {
        return score;
    }
    public String getQrName() {
        return nameText;
    }

    public HashMap<String,String> getComments(){return comments;}
    public ArrayList<String> getOwnedBy(){return ownedBy;}

    public ArrayList<Double> getLocation() {
        return location;
    }

    public String getNameText() {
        return nameText;
    }

    //public String getComment() {return comment;}

    //public String getOthers() {return others;}
}