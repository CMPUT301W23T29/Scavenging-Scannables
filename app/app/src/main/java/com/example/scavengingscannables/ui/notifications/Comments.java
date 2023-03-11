package com.example.scavengingscannables.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

public class Comments extends AppCompatActivity {

    Integer QrCodeID;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comments);
        Intent intent = getIntent();
        QrCodeID = intent.getIntExtra("QrCodeID",0);
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        backButton = findViewById(R.id.button_comments_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        dbc.GetQRCodeByID(QrCodeID, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                QrCode q = (QrCode) data;

            }
        });
    }
}
