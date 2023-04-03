package com.example.scavengingscannables.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentProfileBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherPlayerProfileActivity extends AppCompatActivity {
    private TextView usernameView;
    private TextView phone;
    private TextView totalScanned;
    private TextView totalScore;
    private ImageView highest;
    private String highestId;
    private ImageView lowest;
    private String lowestId;
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    String username;
    private Integer tScore = 0;
    private Integer tScanned = 0;
    private final ArrayList<String> scores = new ArrayList<>();
    private String userPhone;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    private ArrayList<QrCode> playerQRCodes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileQRCodeAdapter profileQRCodeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_other_profile_activity);

        phone = findViewById(R.id.phone);
        totalScanned = findViewById(R.id.codes_scanned);
        totalScore = findViewById(R.id.total_score);
        highest = findViewById(R.id.n_image_highest_qr);
        lowest = findViewById(R.id.n_image_lowest_qr);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("user");

        usernameView = findViewById(R.id.user_name);

        this.recyclerView = findViewById(R.id.profile_recycler_view);
        this.profileQRCodeAdapter = new ProfileQRCodeAdapter(this.playerQRCodes, username);

        this.recyclerView.setAdapter(profileQRCodeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                userPhone = p.getPhoneNumber().toString();
                Boolean b = p.getHide();
                usernameView.setText(username);

                if (!b){
                    totalScanned.setText("0");
                    totalScore.setText("0");
                    Drawable imageNotFound = ResourcesCompat.getDrawable(getResources(), R.drawable.vector_image_not_found, null);
                    highest.setImageDrawable(imageNotFound);
                    lowest.setImageDrawable(imageNotFound);
                    phone.setText(userPhone);
                    dbc.GetAllQrCodeOfUserOneByOne(username, new FirestoreDatabaseCallback() {
                        @Override
                        public <T> void OnDataCallback(T data) {
                            QrCode qrCode = (QrCode) data;

                            playerQRCodes.add(qrCode);
                            profileQRCodeAdapter.notifyDataSetChanged();

                            lowestHighest.put(qrCode.getqrId(),Integer.valueOf(qrCode.getScore()));
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
                                    Picasso.get().load(ql.getVisualLink()).into(lowest);
                                }
                            });
                            highestId = list.get((list.size())-1).getKey();
                            dbc.GetQRCodeByID(highestId, new FirestoreDatabaseCallback() {
                                @Override
                                public <T> void OnDataCallback(T data) {
                                    QrCode ql = (QrCode)data;
                                    Picasso.get().load(ql.getVisualLink()).into(highest);
                                }
                            });

                            scores.add(qrCode.getScore());
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
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
