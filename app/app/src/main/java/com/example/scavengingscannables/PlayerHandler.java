package com.example.scavengingscannables;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * This class handles Player operations asynchronously
 */
public class PlayerHandler implements FirestoreDatabaseCallback {
    private final String username;

    private final FirestoreDatabaseController fdc;

    private final String codeID;

    private final Activity activity;

    public PlayerHandler(String username, FirestoreDatabaseController fdc, String codeID, Activity activity) {
        this.username = username;
        this.fdc = fdc;
        this.codeID = codeID;
        this.activity = activity;
    }

    @Override
    public <T> void OnDataCallback(T data) {
        Player player = (Player) data;
        ArrayList<String> codeList = player.getScannedQRCodesID();
        if (codeList.contains(codeID)) {
            Toast.makeText(activity, "You have already scanned this QR code", Toast.LENGTH_LONG).show();
        } else {
            player.AddQRCodeByID(codeID);
            fdc.SavePlayerByUsername(player);
        }
    }

    @Override
    public void OnDocumentExists() {
    }

    @Override
    public void OnDocumentDoesNotExist() {
    }
}
