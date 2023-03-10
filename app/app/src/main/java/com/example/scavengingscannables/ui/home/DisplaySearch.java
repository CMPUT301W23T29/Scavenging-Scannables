package com.example.scavengingscannables.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class DisplaySearch extends AppCompatActivity {
    private  Button btn_back;
    private  Button button_viewQrCodes;
    private TextView name;
    private TextView phone;
    private TextView total;
    private TextView highest;
    private TextView number_of_scanned;
    private TextView lowest;
    private String user_phone;
    private ArrayList<Integer> qrids = new ArrayList<>();
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        name = (TextView) findViewById(R.id.display_user_name);
        phone = (TextView) findViewById(R.id.display_phone);
        total = (TextView) findViewById(R.id.display_total_score);
        highest = (TextView) findViewById(R.id.display_highest_score);
        lowest = (TextView) findViewById(R.id.display_lowest_score);
        number_of_scanned = (TextView) findViewById(R.id.display_codes_scanned);
        btn_back = (Button) findViewById(R.id.display_back);
        button_viewQrCodes = (Button) findViewById(R.id.view_qrCodes);
        Intent intent = getIntent();
        String str = intent.getStringExtra("user");
        name.setText(str);
        dbc.GetPlayerByUsername(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                user_phone = p.getPhoneNumber().toString();
                phone.setText(user_phone);
                Log.d("LOG", p.getEmail());

            }
        });
        dbc.GetAllQrCodeOfUser("test", new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                FirestoreDatabaseCallback.super.OnDataCallback(data);
                qrids = (ArrayList<Integer>) data;
                Log.d("QRCODE List", String.valueOf(qrids));
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_viewQrCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, OthersQrCodesActivity.class);
                intent.putExtra("other",str);
                startActivity(intent);
            }
        });
    }
}