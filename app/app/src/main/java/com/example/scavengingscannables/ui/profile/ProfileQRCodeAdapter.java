package com.example.scavengingscannables.ui.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QRCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.map.DetailQRCodeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter to display qrcodes in the grid
 */
public class ProfileQRCodeAdapter extends RecyclerView.Adapter<ProfileQRCodeAdapter.ViewHolder> {

    private final ArrayList<QRCode> QRCodes;
    private final String username;
    private final ProfileQRCodeAdapter profileQRCodeAdapter;

    private ProfileDeleteQRCodeCallback callback = null;

    public ProfileQRCodeAdapter(ArrayList<QRCode> QRCodes, String username) {
        this.QRCodes = QRCodes;
        this.username = username;
        this.profileQRCodeAdapter = this;
    }

    public void setCallback(ProfileDeleteQRCodeCallback callback){
        this.callback = callback;
    }

    @NonNull
    @Override
    public ProfileQRCodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_qrcode_layout, parent, false);
        return new ProfileQRCodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileQRCodeAdapter.ViewHolder viewHolder, int position) {
        QRCode qrCode = this.QRCodes.get(position);
        Picasso.get().load(qrCode.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(viewHolder.getImageView());
        //viewHolder.getImageView().setContentDescription(qrCode.getqrId());
        viewHolder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailQRCodeActivity.class);
                intent.putExtra("qrID", qrCode.getqrId());
                view.getContext().startActivity(intent);
            }
        });

        //define a alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.getImageView().getContext());
        builder.setMessage("Do you want to delete?");
        builder.setTitle("Warning!");
        builder.setCancelable(false);
        builder.setPositiveButton("no", (dialog, which) -> {
            dialog.cancel();
        });
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        builder.setNegativeButton("yes", (dialog, which) -> {
            QRCodes.remove(qrCode);
            dbc.getPlayerByUsername(username, new FirestoreDatabaseCallback() {
                @Override
                public <T> void OnDataCallback(T data) {
                    Player p = (Player) data;
                    p.RemoveQRCodeByID(qrCode.getqrId());
                    qrCode.RemoveOwnedBy(username);
                    dbc.saveQRCodeByID(qrCode);
                    dbc.savePlayerByUsername(p, new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            if (callback != null){
                                callback.onDeleteQRCode();
                            }
                        }
                    });
                    profileQRCodeAdapter.notifyDataSetChanged();
                }
            });
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        SharedPreferences sharedPref = viewHolder.getImageView().getContext().getSharedPreferences("account", Context.MODE_PRIVATE);
        String thisUsername = sharedPref.getString("username", " ");

        Log.d("LOG", this.username);
        if (this.username == thisUsername){
            // same account
            viewHolder.getImageView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    alertDialog.show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.QRCodes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imageView;

        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.profile_qr_code_image);
        }

        public ImageView getImageView(){
            return imageView;
        }
    }
}
