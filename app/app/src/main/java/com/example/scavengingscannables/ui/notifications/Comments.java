package com.example.scavengingscannables.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Comments extends AppCompatActivity {

    String QrCodeID;
    Button backButton;
    Button confirmButton;
    String userName;
    EditText commentInput;

    HashMap<String, String> commentsHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comments);
        Intent intent = getIntent();
        QrCodeID = intent.getStringExtra("QrCodeID");
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        backButton = findViewById(R.id.button_comments_back);
        confirmButton = findViewById(R.id.Confirm);
        commentInput = findViewById(R.id.input);
        ListView commentsView = findViewById(R.id.comments_list);
        commentsHashMap = new HashMap<String,String>();
        CommentCustomerArrayAdapter arrayAdapter = new CommentCustomerArrayAdapter(this);
        commentsView.setAdapter(arrayAdapter);


        userName = NotificationsFragment.name;

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = commentInput.getText().toString();
                dbc.GetQRCodeByID(QrCodeID, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        QrCode q = (QrCode) data;
                        HashMap<String,String> currentComments = q.getComments();
                        currentComments.put(userName,input);
                        commentInput.setText("");
                        q.setComments(currentComments);
                        dbc.SaveQRCodeByID(q);

                    }
                });

            }
        });
    }
}
