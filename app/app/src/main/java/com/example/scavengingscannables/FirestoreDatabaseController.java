package com.example.scavengingscannables;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

/**
 * Class that handles all Firestore database interactions
 */
public class FirestoreDatabaseController{
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String QRCODE_COLLECTION_NAME = "QRCodes";
    private final String PLAYER_COLLECTION_NAME = "Players";

    /**
     * Gets a QrCode object from an ID from the database
     * @param id id of the qrcode to get
     * @param callback callback function where the qrcode is passed into
     */
    public void GetQRCodeByID(String id, FirestoreDatabaseCallback callback){
        db.collection(QRCODE_COLLECTION_NAME).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                QrCode qrCode = documentSnapshot.toObject(QrCode.class);
                callback.OnDataCallback(qrCode);
            }
        });
    }

    /**
     * Saves a QrCode into the database, overwrites any existing qrcode of the same ID (same qrcode)
     * @param qrcode qrcode to be saved
     */
    public void SaveQRCodeByID(@NonNull QrCode qrcode){
        String qrCodeIDString = qrcode.getqrId();

        db.collection(QRCODE_COLLECTION_NAME)
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

    /**
     * Gets all QrCodes of a player
     * @param username username of the player to query
     * @param callback callback function where the list of qrcodes is passed into
     */
    public void GetAllQrCodeOfUser(String username, FirestoreDatabaseCallback callback){
        GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player player = (Player)data;
                ArrayList<QrCode> qrCodesArray = new ArrayList<>();
                ArrayList<String> scannedIDs = player.getScannedQRCodesID();
                for (String id:scannedIDs) {
                    GetQRCodeByID(id, new FirestoreDatabaseCallback() {
                        @Override
                        public <T> void OnDataCallback(T data) {
                            qrCodesArray.add((QrCode) data);
                            callback.OnDataCallback(qrCodesArray);
                        }
                    });
                }
            }
        });
    }

    /**
     * Gets a player from their username from the databse
     * @param username username of the player to get
     * @param callback callback function where the player object is passed into
     */
    public void GetPlayerByUsername(String username, FirestoreDatabaseCallback callback){
        db.collection(PLAYER_COLLECTION_NAME).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Player player = documentSnapshot.toObject(Player.class);
                callback.OnDataCallback(player);
            }
        });
    }

    /**
     * Gets all usernames in the database (used for searching)
     * @param callback callback function where the list of usernames is passed into
     */
    public void GetAllUsernames(FirestoreDatabaseCallback callback){
        db.collection(PLAYER_COLLECTION_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    /**
     * Saves a player object into the database
     * @param player player to be saved
     */
    public void SavePlayerByUsername(@NonNull Player player) {
        String playerUsername = player.getUsername();

        db.collection(PLAYER_COLLECTION_NAME)
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

    /**
     * Checks if a username already exists in the database
     * @param username username to check
     * @param callback OnDocumentExists is called if username exists, OnDocumentDoesNotExist is called otherwise
     */
    public void CheckUsernameExists(String username, FirestoreDatabaseCallback callback){
        db.collection(PLAYER_COLLECTION_NAME).document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    /**
     * Checks if a QrCode exists in the database
     * @param id id of the qrcode to check
     * @param callback OnDocumentExists is called if qrcode exists, OnDocumentDoesNotExist is called otherwise
     */
    public void CheckQRIDExists(String id, FirestoreDatabaseCallback callback){
        db.collection(QRCODE_COLLECTION_NAME).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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
}
