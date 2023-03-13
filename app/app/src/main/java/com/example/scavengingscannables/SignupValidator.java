package com.example.scavengingscannables;

import java.util.regex.Pattern;

public class SignupValidator {
    /**
     * Validates usernames
     * @param username username to be validated
     * @return true if valid, false if not
     */
    public Boolean IsValidUsername(String username){
        if (username.matches("")){return false;}
        return !username.contains(" ");
    }

    /**
     * Validates names
     * @param name name to validate
     * @return true if valid, false if not
     */
    public Boolean IsValidName(String name){
        if (name.matches("")){return false;}
        return !name.contains(" ");
    }

    /**
     * Validates email using RFC 5322 standards
     * @param email the email to validate
     * @return true if valid email, false if not
     */
    public Boolean IsValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    /**
     * Validates phone number
     * @param phoneNumber phone number to be validated
     * @return true if valid, false if not
     */
    public Boolean IsValidPhoneNumber(Long phoneNumber){
        String phoneNumberRegex = "^\\d{10}$";
        if (phoneNumber < -1L){return false;}
        return Pattern.compile(phoneNumberRegex).matcher(String.valueOf(phoneNumber)).matches();
    }
}
