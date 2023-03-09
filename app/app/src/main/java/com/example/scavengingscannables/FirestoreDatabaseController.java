package com.example.scavengingscannables;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class FirestoreDatabaseController{
    private Gson gson = new Gson();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private String QrCodeCollectionName = "QRCodes";
    private String PlayersCollectionName = "Players";

    public void GetQRCodeByID(Integer id, FirestoreDatabaseCallback callback){
        String idString = String.valueOf(id);
        db.collection(QrCodeCollectionName).document(idString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                QrCode qrCode = documentSnapshot.toObject(QrCode.class);
                callback.OnDataCallback(qrCode);
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

    public void GetPlayerByUsername(String username, FirestoreDatabaseCallback callback){
        db.collection(PlayersCollectionName).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Player player = documentSnapshot.toObject(Player.class);
                callback.OnDataCallback(player);
            }
        });
    }

    public void GetAllUsernames(FirestoreDatabaseCallback callback){
        db.collection(PlayersCollectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<String> usernames = new ArrayList<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        usernames.add(document.getId());
                    }
                    callback.OnDataCallback(usernames);
                }
            }
        });
    }

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

    public void CheckUsernameExists(String username, FirestoreDatabaseCallback callback){
        db.collection(PlayersCollectionName).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    callback.OnDocumentExists();
                }else{
                    callback.OnDocumentDoesNotExist();
                }
            }
        });
    }

    public void CheckQRIDExists(Integer id, FirestoreDatabaseCallback callback){
        String idString = String.valueOf(id);
        db.collection(QrCodeCollectionName).document(idString).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    callback.OnDocumentExists();
                }else{
                    callback.OnDocumentDoesNotExist();
                }
            }
        });
    }

    public void DeleteQrcodeFromPlayer(String playerUsername, int QrCodeID) {
        db.collection(PlayersCollectionName)
                .document(playerUsername)
                .update("scannedQRCodesID", FieldValue.arrayRemove(QrCodeID))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("LOG", "successfully deleted QrCodeID " + QrCodeID + " from " + playerUsername);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LOG", "could not delete QrCodeID " + QrCodeID + " from " + playerUsername + " due to error: " + e.getMessage());
                    }
                });
    }

    // maybe don't need DeleteQRCode
    public void DeleteQRCode(QrCode qrcode) {
        String qrCodeIDString = String.valueOf(qrcode.getQrId());
        db.collection(QrCodeCollectionName)
                .document(qrCodeIDString)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("LOG", "successfully deleted " + qrCodeIDString);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LOG", "could not deleted " + qrCodeIDString);
                    }
                });
    }


}
