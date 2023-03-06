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
    Button backButton;
    Button deleteButton;
    Boolean deleteState;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        deleteState = false;
        FirebaseFirestore db;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcodes);
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("QrCodes");


        ArrayList<QrCode> arrayList = new ArrayList<QrCode>();
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "pp","1","1","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "cc", "1","Two","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "3","1", "Three","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "4","1", "Four","1"));

        QrCustomerArrayAdapter QrAdapter = new QrCustomerArrayAdapter(this, arrayList);

        ListView qrCodesListView = findViewById(R.id.qrcode_list);

        qrCodesListView.setAdapter(QrAdapter);





        //collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            //@Override
            //public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
           // FirebaseFirestoreException error) {
                //arrayList.clear();
                //for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
               // {
                    //Log.d(TAG, String.valueOf(doc.getData().get(" Name")));
                    //Integer imageId = doc.getId().
                   // String city = doc.getId();
                   // String province = (String) doc.getData().get("Province Name");
                   // arrayList.add(new QrCode(imageId,)); // Adding the cities and provinces from FireStore
                //}
                //QrAdapter.notifyDataSetChanged();
           // }
       // });

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
        if (deleteState) {
            qrCodesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //Click to delete Qrcode
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String codeName = QrAdapter.getItem(i).getQrName();
                    collectionReference
                            .document(codeName)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been deleted successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be deleted!" + e.toString());
                                }
                            });
                }
            });
        }


    }
}
