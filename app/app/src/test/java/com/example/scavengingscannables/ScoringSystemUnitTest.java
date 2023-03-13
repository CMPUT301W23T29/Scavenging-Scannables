package com.example.scavengingscannables;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScoringSystemUnitTest {
    @Test
    public void TestScoringSystem(){
        ScoringSystem ss = new ScoringSystem();
        int score = ss.generateScore("70e0f1ade11debb6732029c267095e092b5b43ff271d4f8d9158cb004322f38b");
        Assertions.assertEquals(54, score);
        score = ss.generateScore("696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6");
        Assertions.assertEquals(115, score);
    }
}
