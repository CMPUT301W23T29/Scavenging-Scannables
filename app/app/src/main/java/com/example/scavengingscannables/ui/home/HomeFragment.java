package com.example.scavengingscannables.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Home fragment, which will host the leaderboards in the future
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String name;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    private String highestId;
    private String lowestId;
    private Integer tScore = 0;
    private final ArrayList<String> scores = new ArrayList<>();
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    ImageButton btnSearch;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnSearch = root.findViewById(R.id.search_button1);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        name = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
        dbc.GetPlayerByUsername(name, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        Player p = (Player) data;
                        qrCodes = p.getScannedQRCodesID();
                        if (qrCodes.size() > 0) {
                            for (int i = 0; i < qrCodes.size(); i++) {
                                String qrcode = qrCodes.get(i);
                                dbc.GetQRCodeByID(qrcode, new FirestoreDatabaseCallback() {
                                    @Override
                                    public <T> void OnDataCallback(T data) {
                                        QrCode q = (QrCode) data;
                                        lowestHighest.put(qrcode, Integer.valueOf(q.getScore()));
                                        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowestHighest.entrySet());
                                        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                            @Override
                                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                                return o1.getValue().compareTo(o2.getValue());
                                            }
                                        });
                                        lowestId = list.get(0).getKey();
                                        p.setLowest(lowestId);
                                        highestId = list.get((list.size()) - 1).getKey();
                                        p.setHighest(highestId);
                                        tScore = 0;
                                        scores.add(q.getScore());
                                        for (int i=0;i<scores.size();i++) {
                                            tScore += Integer.parseInt(scores.get(i));
                                        }
                                        p.setTotal(String.valueOf(tScore));
                                        dbc.SavePlayerByUsername(p);
                                    }
                                });
                            }
                        }else{
                            p.setTotal("0");
                            dbc.SavePlayerByUsername(p);
                        }
                    }
                });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindUserActivity.class);
                startActivity(intent);
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