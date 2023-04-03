package com.example.scavengingscannables.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentHomeBinding;
import com.example.scavengingscannables.ui.profile.OtherPlayerProfileActivity;
import com.example.scavengingscannables.ui.search.SearchResultAdapter;
import com.google.zxing.qrcode.encoder.QRCode;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Home fragment, which will host the leaderboards in the future
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String name;
    private String highest;
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
    ArrayAdapter<String> searchResultAdapter;
    ArrayList<String> output = new ArrayList<>();
    ArrayList<String> output1 = new ArrayList<>();
    private RecyclerView top10LeaderboardRecyclerView;
    private Top10LeaderboardAdapter top10LeaderboardAdapter;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private ArrayList<Player> top10Players = new ArrayList<>();
    private RecyclerView top10QRCodeLeaderboardRecyclerView;
    private Top10QRCodeLeaderboardAdapter top10QRCodeLeaderboardAdapter;
    private ArrayList<QrCode> allQRCodes = new ArrayList<>();
    private ImageView top1;
    private ImageView top2;
    private ImageView top3;
    private TextView myRankingPosition;
    private TextView myRankingName;
    private TextView myRankingScore;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchResultAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, output);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        name = sharedPref.getString("username", "ERROR NO USERNAME FOUND");

        top10LeaderboardRecyclerView = root.findViewById(R.id.top10_leaderboard);
        top10LeaderboardAdapter = new Top10LeaderboardAdapter(this.top10Players);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        top10LeaderboardRecyclerView.setAdapter(top10LeaderboardAdapter);
        top10LeaderboardRecyclerView.setLayoutManager(linearLayoutManager);

        top10QRCodeLeaderboardRecyclerView = root.findViewById(R.id.top10_qrcode_leaderboard);
        top10QRCodeLeaderboardAdapter = new Top10QRCodeLeaderboardAdapter(this.allQRCodes);

        top10QRCodeLeaderboardRecyclerView.setAdapter(top10QRCodeLeaderboardAdapter);
        top10QRCodeLeaderboardRecyclerView.setLayoutManager(llm);

        top1 = root.findViewById(R.id.top1_qrcode_image);
        top2 = root.findViewById(R.id.top2_qrcode_image);
        top3 = root.findViewById(R.id.top3_qrcode_image);

        View myRankingView = root.findViewById(R.id.my_ranking);
        myRankingPosition = myRankingView.findViewById(R.id.leaderboard_position);
        myRankingName = myRankingView.findViewById(R.id.player_name);
        myRankingScore = myRankingView.findViewById(R.id.player_score);

        myRankingName.setText(name);

        allPlayers.clear();
        top10Players.clear();
        allQRCodes.clear();

        // populate top 10 qrcodes recycler view
        dbc.GetAllQRCodesOneByOneWithConfirmation(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Pair<QrCode, Boolean> dataPair = (Pair<QrCode, Boolean>) data;

                QrCode qrCode = dataPair.first;
                Boolean isFinal = dataPair.second;
                allQRCodes.add(qrCode);
                allQRCodes.sort(new Comparator<QrCode>() {
                    @Override
                    public int compare(QrCode q1, QrCode q2) {
                        return Integer.parseInt(q2.getScore()) - Integer.parseInt(q1.getScore());
                    }
                });
                while(allQRCodes.size() > 10){
                    allQRCodes.remove(allQRCodes.size() - 1);
                }
                top10QRCodeLeaderboardAdapter.notifyDataSetChanged();
                if (isFinal){
                    try{
                        Picasso.get().load(allQRCodes.get(0).getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(top1);
                        Picasso.get().load(allQRCodes.get(1).getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(top2);
                        Picasso.get().load(allQRCodes.get(2).getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(top3);
                    }catch (ArrayIndexOutOfBoundsException e){
                        // only populates the first 1 or 2
                    }
                }
            }
        });

        // populate top 10 players
        dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                allUsernames = (ArrayList<String>) data;
                for (String username:allUsernames) {
                    dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                        @Override
                        public <T> void OnDataCallback(T data) {
                            Player p = (Player) data;
                            top10Players.add(p);
                            allPlayers.add(p);
                            top10Players.sort(new Comparator<Player>() {
                                @Override
                                public int compare(Player player, Player t1) {
                                    return Integer.parseInt(t1.getTotal()) - Integer.parseInt(player.getTotal());
                                }
                            });
                            allPlayers.sort(new Comparator<Player>() {
                                @Override
                                public int compare(Player player, Player t1) {
                                    return Integer.parseInt(t1.getTotal()) - Integer.parseInt(player.getTotal());
                                }
                            });
                            while(top10Players.size() > 10){
                                top10Players.remove(top10Players.size() - 1);
                            }

                            for (Player pl:allPlayers) {
                                if(pl.getUsername().equals(name)){
                                    myRankingPosition.setText(String.valueOf(allPlayers.indexOf(pl) + 1));
                                    myRankingScore.setText(String.valueOf(pl.getTotal()));
                                }
                            }

                            top10LeaderboardAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        /**
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
        updateView();
         **/
        return root;
    }

    private void updateView(){
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}