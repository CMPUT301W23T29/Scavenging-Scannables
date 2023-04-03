package com.example.scavengingscannables.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.OtherPlayerProfileActivity;

import java.util.ArrayList;

public class Top10LeaderboardAdapter extends RecyclerView.Adapter<Top10LeaderboardAdapter.ViewHolder> {
    private final ArrayList<Player> players;

    public Top10LeaderboardAdapter(ArrayList<Player> players){
        this.players = players;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView positionText;
        private final TextView nameText;
        private final TextView scoreText;
        public ViewHolder(View view){
            super(view);
            positionText = view.findViewById(R.id.leaderboard_position);
            nameText = view.findViewById(R.id.player_name);
            scoreText = view.findViewById(R.id.player_score);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), OtherPlayerProfileActivity.class);
                    intent.putExtra("user", nameText.getText());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public TextView getPositionText(){
            return positionText;
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getScoreText() {
            return scoreText;
        }
    }

    @NonNull
    @Override
    public Top10LeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top10_leaderboard_adapter_layout, parent, false);
        return new Top10LeaderboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Top10LeaderboardAdapter.ViewHolder viewHolder, int position) {
        viewHolder.getPositionText().setText("#" + (position + 1));
        viewHolder.getNameText().setText(this.players.get(position).getUsername());
        viewHolder.getScoreText().setText(this.players.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
