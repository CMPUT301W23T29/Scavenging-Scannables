package com.example.scavengingscannables;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class FirestoreDatabaseController{
    private Gson gson = new Gson();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private String QrCodeCollectionName = "QRCodes";
    private String PlayersCollectionName = "Players";
    static private boolean userExists;

    // DOES NOT CURRENTLY WORK
    public QrCode GetQRCodeByID(Integer id){
        QrCode qrCode = new QrCode();
        return qrCode;
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

    // DOES NOT CURRENTLY WORK
    public void GetPlayerByUsername(String username, ReturnPlayerFromDatabase callback){
        Log.d("LOG", username);
        db.collection(QrCodeCollectionName).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("LOG", documentSnapshot.getData().toString());
                Player player = documentSnapshot.toObject(Player.class);
                Log.d("LOG", String.valueOf(player));
                callback.returnPlayer(player);
            }
        });
    }

    public void SavePlayerByUsername(Player player){
        String playerUsername = player.getUsername();

        db.collection(PlayersCollectionName)
                .document(playerUsername)
                .set(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("LOG", "successfully saved " + playerUsername);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("LOG", "coudl not save " + playerUsername);
                    }
                });
    }

    // DOES NOT CURRENTLY WORK
    // REQUIRES GetPlayerByUsername TO PROPERLY WORK
    public boolean CheckIfAccountExistsByUsername(String username){
        return false;
    }
}
