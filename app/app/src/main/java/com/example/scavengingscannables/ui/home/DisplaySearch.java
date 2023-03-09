package com.example.scavengingscannables.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.scavengingscannables.R;

public class DisplaySearch extends AppCompatActivity {
    private  Button btn_back;
    private TextView name;
    private TextView phone;
    private TextView total;
    private TextView highest;
    private TextView number_of_scanned;
    private TextView lowest;
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
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, FindUserActivity.class);
                startActivity(intent);
            }
        });
    }
}