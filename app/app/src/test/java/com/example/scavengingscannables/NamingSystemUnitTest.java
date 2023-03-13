package com.example.scavengingscannables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NamingSystemUnitTest {

    @Test
    public void TestNamingSystem(){
        NamingSystem ns = new NamingSystem();
        String name = ns.generateName("70e0f1ade11debb6732029c267095e092b5b43ff271d4f8d9158cb004322f38b");
        Assertions.assertEquals("Trustworthy Blue Omani Penguins Learning Soccer", name);
        name = ns.generateName("012345");
        Assertions.assertEquals("Edgy Red Canadian Lions Singing Archery", name);
    }
}
