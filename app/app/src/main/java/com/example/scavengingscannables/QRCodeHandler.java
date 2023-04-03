package com.example.scavengingscannables;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class handles QR code operations asynchronously
 */
public class QRCodeHandler implements FirestoreDatabaseCallback {
    private final FirestoreDatabaseController fdc;

    private final NamingSystem namsys = new NamingSystem();

    private final Activity activity;

    private final Bitmap image;

    private final String hash;

    private final int score;

    private boolean storePhoto = false;

    private boolean storeLocation = false;

    private final String username;

    private PlayerHandler ph;

    private final HashMap<String, Double> locationMap;

    public QRCodeHandler(Activity activity, String hash, int score, FirestoreDatabaseController fdc, HashMap<String, Double> locationMap, Bitmap image, String username) {
       this.activity = activity;
       this.hash = hash;
       this.score = score;
       this.fdc = fdc;
       this.image = image;
       this.locationMap = locationMap;
       this.username = username;
    }

    // When the existing QR code is retrieved or the player is retrieved
    @Override
    public <T> void OnDataCallback(T data) {
        editExistingQRCode((QRCode) data);

        // Add QR code id to user's list of codes
    }

    // If the QR code exists
    @Override
    public void OnDocumentExists() {
        fdc.getQRCodeByID(hash, this);
    }

    // If the QR code doesn't exist
    @Override
    public void OnDocumentDoesNotExist() {
        createNewQRCode();

        // Add QR code id to user's list of codes
    }

    /**
     * Creates a new QR code object using all the information about the real life QR code the user has scanned
     */
    private void createNewQRCode() {
        // Ask the user if they want to store an image of the object they just scanned
        // Then, whether the user wants to store an image or not, we ask if they want to store the location of the object they just scanned

        // Generate a name for the hash
        String hashedName = namsys.generateName(hash);

        // Create HashMap for comments, ArrayList for owners, and an ArrayList for locations
        // Add current user to list of owners
        HashMap<String, String> comments = new HashMap<String, String>();
        ArrayList<String> ownedBy = new ArrayList<>();
        ownedBy.add(username);

        // Store location
        GeoPoint qrLocation = new GeoPoint(locationMap.get("latitude"), locationMap.get("longitude"));

        // If there is no image, we'll set storePhoto to false
        if (image == null) {
            storePhoto = true;
        }

        //  If the user decided not to store a location, we'll set storeLocation to false
        if (locationMap.get("latitude") == 0 && locationMap.get("longitude") == 0) {
            storeLocation = true;
        }

        QRCodeImageLocationInfo qrCodeImageLocationInfo = new QRCodeImageLocationInfo(image, qrLocation, storePhoto, storeLocation);
        ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfoList = new ArrayList<>();
        qrCodeImageLocationInfoList.add(qrCodeImageLocationInfo);

        // Create a QR code using the data we've generated
        QRCode newCode = new QRCode(hash, Integer.toString(score), hashedName,  comments, ownedBy, qrCodeImageLocationInfoList);

        // Save the new QR code to the database
        fdc.saveQRCodeByID(newCode);
    }

    /**
     * Edits an existing QR code object by adding the current user to the QR code's list of players who have scanned it
     * @param qrcode the QR code to edit
     */
    private void editExistingQRCode(QRCode qrcode) {
        // If there is no image, we'll set storePhoto to false
        if (image == null) {
            storePhoto = true;
        }

        //  If the user decided not to store a location, we'll set storeLocation to false
        if (locationMap.get("latitude") == 0 && locationMap.get("longitude") == 0) {
            storeLocation = true;
        }

        GeoPoint qrLocation = new GeoPoint(locationMap.get("latitude"), locationMap.get("longitude"));

        QRCodeImageLocationInfo qrCodeImageLocationInfo = new QRCodeImageLocationInfo(image, qrLocation, storePhoto, storeLocation);
        qrcode.AddQRCodeImageLocationInfo(qrCodeImageLocationInfo);

        // Get QR code using its id
        // Add the current user to its ownedBy list
        ArrayList<String> ownedBy = qrcode.getOwnedBy();
        if (!ownedBy.contains(username)) {
            qrcode.getOwnedBy().add(username);
            fdc.saveQRCodeByID(qrcode);
        }
    }

}
