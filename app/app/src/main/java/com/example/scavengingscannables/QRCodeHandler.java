package com.example.scavengingscannables;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This class handles QR code operations asynchronously
 */
public class QRCodeHandler implements FirestoreDatabaseCallback {
    private FirestoreDatabaseController fdc;

    private NamingSystem namsys = new NamingSystem();

    private Activity activity;

    private Bitmap image;

    private String hash;

    private int score;

    private boolean storePhoto = true;

    private boolean storeLocation = true;

    private String username;

    private PlayerHandler ph;

    private HashMap<String, Double> locationMap;

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
        editExistingQRCode((QrCode) data);

        // Add QR code id to user's list of codes
        ph = new PlayerHandler(username, fdc, hash, activity);
        fdc.GetPlayerByUsername(username, ph);
    }

    // If the QR code exists
    @Override
    public void OnDocumentExists() {
        fdc.GetQRCodeByID(hash, this);
    }

    // If the QR code doesn't exist
    @Override
    public void OnDocumentDoesNotExist() {
        createNewQRCode();

        // Add QR code id to user's list of codes
        ph = new PlayerHandler(username, fdc, hash, activity);
        fdc.GetPlayerByUsername(username, ph);
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
            storePhoto = false;
        }

        //  If the user decided not to store a location, we'll set storeLocation to false
        if (locationMap.get("latitude") == 0 && locationMap.get("longitude") == 0) {
            storeLocation = false;
        }

        QRCodeImageLocationInfo qrCodeImageLocationInfo = new QRCodeImageLocationInfo(image, qrLocation, storePhoto, storeLocation);
        ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfoList = new ArrayList<>();
        qrCodeImageLocationInfoList.add(qrCodeImageLocationInfo);

        // Create a QR code using the data we've generated
        QrCode newCode = new QrCode(hash, Integer.toString(score), hashedName,  comments, ownedBy, qrCodeImageLocationInfoList);

        // Save the new QR code to the database
        fdc.SaveQRCodeByID(newCode);
    }

    /**
     * Edits an existing QR code object by adding the current user to the QR code's list of players who have scanned it
     * @param qrcode the QR code to edit
     */
    private void editExistingQRCode(QrCode qrcode) {
        // If there is no image, we'll set storePhoto to false
        if (image == null) {
            storePhoto = false;
        }

        //  If the user decided not to store a location, we'll set storeLocation to false
        if (locationMap.get("latitude") == 0 && locationMap.get("longitude") == 0) {
            storeLocation = false;
        }

        GeoPoint qrLocation = new GeoPoint(locationMap.get("latitude"), locationMap.get("longitude"));

        QRCodeImageLocationInfo qrCodeImageLocationInfo = new QRCodeImageLocationInfo(image, qrLocation, storePhoto, storeLocation);
        qrcode.AddQRCodeImageLocationInfo(qrCodeImageLocationInfo);

        // Get QR code using its id
        // Add the current user to its ownedBy list
        ArrayList<String> ownedBy = qrcode.getOwnedBy();
        if (!ownedBy.contains(username)) {
            qrcode.getOwnedBy().add(username);
            fdc.SaveQRCodeByID(qrcode);
        }
    }

}
