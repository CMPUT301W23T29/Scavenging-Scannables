package com.example.scavengingscannables.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.MainActivity;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentNotificationsBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    Button viewQrCodes;
    private TextView usernameView;
    private TextView phone;
    private TextView total_scanned;
    private TextView total_score;
    private ImageView highest;
    private String highest_id;
    private ImageView lowest;
    private String lowest_id;
    private HashMap<String,Integer> lowest_highest = new HashMap<>();
    private ArrayList<String> qrcodes = new ArrayList<>();
    String username;
    static String name;
    private Integer t_score = 0;
    private Integer t_scanned = 0;
    private ArrayList<String> scores = new ArrayList<>();
    private String user_phone;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewQrCodes = (Button)root.findViewById(R.id.ViewQrCodes);
        phone = (TextView)root.findViewById(R.id.phone);
        total_scanned = (TextView)root.findViewById(R.id.codes_scanned);
        total_score = (TextView)root.findViewById(R.id.total_score);
        highest = (ImageView)root.findViewById(R.id.n_image_highest_qr);
        lowest = (ImageView)root.findViewById(R.id.n_image_lowest_qr);
        viewQrCodes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QrCodesActivity.class);
                Gson gson = new Gson();
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        usernameView = root.findViewById(R.id.user_name);
        username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
        usernameView.setText(username);
        name = username;
        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
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
                                if(lowest_highest.size() == 1){
                                    //
                                }else{
                                    List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowest_highest.entrySet());
                                    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o1.getValue().compareTo(o2.getValue());
                                        }
                                    });
                                    lowest_id = list.get(0).getKey();
                                    highest_id = list.get((list.size())-1).getKey();
                                }
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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}