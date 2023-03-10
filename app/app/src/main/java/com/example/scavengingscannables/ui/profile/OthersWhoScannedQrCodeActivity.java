package com.example.scavengingscannables.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class OthersWhoScannedQrCodeActivity extends AppCompatActivity {

    String qrCodeID;
    Button backButton;
    ArrayList<String> playerNamesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_others);
        Intent intent = getIntent();
        qrCodeID = intent.getStringExtra("QrCodeID");

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        backButton = findViewById(R.id.button_others_back);
        ListView playerNamesView = findViewById(R.id.others_list);
        playerNamesList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, R.layout.playername_item,playerNamesList);
        playerNamesView.setAdapter(arrayAdapter);


        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                ArrayList<String> usernames = (ArrayList<String>) data;
                for (String username : usernames){
                    dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                        @Override
                        public <T> void OnDataCallback(T data) {
                            Player p = (Player)data;
                            if (p.getScannedQRCodesID().contains(qrCodeID)){
                                arrayAdapter.add(username);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        });

    }
}