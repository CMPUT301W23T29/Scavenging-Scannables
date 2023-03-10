package com.example.scavengingscannables;

public class QrInfo {
    // Will generate a link to an image based on the hash of qrID
    public void GenerateVisualLink(String hash) {
        String seed = hash.substring(0, 63);
        String linkTemplate = "https://picsum.photos/" + seed + "/seed/200/";
        //Store link into database, can be pulled from database and shown whenever needed

    }
}
