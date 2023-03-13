package com.example.scavengingscannables.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    Button viewQrCodes;
    private TextView usernameView;
    private TextView phone;
    private TextView totalScanned;
    private TextView totalScore;
    private ImageView highest;
    private String highestId;
    private ImageView lowest;
    private String lowestId;
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    String username;
    static String name;
    private Integer tScore = 0;
    private Integer tScanned = 0;
    private final ArrayList<String> scores = new ArrayList<>();
    private String userPhone;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewQrCodes = root.findViewById(R.id.ViewQrCodes);
        phone = root.findViewById(R.id.phone);
        totalScanned = root.findViewById(R.id.codes_scanned);
        totalScore = root.findViewById(R.id.total_score);
        highest = root.findViewById(R.id.n_image_highest_qr);
        lowest = root.findViewById(R.id.n_image_lowest_qr);
        viewQrCodes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QrCodesActivity.class);
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
                                    highestId = list.get((list.size())-1).getKey();

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}