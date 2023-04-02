package com.example.scavengingscannables.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.DetailQrcodeLocationsBinding;
import com.example.scavengingscannables.databinding.DetailQrcodeOthersBinding;
import com.example.scavengingscannables.ui.search.SearchResultAdapter;

import java.util.ArrayList;

public class DetailQRCodeOthersFragment extends Fragment {
    private ArrayList<String> usernames = null;
    private RecyclerView othersView;
    DetailQrcodeOthersBinding binding;

    public DetailQRCodeOthersFragment() {
        // Required empty public constructor
    }

    public DetailQRCodeOthersFragment(ArrayList<String> usernames){
        this.usernames = usernames;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_qrcode_others, container, false);
        othersView = view.findViewById(R.id.display_others_recyclerview);
        // reusing search adapter because its the same thing
        SearchResultAdapter adapter = new SearchResultAdapter(usernames);
        othersView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        othersView.setLayoutManager(linearLayoutManager);
        binding = DetailQrcodeOthersBinding.inflate(inflater, container, false);
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.getRoot().requestLayout();
    }
}
