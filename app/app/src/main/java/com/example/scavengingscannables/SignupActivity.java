package com.example.scavengingscannables;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * First screen that users sees, allows them to fill in profile information and create an account
 */
public class SignupActivity extends AppCompatActivity {

    Button signupButtonView;
    EditText usernameView;
    EditText firstNameView;
    EditText lastNameView;
    EditText emailView;
    EditText phoneNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();

        // checks if current device has a registered account
        SharedPreferences sharedPref = getSharedPreferences("account", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", " ");
        dbc.CheckUsernameExists(username, new FirestoreDatabaseCallback() {
            // account exists in database
            @Override
            public void OnDocumentExists() {
                SwitchToMainActivity();
            }

            @Override
            public void OnDocumentDoesNotExist() {
                setContentView(R.layout.signup_activity);
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
                        try {
                            phoneNumber = Long.parseLong(phoneNumberView.getText().toString());
                        } catch (NumberFormatException e) {
                            phoneNumber = -1L;
                        }
                        String email = emailView.getText().toString();

                        SignupValidator validator = new SignupValidator();
                        // checks if inputs are valid
                        if (!validator.IsValidUsername(username)) {
                            Toast.makeText(SignupActivity.this, "Invalid username!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!validator.IsValidName(firstName)) {
                            Toast.makeText(SignupActivity.this, "Invalid first name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!validator.IsValidName(lastName)) {
                            Toast.makeText(SignupActivity.this, "Invalid last name!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!validator.IsValidEmail(email)) {
                            Toast.makeText(SignupActivity.this, "Invalid email!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!validator.IsValidPhoneNumber(phoneNumber)) {
                            Toast.makeText(SignupActivity.this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // only allows creating if username does not exist in database
                        Long finalPhoneNumber = phoneNumber;
                        dbc.CheckUsernameExists(username, new FirestoreDatabaseCallback() {
                            // username does exist, prevent signup
                            @Override
                            public void OnDocumentExists() {
                                Toast.makeText(SignupActivity.this, username + " already exists!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnDocumentDoesNotExist() {
                                // create new user and save to database
                                Boolean hide =false;
                                String highest = "default";
                                String lowest = "default";
                                String total = "";
                                Player newPlayer = new Player(username, firstName, lastName, finalPhoneNumber, email, hide,highest,lowest,total);
                                dbc.SavePlayerByUsername(newPlayer);

                                // saves username to local file for future logins
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("hasAccount", true);
                                editor.putString("username", username);
                                editor.apply();

                                Toast.makeText(SignupActivity.this, "Welcome to Scavenging Scannables, " + username + "!", Toast.LENGTH_SHORT).show();
                                SwitchToMainActivity();
                            }
                        });
                    }
                });
            }
        });
    }

    // swap to main activity (home page)
    private void SwitchToMainActivity(){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
