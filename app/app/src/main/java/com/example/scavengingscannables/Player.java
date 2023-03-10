package com.example.scavengingscannables;

import java.util.ArrayList;

public class Player {
    private String username;
    private ArrayList<String> scannedQRCodesID = new ArrayList<>();
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String email;

    public Player(String username, String firstName, String lastName, Long phoneNumber, String email) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Player(){}

    public void AddQRCodeByID(String id){
        this.scannedQRCodesID.add(id);
    }

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

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
