package com.example.scavengingscannables;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.hash.Hashing;
import com.google.zxing.Result;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ScannerFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private int CAMERA_PERMISSION_CODE = 1;
    private FirestoreDatabaseController fdc = new FirestoreDatabaseController();
    private ScoringSystem scrsys = new ScoringSystem();
    private QRCodeHandler qrch;
    private int score;
    private String sha256hex;
    private Bitmap image = null;
    private HashMap<String, Double> locationMap = new HashMap<>();
    private FusedLocationProviderClient flpc;
    private String username;
    ActivityResultLauncher<Intent> someActivityResultLauncher =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // Get the image from the camera activity
                        Intent data = result.getData();
                        image = (Bitmap) data.getExtras().get("data");
                        System.out.println(image.toString());

                        // Create new QRCodeHandler to handle our QR code
                        // Pass in everything we'll need to create a new QR code and save it to the database
                        qrch = new QRCodeHandler(getActivity(), sha256hex, score, fdc, locationMap, image, username);

                        fdc.CheckQRIDExists(sha256hex, qrch);
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();

        flpc = LocationServices.getFusedLocationProviderClient(getActivity());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");

        View root = inflater.inflate(R.layout.scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        // Generate a SHA-256 hash of the QR code text
                        sha256hex = Hashing.sha256()
                                .hashString(result.getText(), StandardCharsets.UTF_8)
                                .toString();

                        // Generate a score for the hash
                        score = scrsys.generateScore(sha256hex);

                        // Tell the user what the score of the QR code they scanned was
                        Toast.makeText(getActivity(), "Your score is: " + score,Toast.LENGTH_SHORT).show();

                        askForPhoto();
                        askLocationPermissions();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestCameraPermission();
        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void askForPhoto() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to store an image of the object you just scanned?")

                // If the user decides they want to store and image, we will launch their phone's camera application
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start the activity that launches the camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        someActivityResultLauncher.launch(intent);
                    }
                })
                // If the user decides they do not want to store the image
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Create new QRCodeHandler to handle our QR code
                        // Pass in everything we would need to potentially create a new QR code and save it to the database
                        qrch = new QRCodeHandler(getActivity(), sha256hex, score, fdc, locationMap, image, username);

                        // Check if the QR code we scanned already exists in the database
                        fdc.CheckQRIDExists(sha256hex, qrch);
                    }
                })
                .create().show();
    }

    private void askLocationPermissions() {
        // Here we ask the user if they want to store the object's location
        new AlertDialog.Builder(getActivity())
                .setTitle("Do you want to store this code's location?")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                           getLocation();
                            flpc.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    locationMap.put("latitude", location.getLatitude());
                                    locationMap.put("longitude", location.getLongitude());
                                }
                            });
                        }
                        else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                        }
                    }
                })

                // If the user does not want to store the location, we set the "storeLocation" flag to false
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationMap.put("latitude", 0.0);
                        locationMap.put("longitude", 0.0);
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
