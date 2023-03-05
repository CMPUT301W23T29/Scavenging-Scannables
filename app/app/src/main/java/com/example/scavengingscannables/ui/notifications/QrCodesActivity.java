package com.example.scavengingscannables.ui.notifications;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class QrCodesActivity extends AppCompatActivity {
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qrcodes);
        backButton = findViewById(R.id.button_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        ArrayList<QrCode> arrayList = new ArrayList<QrCode>();
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "pp","1","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "cc", "Two","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "3", "Three","1"));
        arrayList.add(new QrCode(R.drawable.ic_home_black_24dp, "4", "Four","1"));

        QrCustomerArrayAdapter QrAdapter = new QrCustomerArrayAdapter(this, arrayList);

        ListView qrCodesListView = findViewById(R.id.qrcode_list);

        qrCodesListView.setAdapter(QrAdapter);





    }
}
