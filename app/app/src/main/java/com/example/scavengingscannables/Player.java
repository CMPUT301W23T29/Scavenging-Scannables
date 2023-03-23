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

    public Player(String username, String firstName, String lastName, Long phoneNumber, String email, Boolean hide) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.hide = hide;
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

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
