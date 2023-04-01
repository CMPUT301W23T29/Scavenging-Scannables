package com.example.scavengingscannables.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QRCodeImageLocationInfo;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentMapBinding;
import com.example.scavengingscannables.ui.profile.QrCodesActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Fragment for the map view to locate nearby qr codes in the future
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private FragmentMapBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // checks for location permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("LOG", "NO LOCATION PERMISSIONS");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        // creates the google map
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class MapCustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{
        private Boolean hasImage;

        public MapCustomInfoWindowAdapter() {
            this.hasImage = false;
        }

        @Nullable
        @Override
        public View getInfoContents(@NonNull Marker marker) {
            View infoView = getLayoutInflater().inflate(R.layout.map_custom_info_window_content, null);
            QrCode qrCode = (QrCode) marker.getTag();
            if (!hasImage){
                Picasso.get().load(qrCode.getVisualLink()).placeholder(R.drawable.ic_question_mark_black_24dp).into(((ImageView) infoView.findViewById(R.id.info_window_image)), new Callback() {
                    @Override
                    public void onSuccess() {
                        hasImage = true;
                        marker.showInfoWindow();
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println(e);
                    }
                });
            }else{
                Picasso.get().load(qrCode.getVisualLink()).into(((ImageView) infoView.findViewById(R.id.info_window_image)));
            }
            ((TextView) infoView.findViewById(R.id.info_window_title)).setText(marker.getTitle());
            ((TextView) infoView.findViewById(R.id.info_window_score)).setText(marker.getSnippet());
            return infoView;
        }

        @Nullable
        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            return null;
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // gets current location and zooms into map
        // sometimes has a problem where doesn't zoom right away after granting permissions
        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("LOG", "SUCCESS GETTING LOCATION");
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            }
        });

        // populate map with markers of qrcodes
        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetAllQRCodesOneByOne(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                QrCode qrCode = (QrCode) data;
                String qrName = qrCode.getNameText();
                qrCode.getQrCodeImageLocationInfoList();
                if (qrCode.getQrCodeImageLocationInfoList() != null) {
                    for (QRCodeImageLocationInfo qrCodeImageLocationInfo : qrCode.getQrCodeImageLocationInfoList()) {
                        if (qrCodeImageLocationInfo != null) {
                            Log.d("LOG", qrCode.getqrId());
                            if (!qrCodeImageLocationInfo.getIsLocationPrivate()) {
                                GeoPoint qrGeopoint = qrCodeImageLocationInfo.getImageLocation();
                                LatLng qrLatLng = new LatLng(qrGeopoint.getLatitude(), qrGeopoint.getLongitude());
                                Marker qrMarker = mMap.addMarker(new MarkerOptions()
                                        .position(qrLatLng)
                                        .title(qrName)
                                        .snippet(qrCode.getScore()));
                                qrMarker.setTag(qrCode);
                            }
                        }
                    }
                }

            }
        });

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        mMap.setInfoWindowAdapter(new MapCustomInfoWindowAdapter());
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        // change to load individual qr code in future
        QrCode qrCode = (QrCode) marker.getTag();
        Log.d("LOG", qrCode.getNameText());
        Intent intent = new Intent(getActivity(), DetailQrCode.class);
        intent.putExtra("qrID", qrCode.getqrId());
        startActivity(intent);
    }
}