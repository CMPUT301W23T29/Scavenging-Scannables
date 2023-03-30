package com.example.scavengingscannables.ui.map;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.CommentsActivity;
import com.example.scavengingscannables.ui.profile.OthersWhoScannedQrCodeActivity;
import com.squareup.picasso.Picasso;


public class DetailQrCode extends AppCompatActivity {

    String id;
    Button back;
    TextView name;
    TextView score;
    TextView comment;
    TextView other;

    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);
        Intent intent = getIntent();
        id = intent.getStringExtra("qrID");
        back = findViewById(R.id.qrcode_back);
        name = findViewById(R.id.qrcode_name);
        score = findViewById(R.id.qrcode_score);
        comment = findViewById(R.id.qrcode_comment);
        image = findViewById(R.id.image_qrcode);
        other = findViewById(R.id.qrcode_other);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetQRCodeByID(id, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                QrCode q= (QrCode) data;
                name.setText("Name: "+q.getNameText());
                score.setText("Score: "+q.getScore());
                Picasso.get().load(q.getVisualLink()).into(image);
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailQrCode.this, CommentsActivity.class);
                intent.putExtra("QrCodeID", id);
                startActivity(intent);
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailQrCode.this, OthersWhoScannedQrCodeActivity.class);
                intent.putExtra("QrCodeID", id);
                startActivity(intent);
            }
        });




    }
}
