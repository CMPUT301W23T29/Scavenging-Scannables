package com.example.scavengingscannables.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    String QrCodeID;
    Button backButton;
    Button confirmButton;
    String username;
    EditText commentInput;
    ListView commentsView;

    HashMap<String, String> commentsHashMap;
    CommentCustomerArrayAdapter arrayAdapter;

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
        commentsView = findViewById(R.id.comments_list);
        commentsHashMap = new HashMap<String,String>();
        arrayAdapter = new CommentCustomerArrayAdapter(this);
        commentsView.setAdapter(arrayAdapter);


        username = ProfileFragment.name;

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //Show the Comments
        dbc.GetQRCodeByID(QrCodeID, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                arrayAdapter.clear();
                QrCode q = (QrCode) data;
                commentsHashMap = q.getComments();
                q.getComments().forEach((key, value) -> {
                    Comment comment = new Comment(key, value);
                    arrayAdapter.add(comment);
                    arrayAdapter.notifyDataSetChanged();
                });
            }
        });
        //Add a Comment

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = commentInput.getText().toString();
                dbc.GetQRCodeByID(QrCodeID, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        QrCode q = (QrCode) data;
                        HashMap<String,String> currentComments = q.getComments();
                        currentComments.put(username,input);
                        commentInput.setText("");
                        q.setComments(currentComments);
                        dbc.SaveQRCodeByID(q);
                        dbc.GetQRCodeByID(QrCodeID, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                arrayAdapter.clear();
                                QrCode q = (QrCode) data;
                                commentsHashMap = q.getComments();
                                q.getComments().forEach((key, value) -> {
                                    Comment comment = new Comment(key, value);
                                    arrayAdapter.add(comment);
                                    arrayAdapter.notifyDataSetChanged();
                                });
                            }
                        });

                    }
                });

            }
        });
    }
}
