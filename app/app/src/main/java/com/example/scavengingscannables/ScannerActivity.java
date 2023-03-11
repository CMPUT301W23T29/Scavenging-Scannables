package com.example.scavengingscannables;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

    private NamingSystem namingSystem = new NamingSystem();
    private boolean storeCode;
    private boolean storeLocation;



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

                        String sha256hex = Hashing.sha256()
                                .hashString(result.getText(), StandardCharsets.UTF_8)
                                .toString();

//                        Toast.makeText(ScannerActivity.this, sha256hex,Toast.LENGTH_SHORT).show();
                        int score = scrsys.generateScore(sha256hex);

                        new AlertDialog.Builder(ScannerActivity.this)
                                .setTitle("Do you want to store this code's location?")
                                .setMessage("Your score is: " + score)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(ScannerActivity.this, "Code location stored.", Toast.LENGTH_SHORT).show();
                                        storeLocation = true;
//                                        ActivityCompat.requestPermissions(ScannerActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(ScannerActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
                                        storeLocation = false;
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();


                        new AlertDialog.Builder(ScannerActivity.this)
                                .setTitle("Do you want to store this code?")
//                                .setMessage("Permission is needed because of this and")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(ScannerActivity.this, "Code stored.", Toast.LENGTH_SHORT).show();
                                        storeCode = true;
//                                        ActivityCompat.requestPermissions(ScannerActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(ScannerActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
                                        storeCode = false;
                                        dialog.dismiss();
                                    }
                                })
                                .create().show();

                        String hashedName = namingSystem.generateName(sha256hex);
                        HashMap<String, String> DemoComments = new HashMap<String, String>();
                        ArrayList<String> DemoOwnedBy = new ArrayList<>();
                        ArrayList<Double> demoqrLocation = new ArrayList<>();
                        QrCode newCode = new QrCode(1234567, hashedName, Integer.toString(score),  DemoComments, DemoOwnedBy, demoqrLocation);

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