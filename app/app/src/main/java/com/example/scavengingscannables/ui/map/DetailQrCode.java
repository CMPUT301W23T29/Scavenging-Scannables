package com.example.scavengingscannables.ui.map;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.Comment;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Allows to open and view individual qr codes
 */
public class DetailQrCode extends AppCompatActivity {

    String id;
    TextView name;
    TextView score;
    ImageView image;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private QrCode qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);
        Intent intent = getIntent();
        id = intent.getStringExtra("qrID");
        name = findViewById(R.id.qrcode_name);
        score = findViewById(R.id.qrcode_score);
        image = findViewById(R.id.image_qrcode);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tablayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetQRCodeByID(id, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                QrCode q = (QrCode) data;
                qrCode = q;
                name.setText(q.getNameText());
                score.setText(q.getScore());
                Picasso.get().load(q.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(image);

                ArrayList<Comment> commentArrayList = new ArrayList<>();
                qrCode.getComments().forEach((key, value) -> {
                    Comment comment = new Comment(key, value);
                    commentArrayList.add(comment);
                });

                DetailQRCodeCommentFragment detailQRCodeCommentFragment = new DetailQRCodeCommentFragment(commentArrayList, qrCode.getqrId());
                DetailQRCodeImagesFragment detailQRCodeImagesFragment = new DetailQRCodeImagesFragment(qrCode.getQrCodeImageLocationInfoList());
                DetailQRCodeOthersFragment detailQRCodeOthersFragment = new DetailQRCodeOthersFragment(qrCode.getOwnedBy());
                FragmentStateAdapter adapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
                    @NonNull
                    @Override
                    public Fragment createFragment(int position) {
                        Fragment fragment = null;
                        switch (position) {
                            case 0:
                                fragment = detailQRCodeCommentFragment;
                                break;
                            case 1:
                                fragment = detailQRCodeOthersFragment;
                                break;
                            case 2:
                                fragment = detailQRCodeImagesFragment;
                                break;
                        }
                        return fragment;
                    }

                    @Override
                    public int getItemCount() {
                        return 3;
                    }
                };

                viewPager.setAdapter(adapter);
                viewPager.setUserInputEnabled(false);
                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                        viewPager.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        viewPager.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        super.onPageScrollStateChanged(state);
                        viewPager.getAdapter().notifyDataSetChanged();
                    }
                });
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                        viewPager.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }
}
