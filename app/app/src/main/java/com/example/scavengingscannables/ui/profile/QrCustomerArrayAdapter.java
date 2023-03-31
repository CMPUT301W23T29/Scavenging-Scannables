package com.example.scavengingscannables.ui.profile;

import android.content.Intent;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.squareup.picasso.Picasso;

/**
 * Custom array adapter to show information about qr codes in ListViews
 */
public class QrCustomerArrayAdapter extends ArrayAdapter<QrCode>{

    private final ArrayList<QrCode> qrCodes;
    private final Context context;

    private ImageView qrImage;


    public QrCustomerArrayAdapter(@NonNull Context context, ArrayList<QrCode> qrCodes) {
        super(context, 0, qrCodes);
        this.context=context;
        this.qrCodes = qrCodes;
    }

    public QrCustomerArrayAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        this.qrCodes = new ArrayList<QrCode>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;


        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.qrcode, parent, false);

        }
        QrCode qrcode = super.getItem(position);

        TextView name = currentItemView.findViewById(R.id.codeName);
        name.setText("Name: "+qrcode.getNameText());

        TextView score = currentItemView.findViewById(R.id.codeScore);
        score.setText("Score: "+qrcode.getScore());

        TextView comment = currentItemView.findViewById(R.id.comment);
        TextView others = currentItemView.findViewById(R.id.others);
        TextView location = currentItemView.findViewById(R.id.location_info);


        //Click comment to see comment of others
        comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Comment Clicked");
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("QrCodeID", qrcode.getqrId());
                context.startActivity(intent);
            }
        });

        //Click others to see who also add this qrcode
        others.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("others Clicked");
                Intent intent = new Intent(context, OthersWhoScannedQrCodeActivity.class);
                intent.putExtra("QrCodeID", qrcode.getqrId());
                context.startActivity(intent);
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("location Clicked");
                Intent intent = new Intent(context, LocationInfoActivity.class);
                intent.putExtra("QrCodeID", qrcode.getqrId());
                context.startActivity(intent);
            }
        });

        // display image
        qrImage = currentItemView.findViewById(R.id.codeImage);
        Picasso.get().load(qrcode.getVisualLink()).into(qrImage);

        // then return the recyclable view
        return currentItemView;
    }
}