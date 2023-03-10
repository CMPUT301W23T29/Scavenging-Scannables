package com.example.scavengingscannables;

public class SignupValidator {
    public Boolean IsValidUsername(String username){
        if (username.matches("")){return false;}
        if (username.contains(" ")){return false;}
        return true;
    }

    public Boolean IsValidName(String name){
        if (name.matches("")){return false;}
        return true;
    }

    public Boolean IsValidEmail(String email){
        if (email.matches("")){return false;}
        return true;
    }

    public Boolean IsValidPhoneNumber(Long phoneNumber){
        if (phoneNumber < -1L){return false;}
        return true;
    }
}
