package com.example.scavengingscannables.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QRCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Home fragment, which will host the leaderboards in the future
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private String name;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    ArrayList<String> allUsernames = new ArrayList<>();
    private RecyclerView top10LeaderboardRecyclerView;
    private Top10LeaderboardAdapter top10LeaderboardAdapter;
    private final ArrayList<Player> allPlayers = new ArrayList<>();
    private final ArrayList<Player> top10Players = new ArrayList<>();
    private RecyclerView top10QRCodeLeaderboardRecyclerView;
    private Top10QRCodeLeaderboardAdapter top10QRCodeLeaderboardAdapter;
    private final ArrayList<QRCode> allQRCodes = new ArrayList<>();
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
        dbc.getAllQRCodesOneByOneWithConfirmation(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Pair<QRCode, Boolean> dataPair = (Pair<QRCode, Boolean>) data;

                QRCode qrCode = dataPair.first;
                Boolean isFinal = dataPair.second;
                allQRCodes.add(qrCode);
                allQRCodes.sort(new Comparator<QRCode>() {
                    @Override
                    public int compare(QRCode q1, QRCode q2) {
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
        dbc.getAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                allUsernames = (ArrayList<String>) data;
                for (String username:allUsernames) {
                    dbc.getPlayerByUsername(username, new FirestoreDatabaseCallback() {
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}