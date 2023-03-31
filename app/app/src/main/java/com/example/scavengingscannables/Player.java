package com.example.scavengingscannables;

import java.util.ArrayList;

/**
 * Stores all the information about a player
 */
public class Player {
    private String username;
    private final ArrayList<String> scannedQRCodesID = new ArrayList<>();
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;
    private Boolean hide;
    private String highest;
    private String lowest;
    private String total;

    public Player(String username, String firstName, String lastName, Long phoneNumber, String email, Boolean hide, String highest, String lowest,String total) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hide = hide;
        this.highest = highest;
        this.lowest = lowest;
        this.total = total;
    }

    // to allow firestore to save custom classes
    public Player(){}

    /**
     * Adds a QrCodeID to the player
     * @param id id to add to the player
     */
    public void AddQRCodeByID(String id){
        this.scannedQRCodesID.add(id);

    }

    /**
     * Removes a QrCodeID from the player
     * @param id id to be removed
     */
    public void RemoveQRCodeByID(String id){
        this.scannedQRCodesID.remove(id);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getScannedQRCodesID() {
        return scannedQRCodesID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getHide(){return hide;}

    public void setHide(Boolean b){this.hide = b;}
    public void setHighest(String hid){this.highest = hid;};
    public String getHighest(){return highest;};
    public void setLowest(String lid){this.lowest = lid;};
    public String getLowest(){return lowest;};
    public void setTotal(String tota){this.total = tota;};
    public String getTotal(){return total;};

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
