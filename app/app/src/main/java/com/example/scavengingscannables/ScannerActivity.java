package com.example.scavengingscannables;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.common.hash.Hashing;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;

    private FirestoreDatabaseController fdc = new FirestoreDatabaseController();

    private ScoringSystem scrsys = new ScoringSystem();

    private NamingSystem namsys = new NamingSystem();

    private boolean storeCode;

    private boolean storeLocation;

    int CAMERA_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
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
                                        storeCode = true;

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
                                        storeCode = false;
                                        dialog.dismiss();

                                        // Here we ask the user if they want to store the object's location
                                        new AlertDialog.Builder(ScannerActivity.this)
                                                .setTitle("Do you want to store this code's location?")

                                               // If the user wants to store the location, we set the "storeLocation" flag to true
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        storeLocation = true;
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

                        // These lines are temporary and just create dummy data to feed to our QR code
                        HashMap<String, String> DemoComments = new HashMap<String, String>();
                        ArrayList<String> DemoOwnedBy = new ArrayList<>();
                        ArrayList<Double> demoqrLocation = new ArrayList<>();

                        // Create a QR code using the data we've generated
                        QrCode newCode = new QrCode("1234567", hashedName, Integer.toString(score),  DemoComments, DemoOwnedBy, demoqrLocation);

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