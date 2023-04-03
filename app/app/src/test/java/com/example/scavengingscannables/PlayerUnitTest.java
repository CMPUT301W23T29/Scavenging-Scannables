package com.example.scavengingscannables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PlayerUnitTest {
    Player player;

    PlayerUnitTest(){
        String username = "TESTTESTTEST";
        String firstName = "First";
        String lastName = "Last";
        Long phoneNumber = 7801234567L;
        String email = "abc@abc.com";
        Boolean hide = false;
        String highest = "";
        String lowest = "";
        String total = "0";
        player = new Player(username, firstName, lastName, phoneNumber, email, hide, highest, lowest, total);
    }

    @Test
    public void TestAddQrCode(){
        player.AddQRCodeByID("123456");
        Assertions.assertTrue(player.getScannedQRCodesID().contains("123456"));
        player.AddQRCodeByID("7890");
        Assertions.assertTrue(player.getScannedQRCodesID().contains("7890"));
    }

    @Test
    public void TestRemoveQrCode(){
        player.RemoveQRCodeByID("123456");
        Assertions.assertFalse(player.getScannedQRCodesID().contains("123456"));
    }
}
