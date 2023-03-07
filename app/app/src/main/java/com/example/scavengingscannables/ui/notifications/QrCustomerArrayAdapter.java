package com.example.scavengingscannables.ui.notifications;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;


public class QrCustomerArrayAdapter extends ArrayAdapter<QrCode>{

    private ArrayList<QrCode> qrCodes;
    private Context context;


    public QrCustomerArrayAdapter(@NonNull Context context, ArrayList<QrCode> qrCodes) {
        super(context, 0, qrCodes);
        this.context=context;
        this.qrCodes = qrCodes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;


        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.qrcode, parent, false);

        }
        QrCode qrcode = qrCodes.get(position);

        ImageView image = currentItemView.findViewById(R.id.codeImage);
        assert image != null;
        image.setImageResource(qrcode.getQrId());


        TextView name = currentItemView.findViewById(R.id.codeName);
        name.setText("Name: "+qrcode.getQrName());

        TextView score = currentItemView.findViewById(R.id.codeScore);
        score.setText("Score: "+qrcode.getScore());

        TextView comment = currentItemView.findViewById(R.id.comment);
        TextView others = currentItemView.findViewById(R.id.others);


        //Click comment to see comment of others
        comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Comment Clicked");
                //TODO
            }
        });

        //Click others to see who also add this qrcode
        others.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("others Clicked");
                //TODO
            }
        });

        // then return the recyclable view
        return currentItemView;
    }
}