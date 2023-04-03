package com.example.scavengingscannables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NamingSystemUnitTest {

    @Test
    public void TestNamingSystem(){
        NamingSystem ns = new NamingSystem();
        String name = ns.generateName("18e90a9749f0b2c8e409cd498a9b3e39b26a27a9d18da1fe8809c618010ad60d");
        Assertions.assertEquals("Clever Petite Gray Japanese Plastic Crocodile", name);
        name = ns.generateName("012345");
        Assertions.assertEquals("Edgy Tiny Yellow Danish Wood Kangaroo", name);
    }
}
