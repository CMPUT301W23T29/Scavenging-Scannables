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

    @Test
    public void TestEmailValidation(){
        SignupValidator validator = new SignupValidator();
        String email = "";
        assertFalse(validator.IsValidEmail(email));
        email = " @ .com";
        assertFalse(validator.IsValidEmail(email));
        email = "2 @com";
        assertFalse(validator.IsValidEmail(email));
        email = "2 @com ";
        assertFalse(validator.IsValidEmail(email));
        email = "abc@abc.com";
        assertTrue(validator.IsValidEmail(email));
    }

    @Test
    public void TestNumberValidation(){
        SignupValidator validator = new SignupValidator();
        Long number = 7809999999L;
        assertTrue(validator.IsValidPhoneNumber(number));
        number = 789999999L;
        assertFalse(validator.IsValidPhoneNumber(number));
        number = -7801234567L;
        assertFalse(validator.IsValidPhoneNumber(number));
    }
}
