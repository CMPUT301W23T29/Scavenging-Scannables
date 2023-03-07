package com.example.scavengingscannables;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class FirestoreDatabaseController{
    private Gson gson = new Gson();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private String QrCodeCollectionName = "QRCodes";
    private String PlayersCollectionName = "Players";

    public void GetQRCodeByID(Integer id, GetQRCodeFromDatabaseCallback callback){
        String idString = String.valueOf(id);
        db.collection(QrCodeCollectionName).document(idString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                QrCode qrCode = documentSnapshot.toObject(QrCode.class);
                callback.returnQRCode(qrCode);
            }
        });
    }

    public void SaveQRCodeByID(QrCode qrcode){
        String qrCodeIDString = String.valueOf(qrcode.getQrId());

        db.collection(QrCodeCollectionName)
          .document(qrCodeIDString)
          .set(qrcode)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Log.d("LOG", "successfully saved " + qrCodeIDString);
              }
          })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.d("LOG", "could not save " + qrCodeIDString);
              }
          });
    }

    public void GetPlayerByUsername(String username, GetPlayerFromDatabaseCallback callback){
        db.collection(PlayersCollectionName).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Player player = documentSnapshot.toObject(Player.class);
                callback.returnPlayer(player);
            }
        });
    }

    // need to figure out a way to prevent duplicate logins?
    public void SavePlayerByUsername(Player player) {
        String playerUsername = player.getUsername();

        db.collection(PlayersCollectionName)
                .document(playerUsername)
                .set(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("LOG", "successfully saved" + playerUsername);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LOG", "could not save" + playerUsername);
                    }
                });
    }
}
