package com.example.scavengingscannables.ui.notifications;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.MainActivity;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class QrCodesActivity extends AppCompatActivity {
    Button backButton;
    Button deleteButton;
    Boolean deleteState = false;
    ArrayList<QrCode> qrCodes;
    String username;
    FirebaseFirestore db;
    ListView qrCodesListView;
    QrCustomerArrayAdapter QrAdapter;
    Boolean confirm;
    int pos;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcodes);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        backButton = findViewById(R.id.button_back);
        QrAdapter = new QrCustomerArrayAdapter(this);
        qrCodesListView = findViewById(R.id.qrcode_list);
        qrCodesListView.setAdapter(QrAdapter);
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
        builder.setPositiveButton("no", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        builder.setNegativeButton("yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            QrCode qrCode = QrAdapter.getItem(pos);
            QrAdapter.remove(qrCode);
            dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    Player p = (Player) data;
                    p.RemoveQRCodeByID(qrCode.getQrId());
                    dbc.SavePlayerByUsername(p);
                    QrAdapter.notifyDataSetChanged();
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
                QrAdapter.clear();
                for (QrCode qrcode:qrCodes) {
                    QrAdapter.add(qrcode);
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


