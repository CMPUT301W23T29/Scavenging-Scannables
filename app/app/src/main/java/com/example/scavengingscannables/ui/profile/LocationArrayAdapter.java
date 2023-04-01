package com.example.scavengingscannables.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scavengingscannables.QRCodeImageLocationInfo;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LocationArrayAdapter extends ArrayAdapter<QRCodeImageLocationInfo> {

    private final ArrayList<QRCodeImageLocationInfo> locationInfos;
    private final Context context;

    private ImageView photo;

    public LocationArrayAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        this.locationInfos = new ArrayList<QRCodeImageLocationInfo>();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.locaton_item, parent, false);

        }
        QRCodeImageLocationInfo CurrentLocation = super.getItem(position);



        TextView latitude = currentItemView.findViewById(R.id.latitude);
        TextView longitude = currentItemView.findViewById(R.id.longitude);
        if(CurrentLocation.getIsLocationPrivate()){
            latitude.setText("Private location");
            longitude.setText("Private location");
        }
        else {
            latitude.setText("Latitude: " + CurrentLocation.getImageLocation().getLatitude());
            longitude.setText("Longitude: "+CurrentLocation.getImageLocation().getLongitude());
        }


        // display image
        photo = currentItemView.findViewById(R.id.photo);
        if(CurrentLocation.getIsImagePrivate()){
            photo.setImageResource(R.drawable.ic_question_mark_black_24dp);
        }
        else {

            photo.setImageBitmap(CurrentLocation.convertBase64ToImage());
        }

        return currentItemView;
    }
}
