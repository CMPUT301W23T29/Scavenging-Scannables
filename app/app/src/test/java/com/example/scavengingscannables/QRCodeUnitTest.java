package com.example.scavengingscannables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class QRCodeUnitTest {
    private final QRCode qrCode;

    QRCodeUnitTest(){
        String id = "70e0f1ade11debb6732029c267095e092b5b43ff271d4f8d9158cb004322f38b";
        String score = "54";
        String name = "Trustworthy Blue Omani Penguins Learning Soccer";
        HashMap<String, String> comments = new HashMap<>();
        ArrayList<String> ownedBy = new ArrayList<>();
        ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfoList = new ArrayList<>();
        this.qrCode = new QRCode(id, score, name, comments, ownedBy, qrCodeImageLocationInfoList);
    }
    @Test
    public void TestGetSCore(){
        Assertions.assertEquals("54", qrCode.getScore());
    }

    @Test
    public void TestGetID(){
        Assertions.assertEquals("70e0f1ade11debb6732029c267095e092b5b43ff271d4f8d9158cb004322f38b", qrCode.getqrId());
    }

    @Test
    public void TestGetName(){
        Assertions.assertEquals("Trustworthy Blue Omani Penguins Learning Soccer", qrCode.getNameText());
    }

    @Test
    public void TestGetComments(){
        Assertions.assertEquals(new HashMap<String, String>(), qrCode.getComments());
    }

    @Test
    public void TestGetOwnedBy(){
        Assertions.assertEquals(new ArrayList<String>(), qrCode.getOwnedBy());
    }

    @Test
    public void TestGetVisualLink(){
        Assertions.assertEquals("https://robohash.org/70e0f1ade11debb6732029c267095e092b5b43ff271d4f8d9158cb004322f38b/?set=set4", qrCode.getVisualLink());
    }

    @Test
    public void TestAddOwnedBy(){
        qrCode.AddOwnedBy("player1");
        Assertions.assertTrue(qrCode.getOwnedBy().contains("player1"));
        qrCode.AddOwnedBy("player2");
        Assertions.assertTrue(qrCode.getOwnedBy().contains("player2"));
    }

    @Test
    public void TestRemoveOwnedBy(){
        qrCode.RemoveOwnedBy("player1");
        Assertions.assertFalse(qrCode.getOwnedBy().contains("player1"));
    }

    @Test
    public void TestSetComments(){
        HashMap<String, String> comment = new HashMap<>();
        comment.put("player1", "comment");
        qrCode.setComments(comment);
        Assertions.assertTrue(qrCode.getComments().containsKey("player1"));
        Assertions.assertEquals("comment", qrCode.getComments().get("player1"));
    }
}
