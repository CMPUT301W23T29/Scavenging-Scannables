package com.example.scavengingscannables;

public class PlayerHandler implements FirestoreDatabaseCallback {
    String username;

    FirestoreDatabaseController fdc;

    Player player;

    String codeID;

    public PlayerHandler(String username, FirestoreDatabaseController fdc, String codeID) {
        this.username = username;
        this.fdc = fdc;
        this.codeID = codeID;
    }

    @Override
    public <T> void OnDataCallback(T data) {
        player = (Player) data;
        player.AddQRCodeByID(codeID);
        fdc.SavePlayerByUsername(player);
    }

    @Override
    public void OnDocumentExists() {
    }

    @Override
    public void OnDocumentDoesNotExist() {
    }
}
