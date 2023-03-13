package com.example.scavengingscannables.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplaySearch extends AppCompatActivity {
    private  Button buttonBack;
    private  Button buttonViewQrCodes;
    private TextView name;
    private TextView phone;
    private TextView totalScanned;
    private TextView totalScore;
    private String highestId;
    private String lowestId;
    private ImageView lowestQR;
    private ImageView highestQR;
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    private Integer tScore = 0;
    private Integer tScanned = 0;
    private final ArrayList<String> scores = new ArrayList<>();
    private String userPhone;
    private ArrayList<String> qrIDs = new ArrayList<>();
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        name = findViewById(R.id.display_user_name);
        phone = findViewById(R.id.display_phone);
        totalScore = findViewById(R.id.display_total_score);
        ImageView highest = findViewById(R.id.display_highest_qr);
        ImageView lowest = findViewById(R.id.display_lowest_qr);
        totalScanned = findViewById(R.id.display_codes_scanned);
        buttonBack = findViewById(R.id.display_back);
        buttonViewQrCodes = findViewById(R.id.view_qrCodes);
        lowestQR = findViewById(R.id.display_lowest_qr);
        highestQR = findViewById(R.id.display_highest_qr);
        Intent intent = getIntent();
        String str = intent.getStringExtra("user");
        name.setText(str);
        dbc.GetPlayerByUsername(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                userPhone = p.getPhoneNumber().toString();
                phone.setText(userPhone);
                Log.d("LOG", p.getEmail());

            }
        });
        dbc.GetAllQrCodeOfUser(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                FirestoreDatabaseCallback.super.OnDataCallback(data);
                qrIDs = (ArrayList<String>) data;
                Log.d("QRCODE List", String.valueOf(qrIDs));
            }
        });
        dbc.GetPlayerByUsername(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                userPhone = p.getPhoneNumber().toString();
                phone.setText(userPhone);
                qrCodes = p.getScannedQRCodesID();
                if (qrCodes.size() > 0){
                    for (int i = 0; i< qrCodes.size(); i++){
                        String qrcode = qrCodes.get(i);
                        dbc.GetQRCodeByID(qrcode, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                QrCode q = (QrCode)data;
                                lowestHighest.put(qrcode,Integer.valueOf(q.getScore()));
                                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowestHighest.entrySet());
                                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o1.getValue().compareTo(o2.getValue());
                                        }
                                    });
                                    lowestId = list.get(0).getKey();
                                    dbc.GetQRCodeByID(lowestId, new FirestoreDatabaseCallback() {
                                        @Override
                                        public <T> void OnDataCallback(T data) {
                                            QrCode ql = (QrCode)data;
                                            Picasso.get().load(ql.getVisualLink()).into(lowestQR);
                                        }
                                    });
                                    highestId = list.get((list.size())-1).getKey();
                                    dbc.GetQRCodeByID(highestId, new FirestoreDatabaseCallback() {
                                        @Override
                                        public <T> void OnDataCallback(T data) {
                                            QrCode ql = (QrCode)data;
                                            Picasso.get().load(ql.getVisualLink()).into(highestQR);
                                        }
                                    });

                                scores.add(q.getScore());
                                tScore = 0;
                                for (int i=0;i<scores.size();i++) {
                                    tScore += Integer.parseInt(scores.get(i));
                                }
                                totalScore.setText(tScore.toString());
                                tScanned = scores.size();
                                totalScanned.setText(tScanned.toString());
                            }
                        });
                    }
                }else {
                    totalScore.setText("0");
                    totalScanned.setText("0");
                }
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonViewQrCodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplaySearch.this, OthersQrCodesActivity.class);
                intent.putExtra("other",str);
                startActivity(intent);
            }
        });
    }
}