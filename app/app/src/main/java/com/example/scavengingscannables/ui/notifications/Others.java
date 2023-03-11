package com.example.scavengingscannables.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class Others extends AppCompatActivity {

    Integer QrCodeID;
    Button backButton;
    ArrayList<String> playerNamesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_others);
        Intent intent = getIntent();
        QrCodeID = intent.getIntExtra("QrCodeID",0);

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        backButton = findViewById(R.id.button_others_back);
        ListView playerNamesView = findViewById(R.id.others_list);
        playerNamesList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this, R.layout.playername,playerNamesList);
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
                for (String userName : usernames){
                    dbc.GetPlayerByUsername(userName, new FirestoreDatabaseCallback() {
                        @Override
                        public <T> void OnDataCallback(T data) {
                            Player p = (Player)data;
                            if (p.getScannedQRCodesID().contains(QrCodeID)){
                                playerNamesList.add(userName);
                                arrayAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        });

    }
}