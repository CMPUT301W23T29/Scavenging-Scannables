package com.example.scavengingscannables;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    private FirestoreDatabaseController fdc = new FirestoreDatabaseController();

    private ScoringSystem scrsys = new ScoringSystem();

    private NamingSystem namsys = new NamingSystem();

    private boolean storePhoto;

    private boolean storeLocation;

    int CAMERA_REQUEST_CODE = 102;

    double latitude;

    double longitude;

    FusedLocationProviderClient flpc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        flpc = LocationServices.getFusedLocationProviderClient(ScannerActivity.this);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Generate a SHA-256 hash of the QR code text
                        String sha256hex = Hashing.sha256()
                                .hashString(result.getText(), StandardCharsets.UTF_8)
                                .toString();

                        // Generate a score for the hash
                        int score = scrsys.generateScore(sha256hex);

                        // Tell the user what the score of the QR code they scanned was
                        Toast.makeText(ScannerActivity.this, "Your score is: " + score,Toast.LENGTH_SHORT).show();

                        // Ask the user if they want to store an image of the object they just scanned
                        // Then, whether the user wants to store an image or not, we ask if they want to store the location of the object they just scanned
                        new AlertDialog.Builder(ScannerActivity.this)
                                .setTitle("Do you want to store an image of the object you just scanned?")

                                // If the user decides they want to store and image, we will launch their phone's camera application
                                // We will also set the "storeCode" flag to true
                                // Then we ask if they want to store the location of where they scanned the code
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        storePhoto= true;

                                        // Launch camera activity
                                        Intent myIntent = new Intent(ScannerActivity.this, CameraActivity.class);
//                                      myIntent.putExtra("key", value); //Optional parameters
                                        ScannerActivity.this.startActivity(myIntent);

                                        // Here we ask the user if they want to store the object's location
                                        new AlertDialog.Builder(ScannerActivity.this)
                                                .setTitle("Do you want to store this code's location?")

                                                // If the user wants to store the location, we set the "storeLocation" flag to true
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        storeLocation = true;
                                                        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                                                            getLocation();
                                                            flpc.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Location> task) {
                                                                    Location location = task.getResult();
                                                                    if (location != null) {
                                                                        try {
                                                                            // Initialize geoCoder
                                                                            Geocoder geocoder = new Geocoder(ScannerActivity.this, Locale.getDefault());
                                                                            // Initialize address list
                                                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                                            Toast.makeText(ScannerActivity.this, addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
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
                                                            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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
                                })
                                // If the user decides they do not want to store the image, we will go straight to asking them if they want to store the location of where they scanned the image
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        storePhoto = false;
                                        dialog.dismiss();

                                        // Here we ask the user if they want to store the object's location
                                        new AlertDialog.Builder(ScannerActivity.this)
                                                .setTitle("Do you want to store this code's location?")

                                               // If the user wants to store the location, we set the "storeLocation" flag to true
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        storeLocation = true;
                                                        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                                                            getLocation();
                                                            flpc.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Location> task) {
                                                                    Location location = task.getResult();
                                                                    if (location != null) {
                                                                        try {
                                                                            // Initialize geoCoder
                                                                            Geocoder geocoder = new Geocoder(ScannerActivity.this, Locale.getDefault());
                                                                            // Initialize address list
                                                                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                                                            Toast.makeText(ScannerActivity.this, addresses.get(0).getLatitude() + ", " + addresses.get(0).getLongitude(), Toast.LENGTH_LONG).show();
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
                                                            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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
                                })
                                .create().show();

                        // Generate a name for the hash
                        String hashedName = namsys.generateName(sha256hex);

                        // Create HashMap for comments, ArrayList for owners, and an ArrayList for locations
                        HashMap<String, String> comments = new HashMap<String, String>();
                        ArrayList<String> ownedBy = new ArrayList<>();

                        // Store location based on the user's choices
                        ArrayList<Double> qrLocation = new ArrayList<>();
                        if (storeLocation) {
                            qrLocation.add(latitude);
                            qrLocation.add(longitude);
                        }

                        // Create a QR code using the data we've generated
                        QrCode newCode = new QrCode(sha256hex, Integer.toString(score), hashedName,  comments, ownedBy, qrLocation);

                        // Save the new QR code to the database
                        fdc.SaveQRCodeByID(newCode);
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}