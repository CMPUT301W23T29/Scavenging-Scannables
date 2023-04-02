package com.example.scavengingscannables;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scavengingscannables.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Main activity which is run after signup, creates the navigation menus to navigate between the fragments
 */
public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;
    private final int CAMERA_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // tests getting data from database
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetPlayerByUsername("321d213", new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
            }
        });

        // tests adding qrcode to database
        QrCode qrCode = new QrCode("123", "999", "TESTNAME", new HashMap<String, String>(), new ArrayList<String>(), new ArrayList<QRCodeImageLocationInfo>());
        dbc.SaveQRCodeByID(qrCode);

        // tests getting all usernames
        dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                ArrayList<String> usernames = (ArrayList<String>) data;
                Log.d("LOG", String.valueOf(usernames));
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_search, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

    }


}