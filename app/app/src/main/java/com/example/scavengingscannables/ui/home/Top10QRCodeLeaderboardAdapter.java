package com.example.scavengingscannables.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.QRCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.map.DetailQrCode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for top 10 qrcode leaderboard
 */
public class Top10QRCodeLeaderboardAdapter extends RecyclerView.Adapter<Top10QRCodeLeaderboardAdapter.ViewHolder>{
    private final ArrayList<QRCode> QRCodes;

    public Top10QRCodeLeaderboardAdapter(ArrayList<QRCode> QRCodes){
        this.QRCodes = QRCodes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView positionText;
        private final ImageView imageView;
        private final TextView nameText;
        private final TextView scoreText;
        private String qrID;
        public ViewHolder(View view){
            super(view);
            positionText = view.findViewById(R.id.qrcode_leaderboard_position);
            nameText = view.findViewById(R.id.top10_leaderboard_qrcode_name);
            scoreText = view.findViewById(R.id.top10_leaderboard_qrcode_score);
            imageView = view.findViewById(R.id.top10_leaderboard_qrcode_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailQrCode.class);
                    intent.putExtra("qrID", qrID);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void setQrID(String qrID) {
            this.qrID = qrID;
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

        public ImageView getImageView() {
            return imageView;
        }
    }
        @NonNull
    @Override
    public Top10QRCodeLeaderboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top10_qrcode_leaderboard_adapter_layout, parent, false);
        return new Top10QRCodeLeaderboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Top10QRCodeLeaderboardAdapter.ViewHolder viewHolder, int position) {
        QRCode qrCode = this.QRCodes.get(position);
        viewHolder.getPositionText().setText("#" + (position + 1));
        viewHolder.getNameText().setText(qrCode.getNameText());
        viewHolder.getScoreText().setText(qrCode.getScore());
        Picasso.get().load(qrCode.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(viewHolder.getImageView());
        viewHolder.setQrID(qrCode.getqrId());
    }

    @Override
    public int getItemCount() {
        return QRCodes.size();
    }
}
