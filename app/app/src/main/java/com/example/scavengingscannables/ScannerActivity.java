package com.example.scavengingscannables;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class ScannerActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private FirestoreDatabaseController fdc = new FirestoreDatabaseController();

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
                        Toast.makeText(ScannerActivity.this, result.getText(), Toast.LENGTH_SHORT).show();

                        HashMap<String, String> DemoComments = new HashMap<String, String>();
                        ArrayList<String> DemoOwnedBy = new ArrayList<>();
                        ArrayList<Double> demoqrLocation = new ArrayList<>();
                        QrCode newCode = new QrCode(123456, "DemoNameText", result.getText(), DemoComments, DemoOwnedBy, demoqrLocation);

                        fdc.SaveQRCodeByID(newCode);

                        finish();
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