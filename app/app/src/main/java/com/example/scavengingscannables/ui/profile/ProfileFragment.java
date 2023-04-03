package com.example.scavengingscannables.ui.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.MainActivity;
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

/**
 * Fragment for the current player, shows information about the player and allows you to view their qr codes
 */
public class ProfileFragment extends Fragment implements ProfileDeleteQRCodeCallback{

    private FragmentProfileBinding binding;
    Button viewQrCodes;

    Switch hideProfile;
    private TextView usernameView;
    private TextView phone;
    private TextView totalScanned;
    private TextView totalScore;
    private ImageView highest;
    private String highestId;
    private ImageView lowest;
    private String lowestId;
    private HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    String username;
    static String name;
    private Integer tScore = 0;
    private Integer tScanned = 0;
    private ArrayList<String> scores = new ArrayList<>();
    private String userPhone;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    private ArrayList<QrCode> playerQRCodes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileQRCodeAdapter profileQRCodeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        hideProfile = root.findViewById(R.id.hide);
        phone = root.findViewById(R.id.phone);
        totalScanned = root.findViewById(R.id.codes_scanned);
        totalScore = root.findViewById(R.id.total_score);
        highest = root.findViewById(R.id.n_image_highest_qr);
        lowest = root.findViewById(R.id.n_image_lowest_qr);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        usernameView = root.findViewById(R.id.user_name);
        username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
        usernameView.setText(username);
        name = username;

        //Change the hideProfile switch to the state in database
        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                Boolean b = p.getHide();
                hideProfile.setChecked(b);
            }
        });

        hideProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        Player p = (Player)data;
                        if (hideProfile.isChecked()) {
                            p.setHide(true);
                        }
                        else{
                            p.setHide(false);
                        }
                        dbc.SavePlayerByUsername(p);
                    }
                });

            }
        });

        this.recyclerView = root.findViewById(R.id.profile_recycler_view);
        this.profileQRCodeAdapter = new ProfileQRCodeAdapter(this.playerQRCodes, username);
        this.profileQRCodeAdapter.setCallback(this);
        this.recyclerView.setAdapter(profileQRCodeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);

        updateView();

        return root;
    }

    public void resetView(){
        playerQRCodes.clear();
        tScore = 0;
        tScanned = 0;
        lowestId = "";
        highestId = "";
        lowestHighest.clear();
        scores.clear();
        profileQRCodeAdapter.notifyDataSetChanged();
        totalScore.setText("0");
        totalScanned.setText("0");
        highest.setImageResource(R.drawable.vector_image_not_found);
        lowest.setImageResource(R.drawable.vector_image_not_found);
    }

    public void updateView(){
        Log.d("LOG", "UPODATING VIEW");
        resetView();
        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                userPhone = p.getPhoneNumber().toString();
                phone.setText(userPhone);
                qrCodes = p.getScannedQRCodesID();

                p.setTotal(tScore.toString());
                p.setHighest(highestId);
                p.setLowest(lowestId);
                dbc.SavePlayerByUsername(p);

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
                                Picasso.get().load(ql.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(lowest);
                            }
                        });
                        highestId = list.get((list.size())-1).getKey();
                        dbc.GetQRCodeByID(highestId, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                QrCode ql = (QrCode)data;
                                Picasso.get().load(ql.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(highest);
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
                        Log.d("LOG", tScore.toString());
                        p.setTotal(tScore.toString());
                        p.setHighest(highestId);
                        p.setLowest(lowestId);
                        dbc.SavePlayerByUsername(p);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onDeleteQRCode() {
        updateView();
    }
}