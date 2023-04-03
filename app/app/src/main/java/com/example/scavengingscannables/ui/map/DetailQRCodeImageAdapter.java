package com.example.scavengingscannables.ui.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.QRCodeImageLocationInfo;
import com.example.scavengingscannables.R;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class DetailQRCodeImageAdapter extends RecyclerView.Adapter<DetailQRCodeImageAdapter.ViewHolder>{
    private final ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfos;

    public DetailQRCodeImageAdapter(ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfos) {
        this.qrCodeImageLocationInfos = qrCodeImageLocationInfos;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.detail_qrcode_coordinate);
            imageView = view.findViewById(R.id.detail_qrcode_imageview);
        }

        public TextView getTextView(){
            return textView;
        }
        public ImageView getImageView(){
            return imageView;
        }
    }

    @NonNull
    @Override
    public DetailQRCodeImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_qrcode_locations_adapter_layout, parent, false);
        return new DetailQRCodeImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailQRCodeImageAdapter.ViewHolder viewHolder, int position) {
        QRCodeImageLocationInfo qrCodeImageLocationInfo = this.qrCodeImageLocationInfos.get(position);

        if (qrCodeImageLocationInfo.getIsLocationPrivate()){
            viewHolder.getTextView().setText("This location is private.");
        }else{
            GeoPoint gp = qrCodeImageLocationInfo.getImageLocation();
            String latLongString = String.format("(%s, %s)", gp.getLatitude(), gp.getLongitude());
            viewHolder.getTextView().setText(latLongString);
        }

        if (qrCodeImageLocationInfo.getIsImagePrivate()){
            viewHolder.getImageView().setImageResource(R.drawable.ic_question_mark_black_24dp);
        }else{
            viewHolder.getImageView().setImageBitmap(qrCodeImageLocationInfo.convertBase64ToImage());
        }
    }

    @Override
    public int getItemCount() {
        return qrCodeImageLocationInfos.size();
    }
}
