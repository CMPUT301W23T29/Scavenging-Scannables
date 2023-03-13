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
    private QRCodeHandler qrch;

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

                        qrch = new QRCodeHandler(ScannerActivity.this, sha256hex, score, fdc);

                        // Tell the user what the score of the QR code they scanned was
                        Toast.makeText(ScannerActivity.this, "Your score is: " + score,Toast.LENGTH_SHORT).show();

                        fdc.CheckQRIDExists(sha256hex, qrch);
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