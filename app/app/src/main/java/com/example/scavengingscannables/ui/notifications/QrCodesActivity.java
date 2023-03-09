package com.example.scavengingscannables.ui.notifications;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.LoginActivity;
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

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class QrCodesActivity extends AppCompatActivity {

    String userName;
    Player currentPlayer;
    Button backButton;
    Button deleteButton;
    Boolean deleteState;
    QrCode qrCode;
    ArrayList<Integer> qrCodeIDs;

    FirestoreDatabaseController db;

    ListView qrCodesListView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        deleteState = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcodes);
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        db = new FirestoreDatabaseController();
        ArrayList<QrCode> arrayList = new ArrayList<QrCode>();

        QrCustomerArrayAdapter QrAdapter = new QrCustomerArrayAdapter(this, arrayList);
        qrCodesListView = findViewById(R.id.qrcode_list);
        qrCodesListView.setAdapter(QrAdapter);

        //Who's profile
        userName = LoginActivity.MyUsername;
        userName = "zhi";


        //Show my qr code
        db.GetPlayerByUsername(userName, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                currentPlayer = p;
            }
        });

        //Show my current qr code
        qrCodeIDs = currentPlayer.getScannedQRCodesID();
        for (int qrcodeId:qrCodeIDs){
            db.GetQRCodeByID(qrcodeId, new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    QrCode q = (QrCode)data;
                    qrCode = q;
                }
            });
            arrayList.add(qrCode);
        }
        QrAdapter.notifyDataSetChanged();



        //delete QRCode
        deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (deleteState == false) {
                    deleteState = true;
                }
                else{
                    deleteState = false;
                }
            }
        });
        qrCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Click to delete Qrcode
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (deleteState) {
                    QrCode qrCode = QrAdapter.getItem(i);
                    db.DeleteQrcodeFromPlayer(currentPlayer.getUsername(),qrCode.getQrId());
                    arrayList.remove(i);
                    QrAdapter.notifyDataSetChanged();
                }
            }
        });



    }
}
