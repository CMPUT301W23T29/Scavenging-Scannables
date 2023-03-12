package com.example.scavengingscannables.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplaySearch extends AppCompatActivity {
    private  Button btn_back;
    private  Button button_viewQrCodes;
    private TextView name;
    private TextView phone;
    private TextView total_scanned;
    private TextView total_score;
    private ImageView highest;
    private String highest_id;
    private ImageView lowest;
    private String lowest_id;
    private HashMap<String,Integer> lowest_highest = new HashMap<>();
    private ArrayList<String> qrcodes = new ArrayList<>();
    private Integer t_score = 0;
    private Integer t_scanned = 0;
    private ArrayList<String> scores = new ArrayList<>();
    private String user_phone;
    private ArrayList<String> qrids = new ArrayList<>();
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
        name = (TextView) findViewById(R.id.display_user_name);
        phone = (TextView) findViewById(R.id.display_phone);
        total_score = (TextView) findViewById(R.id.display_total_score);
        highest = (ImageView) findViewById(R.id.display_highest_qr);
        lowest = (ImageView) findViewById(R.id.display_lowest_qr);
        total_scanned = (TextView) findViewById(R.id.display_codes_scanned);
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
        dbc.GetAllQrCodeOfUser(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                FirestoreDatabaseCallback.super.OnDataCallback(data);
                qrids = (ArrayList<String>) data;
                Log.d("QRCODE List", String.valueOf(qrids));
            }
        });
        dbc.GetPlayerByUsername(str, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                user_phone = p.getPhoneNumber().toString();
                phone.setText(user_phone);
                qrcodes = p.getScannedQRCodesID();
                if (qrcodes.size() > 0){
                    for (int i=0;i<qrcodes.size();i++){
                        String qrcode = qrcodes.get(i);
                        dbc.GetQRCodeByID(qrcode, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                QrCode q = (QrCode)data;
                                lowest_highest.put(qrcode,Integer.valueOf(q.getScore()));
                                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowest_highest.entrySet());
                                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o1.getValue().compareTo(o2.getValue());
                                        }
                                    });
                                    lowest_id = list.get(0).getKey();
                                    highest_id = list.get((list.size())-1).getKey();

                                scores.add(q.getScore());
                                t_score = 0;
                                for (int i=0;i<scores.size();i++) {
                                    t_score += Integer.parseInt(scores.get(i));
                                }
                                total_score.setText(t_score.toString());
                                t_scanned = scores.size();
                                total_scanned.setText(t_scanned.toString());
                            }
                        });
                    }
                }else {
                    total_score.setText("0");
                    total_scanned.setText("0");
                }
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