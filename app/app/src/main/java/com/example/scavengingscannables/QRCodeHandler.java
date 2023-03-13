package com.example.scavengingscannables;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class QRCodeHandler implements FirestoreDatabaseCallback {
//    private FirestoreDatabaseController fdc = new FirestoreDatabaseController();
    private FirestoreDatabaseController fdc;

    private NamingSystem namsys = new NamingSystem();

    private boolean storePhoto;

    private boolean storeLocation;

    double latitude;

    double longitude;

    Activity a;

    String hash;

    int score;

    FusedLocationProviderClient flpc;

    String username;


    public QRCodeHandler(Activity a, String hash, int score, FirestoreDatabaseController fdc) {
       this.a = a;
       this.hash = hash;
       this.score = score;
       this.fdc = fdc;
       this.flpc =  LocationServices.getFusedLocationProviderClient(this.a);

       SharedPreferences sharedPref = a.getSharedPreferences("account", Context.MODE_PRIVATE);
       username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
    }

    @Override
    public <T> void OnDataCallback(T data) {
        scanExistingQRCode((QrCode) data);
    }

    @Override
    public void OnDocumentExists() {
        fdc.GetQRCodeByID(hash, this);
    }

    @Override
    public void OnDocumentDoesNotExist() {
        scanNewQRCode();
    }

    private void scanNewQRCode() {
        // Ask the user if they want to store an image of the object they just scanned
        // Then, whether the user wants to store an image or not, we ask if they want to store the location of the object they just scanned
        askForPhoto();

        // Generate a name for the hash
        String hashedName = namsys.generateName(hash);

        // Create HashMap for comments, ArrayList for owners, and an ArrayList for locations
        // Add current user to list of owners
        HashMap<String, String> comments = new HashMap<String, String>();
        ArrayList<String> ownedBy = new ArrayList<>();
        ownedBy.add(username);

        // Store location based on the user's choices
        ArrayList<Double> qrLocation = new ArrayList<>();
        if (storeLocation) {
            qrLocation.add(latitude);
            qrLocation.add(longitude);
        }

        // Create a QR code using the data we've generated
        QrCode newCode = new QrCode(hash, Integer.toString(score), hashedName,  comments, ownedBy, qrLocation);

        // Save the new QR code to the database
        fdc.SaveQRCodeByID(newCode);
    }

    private void scanExistingQRCode(QrCode qrcode) {
        // Get QR code using its id
        // Add the current user to its ownedBy list
        qrcode.getOwnedBy().add(username);
        fdc.SaveQRCodeByID(qrcode);
    }

    private void askLocationPermissions() {
        // Here we ask the user if they want to store the object's location
        new AlertDialog.Builder(a)
                .setTitle("Do you want to store this code's location?")

                // If the user wants to store the location, we set the "storeLocation" flag to true
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeLocation = true;
                        if (ActivityCompat.checkSelfPermission(a, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                           getLocation();
                            flpc.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    Location location = task.getResult();
                                    if (location != null) {
                                        try {
                                            // Initialize geoCoder
                                            Geocoder geocoder = new Geocoder(a, Locale.getDefault());
                                            // Initialize address list
                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                            Toast.makeText(a, addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
                                            // Store latitude and longitude
                                            latitude = addresses.get(0).getLatitude();
                                            longitude = addresses.get(0).getLongitude();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                        else {
                            ActivityCompat.requestPermissions(a, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                    }
                })

                // If the user does not want to store the location, we set the "storeLocation" flag to false
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storeLocation = false;
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
    private void askForPhoto() {
        new AlertDialog.Builder(a)
                .setTitle("Do you want to store an image of the object you just scanned?")

                // If the user decides they want to store and image, we will launch their phone's camera application
                // We will also set the "storeCode" flag to true
                // Then we ask if they want to store the location of where they scanned the code
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storePhoto = true;
                        // Launch camera activity
                        Intent myIntent = new Intent(a, CameraActivity.class);
//                      myIntent.putExtra("key", value); //Optional parameters
                        a.startActivity(myIntent);
                        askLocationPermissions();
                    }
                })
                // If the user decides they do not want to store the image, we will go straight to asking them if they want to store the location of where they scanned the image
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storePhoto = false;
                        dialog.dismiss();
                        askLocationPermissions();
                    }
                })
                .create().show();
    }
}
