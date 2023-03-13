package com.example.scavengingscannables.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.QrCustomerArrayAdapter;

import java.util.ArrayList;

/**
 * Lists qr codes from other players
 */
public class OthersQrCodesActivity extends AppCompatActivity {
    Button backButton;
    ArrayList<QrCode> qrCodes;
    String username;
    ListView qrCodesListView;
    QrCustomerArrayAdapter qrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_others_qrcodes);
        backButton = findViewById(R.id.other_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("other");
        qrAdapter = new QrCustomerArrayAdapter(this);
        qrCodesListView = findViewById(R.id.other_qrcode_list);
        qrCodesListView.setAdapter(qrAdapter);

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetAllQrCodeOfUser(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                qrCodes = (ArrayList<QrCode>) data;
                qrAdapter.clear();
                for (QrCode qrcode:qrCodes) {
                    qrAdapter.add(qrcode);
                }
            }
        });
    }
}