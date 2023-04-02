package com.example.scavengingscannables.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentMapBinding;
import com.example.scavengingscannables.databinding.FragmentSearchBinding;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, FirestoreDatabaseCallback{
    private FragmentSearchBinding binding;
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> searchResults = new ArrayList<>();
    private RecyclerView searchResultView;
    private SearchResultAdapter searchResultAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d("LOG", "ENTERED SEARCH FRAGMENT");

        FirestoreDatabaseController dbc = new FirestoreDatabaseController();
        dbc.GetAllUsernames(this);

        // sets listeners
        SearchView searchView = root.findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(this);

        searchResultView = root.findViewById(R.id.recycler_view);
        searchResultAdapter = new SearchResultAdapter(this.searchResults);
        searchResultView.setAdapter(searchResultAdapter);
        searchResultView.lay

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        this.searchResults.clear();
        for (String username: this.usernames) {
            if (username.contains(s)){
                this.searchResults.add(username);
            }
        }
        this.searchResultAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public <T> void OnDataCallback(T data) {
        this.usernames = (ArrayList<String>) data;
    }
}
