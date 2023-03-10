package com.example.scavengingscannables;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupValidationUnitTest {

    @Test
    public void TestUsernameValidation(){
        SignupValidator validator = new SignupValidator();
        String username = "";
        assertFalse(validator.IsValidUsername(username));
        username = "abc c";
        assertFalse(validator.IsValidUsername(username));
        username = "abc ";
        assertFalse(validator.IsValidUsername(username));
        username = "abc";
        assertTrue(validator.IsValidUsername(username));
    }
}
