package com.example.scavengingscannables.ui.profile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.MainActivity;
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ScannerActivity;
import com.example.scavengingscannables.databinding.FragmentProfileBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment for the current player, shows information about the player and allows you to view their qr codes
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    Button viewQrCodes;

    Switch hideProfile;
    private TextView usernameView;
    private TextView phone;
    private TextView totalScanned;
    private TextView totalScore;
    private ImageView highest;
    private String highestId;
    private ImageView lowest;
    private String lowestId;
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
    String username;
    static String name;
    private Integer tScore = 0;
    private Integer tScanned = 0;
    private final ArrayList<String> scores = new ArrayList<>();
    private String userPhone;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    private final int CAMERA_PERMISSION_CODE = 1;

    private ArrayList<QrCode> playerQRCodes = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProfileQRCodeAdapter profileQRCodeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        hideProfile = root.findViewById(R.id.hide);
        phone = root.findViewById(R.id.phone);
        totalScanned = root.findViewById(R.id.codes_scanned);
        totalScore = root.findViewById(R.id.total_score);
        highest = root.findViewById(R.id.n_image_highest_qr);
        lowest = root.findViewById(R.id.n_image_lowest_qr);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        usernameView = root.findViewById(R.id.user_name);
        username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");
        usernameView.setText(username);
        name = username;

        //Change the hideProfile switch to the state in database
        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                Boolean b = p.getHide();
                hideProfile.setChecked(b);
            }
        });

        hideProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        Player p = (Player)data;
                        if (hideProfile.isChecked()) {
                            p.setHide(true);
                        }
                        else{
                            p.setHide(false);
                        }
                        dbc.SavePlayerByUsername(p);
                    }
                });

            }
        });

        this.recyclerView = root.findViewById(R.id.profile_recycler_view);
        this.profileQRCodeAdapter = new ProfileQRCodeAdapter(this.playerQRCodes, username);

        this.recyclerView.setAdapter(profileQRCodeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        dbc.GetPlayerByUsername(username, new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                Player p = (Player)data;
                userPhone = p.getPhoneNumber().toString();
                phone.setText(userPhone);
                qrCodes = p.getScannedQRCodesID();

                dbc.GetAllQrCodeOfUserOneByOne(username, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        QrCode qrCode = (QrCode) data;

                        playerQRCodes.add(qrCode);
                        profileQRCodeAdapter.notifyDataSetChanged();

                        lowestHighest.put(qrCode.getqrId(),Integer.valueOf(qrCode.getScore()));
                        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowestHighest.entrySet());
                        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                            @Override
                            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                return o1.getValue().compareTo(o2.getValue());
                            }
                        });
                        lowestId = list.get(0).getKey();
                        dbc.GetQRCodeByID(lowestId, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                QrCode ql = (QrCode)data;
                                Picasso.get().load(ql.getVisualLink()).into(lowest);
                            }
                        });
                        highestId = list.get((list.size())-1).getKey();
                        dbc.GetQRCodeByID(highestId, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                QrCode ql = (QrCode)data;
                                Picasso.get().load(ql.getVisualLink()).into(highest);
                            }
                        });

                        scores.add(qrCode.getScore());
                        tScore = 0;
                        for (int i=0;i<scores.size();i++) {
                            tScore += Integer.parseInt(scores.get(i));
                        }
                        totalScore.setText(tScore.toString());
                        tScanned = scores.size();
                        totalScanned.setText(tScanned.toString());
                    }
                });
            }
        });

        FloatingActionButton fab = root.findViewById(R.id.fab);
        // Attach QR scanner activity to FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for camera permission. If it has been granted, show confirmation. If not, request permission for it
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "You have already granted this permission!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    requestCameraPermission();
                }
            }
        });

        return root;
    }

    // Function to request camera permission from user
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("Permission is needed because of this and")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            requestPermissions(new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(getActivity(), "Hello2", Toast.LENGTH_SHORT).show();
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}