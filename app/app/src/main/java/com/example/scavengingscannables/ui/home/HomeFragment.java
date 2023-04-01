package com.example.scavengingscannables.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

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
    private String target;
    private String highest;
    private TextView nameTop10;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    private String highestId;
    private String lowestId;
    private Integer tScore = 0;
    private ListView top10;
    private boolean s = false;
    private Switch changeTop10;
    ArrayList<String> allUsernames = new ArrayList<>();
    private final ArrayList<String> scores = new ArrayList<>();
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private final HashMap<String,Integer> lowestHighest1 = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    private Button btnSearch;
    ArrayAdapter<String> searchResultAdapter;
    ArrayList<String> output = new ArrayList<>();
    ArrayList<String> output1 = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnSearch = root.findViewById(R.id.search_button1);
        changeTop10 = root.findViewById(R.id.switch_top10);
        top10 = root.findViewById(R.id.top10);
        nameTop10 = root.findViewById(R.id.name_top10);
        searchResultAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, output);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        name = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
        dbc.GetPlayerByUsername(name, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        Player p = (Player) data;
                        qrCodes = p.getScannedQRCodesID();
                        if (qrCodes.size() > 0) {
                            scores.clear();
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
                                        scores.add(q.getScore());
                                        tScore = 0;
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
        changeTop10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s = !s;
                updateView();
            }
        });
        top10.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                target = output1.get(i);
                Intent intent = new Intent(getActivity(), DisplaySearch.class);
                intent.putExtra("user",target);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FindUserActivity.class);
                startActivity(intent);
            }
        });
        updateView();
        return root;
    }

    private void updateView(){
        if (s == false){
            nameTop10.setText("Top10 (Total Score)");
            dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    allUsernames = (ArrayList<String>) data;
                    Log.d("SEARCH1", String.valueOf(allUsernames));
                    for (int i = 0; i < allUsernames.size(); i++) {

                        int finalI = i;
                        dbc.GetPlayerByUsername(allUsernames.get(i), new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                Player p = (Player) data;
                                String username = p.getUsername();
                                lowestHighest1.put(username, Integer.valueOf(p.getTotal()));
                                List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowestHighest1.entrySet());
                                Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                    @Override
                                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                        return o1.getValue().compareTo(o2.getValue());
                                    }
                                });
                                if(list.size() <= 10){
                                    output.clear();
                                    output1.clear();
                                    searchResultAdapter.clear();
                                    top10.setAdapter(searchResultAdapter);
                                    for(int j=1;j<list.size()+1;j++){
                                        output.add(list.get(list.size()-j).getKey()+": "+list.get(list.size()-j).getValue());
                                        output1.add(list.get(list.size()-j).getKey());
                                    }
                                }else {
                                    output.clear();
                                    output1.clear();
                                    searchResultAdapter.clear();
                                    top10.setAdapter(searchResultAdapter);
                                    for (int j = 1; j < 11; j++) {
                                        output.add(list.get(list.size()-j).getKey()+": "+list.get(list.size()-j).getValue());
                                        output1.add(list.get(list.size()-j).getKey());
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }else{
            nameTop10.setText("Top10 (The Highest Score)");
            dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    allUsernames = (ArrayList<String>) data;
                    Log.d("SEARCH2", String.valueOf(allUsernames));
                    for (int i = 0; i < allUsernames.size(); i++){
                        int finalI2 = i;
                        dbc.GetPlayerByUsername(allUsernames.get(i), new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                Player p = (Player) data;
                                highest = p.getHighest();
                                dbc.GetQRCodeByID(highest, new FirestoreDatabaseCallback() {
                                    @Override
                                    public <T> void OnDataCallback(T data) {
                                        QrCode q = (QrCode) data;
                                        lowestHighest1.put(allUsernames.get(finalI2),Integer.valueOf(q.getScore()));
                                        List<Map.Entry<String, Integer>> list1 = new ArrayList<Map.Entry<String, Integer>>(lowestHighest1.entrySet());
                                        Collections.sort(list1, new Comparator<Map.Entry<String, Integer>>() {
                                            @Override
                                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                                return o1.getValue().compareTo(o2.getValue());
                                            }
                                        });
                                        if(list1.size() <= 10){
                                            output.clear();
                                            output1.clear();
                                            searchResultAdapter.clear();
                                            top10.setAdapter(searchResultAdapter);
                                            for(int j=1;j<list1.size()+1;j++){
                                                output.add(list1.get(list1.size()-j).getKey()+": "+list1.get(list1.size()-j).getValue());
                                                output1.add(list1.get(list1.size()-j).getKey());
                                            }
                                        }else{
                                            output.clear();
                                            output1.clear();
                                            searchResultAdapter.clear();
                                            top10.setAdapter(searchResultAdapter);
                                            for(int j=1;j<11;j++) {
                                                output.add(list1.get(list1.size()-j).getKey()+": "+list1.get(list1.size()-j).getValue());
                                                output1.add(list1.get(list1.size()-j).getKey());
                                            }
                                        }
                                    }
                                });
                            }
                        });

                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}