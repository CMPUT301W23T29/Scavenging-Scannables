package com.example.scavengingscannables.ui.profile;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class QrCodesActivity extends AppCompatActivity {
    Button backButton;
    Button deleteButton;
    Boolean deleteState = false;
    ArrayList<QrCode> qrCodes;
    String username;
    ListView qrCodesListView;
    QrCustomerArrayAdapter qrAdapter;
    int pos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcodes);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        backButton = findViewById(R.id.button_back);
        qrAdapter = new QrCustomerArrayAdapter(this);
        qrCodesListView = findViewById(R.id.qrcode_list);
        qrCodesListView.setAdapter(qrAdapter);
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (deleteState == false) {
                    deleteState = true;

                    deleteButton.setText("Click Qrcode to delete");
                }
                else{
                    deleteState = false;
                    deleteButton.setText("Delete");
                }
            }
        });

        //define a alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(QrCodesActivity.this);
        builder.setMessage("Do you want to delete ?");
        builder.setTitle("Warning !");
        builder.setCancelable(false);
        builder.setPositiveButton("no", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setNegativeButton("yes", (dialog, which) -> {
            QrCode qrCode = qrAdapter.getItem(pos);
            qrAdapter.remove(qrCode);
            dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    Player p = (Player) data;
                    p.RemoveQRCodeByID(qrCode.getqrId());
                    qrCode.RemoveOwnedBy(username);
                    dbc.SaveQRCodeByID(qrCode);
                    dbc.SavePlayerByUsername(p);
                    qrAdapter.notifyDataSetChanged();
                }
            });
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        //Show the list
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

        //Show alertdialog when delete
        qrCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           //Click to delete Qrcode
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               if (deleteState) {
                   pos = i;
                   alertDialog.show();
               }
           }
       });

        //Click comments to see comments


        /***
         dbc.DeleteQrcodeFromPlayer(username, qrCode.getQrId(), new FirestoreDatabaseCallback() {
        @Override
        public <T> void OnDataCallback(T data) {
        FirestoreDatabaseCallback.super.OnDataCallback(data);
        dbc.GetAllQrCodeOfUser(username, new FirestoreDatabaseCallback() {
        @Override
        public <T> void OnDataCallback(T data) {
        qrCodes = (ArrayList<QrCode>) data;
        QrAdapter.clear();
        for (QrCode qrcode:qrCodes) {
        QrAdapter.add(qrcode);
        }
        QrAdapter.notifyDataSetChanged();
        }
        });
        }
        });
         ***/
    }
}


