package com.example.scavengingscannables;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores all the information about a scanned qr code
 */
public class QrCode {
    public void setQrId(String qrId) {
        this.qrId = qrId;
    }

    private String qrId;
    private String score;
    private String nameText;
    private HashMap<String,String> comments;
    private ArrayList<String> ownedBy;
    private String visualLink;
    //Currently set location to [x,y]
    private ArrayList<Double> location;

    // to allow firestore to save custom classes
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

    /**
     * Generates the Picsum link to the custom image for the qrcode
     */
    private void GenerateVisualLink() {
        String hash = this.qrId;
        String seed = hash.substring(0, this.qrId.getBytes().length);
        String linkTemplate = "https://picsum.photos/seed/" + seed + "/200/";
        this.visualLink = linkTemplate;
    }

    public String getqrId() {
        return this.qrId;
    }

    public String getScore() {
        return this.score;
    }

    public String getNameText() {
        return this.nameText;
    }
    public String getVisualLink() {
        return this.visualLink;
    }

    public HashMap<String, String> getComments() {
        return this.comments;
    }

    public void setComments(HashMap<String, String> newComments) {
        this.comments = newComments;
    }

    public ArrayList<String> getOwnedBy() {
        return this.ownedBy;
    }

    public ArrayList<Double> getLocation() {
        return this.location;
    }

    /**
     * Adds a usernanme to the owned by section of a qrcode
     * @param username username to be added
     */
    public void AddOwnedBy(String username){
        this.ownedBy.add(username);
    }

    /**
     * Removes a username from the qrcode's owned by
     * @param username username to be removed
     */
    public void RemoveOwnedBy(String username){
        this.ownedBy.remove(username);
    }
}