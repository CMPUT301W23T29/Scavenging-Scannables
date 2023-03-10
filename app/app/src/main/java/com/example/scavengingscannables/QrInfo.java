package com.example.scavengingscannables;

public class QrInfo {
    // Will generate a link to an image based on the hash of qrID
    public String GenerateVisualLink(String hash) {
        String seed = hash.substring(0, 63);
        String linkTemplate = "https://picsum.photos/" + seed + "/seed/200/";
        return linkTemplate;

    }
}
