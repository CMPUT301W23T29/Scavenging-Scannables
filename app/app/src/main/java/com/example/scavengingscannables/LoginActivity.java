package com.example.scavengingscannables;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    Button signupButtonView;
    EditText usernameView;
    EditText firstNameView;
    EditText lastNameView;
    EditText emailView;
    EditText phoneNumberView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        SharedPreferences sharedPref = getSharedPreferences("account", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("hasAccount", false)){
            // if already has account, skip directly to main activity

            // swap to main activity (home page)
            Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(newIntent);
        }

        // find all views
        signupButtonView = findViewById(R.id.signup);
        usernameView = findViewById(R.id.username);
        firstNameView = findViewById(R.id.first_name);
        lastNameView = findViewById(R.id.last_name);
        emailView = findViewById(R.id.email);
        phoneNumberView = findViewById(R.id.phone_number);

        signupButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameView.getText().toString();
                String firstName = firstNameView.getText().toString();
                String lastName = lastNameView.getText().toString();

                Long phoneNumber;
                try{
                    phoneNumber = Long.parseLong(phoneNumberView.getText().toString());
                }
                catch (NumberFormatException e){
                    phoneNumber = -1L;
                }
                String email = emailView.getText().toString();

                FirestoreDatabaseController dbc = new FirestoreDatabaseController();
                // checks if inputs are valid
                if (!username.matches("") && !firstName.matches("") && !lastName.matches("") && !email.matches("") && phoneNumber != -1L){
                    // create new user and save to database
                    Player newPlayer = new Player(username, firstName, lastName, phoneNumber, email);
                    dbc.SavePlayerByUsername(newPlayer);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("hasAccount", true);
                    editor.putString("username", username);
                    editor.apply();

                    // swap to main activity (home page)
                    Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(newIntent);
                }
            }
        });
    }
}
