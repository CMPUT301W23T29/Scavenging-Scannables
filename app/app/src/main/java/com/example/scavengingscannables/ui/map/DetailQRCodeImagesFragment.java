package com.example.scavengingscannables.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.QRCodeImageLocationInfo;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.DetailQrcodeLocationsBinding;
import com.example.scavengingscannables.databinding.DetailQrcodeOthersBinding;
import com.example.scavengingscannables.ui.search.SearchResultAdapter;

import java.util.ArrayList;

public class DetailQRCodeImagesFragment extends Fragment {
    private ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfos = null;
    private RecyclerView othersView;

    DetailQrcodeLocationsBinding binding;

    public DetailQRCodeImagesFragment() {
        // Required empty public constructor
    }

    public DetailQRCodeImagesFragment(ArrayList<QRCodeImageLocationInfo> qrCodeImageLocationInfos) {
        this.qrCodeImageLocationInfos = qrCodeImageLocationInfos;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_qrcode_others, container, false);

        othersView = view.findViewById(R.id.display_others_recyclerview);

        DetailQRCodeImageAdapter adapter = new DetailQRCodeImageAdapter(qrCodeImageLocationInfos);

        othersView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        othersView.setLayoutManager(linearLayoutManager);

        binding = DetailQrcodeLocationsBinding.inflate(inflater, container, false);

        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.getRoot().requestLayout();
    }

}
