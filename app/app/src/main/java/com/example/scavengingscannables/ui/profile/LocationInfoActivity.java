package com.example.scavengingscannables.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QRCodeImageLocationInfo;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;


public class LocationInfoActivity extends AppCompatActivity {

    String id;
    Button back;
    TextView location;

    ImageView photo;

    LocationArrayAdapter locationAdapter;
    ListView locationListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_location);
        Intent intent = getIntent();
        id = intent.getStringExtra("QrCodeID");
        back = findViewById(R.id.location_back);
        locationAdapter = new LocationArrayAdapter(this);
        locationListview = findViewById(R.id.location_list);
        locationListview.setAdapter(locationAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();

        //Show all image with location
        dbc.GetQRCodeByID(id, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                QrCode q= (QrCode) data;
                if (q.getQrCodeImageLocationInfoList() != null) {
                    for (QRCodeImageLocationInfo qrCodeImageLocationInfo : q.getQrCodeImageLocationInfoList()) {
                        locationAdapter.add(qrCodeImageLocationInfo);
                    }
                }
            }
        });


    }
}
