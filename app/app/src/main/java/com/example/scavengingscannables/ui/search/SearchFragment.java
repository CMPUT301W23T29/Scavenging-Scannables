package com.example.scavengingscannables.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.FragmentSearchBinding;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, FirestoreDatabaseCallback{
    private FragmentSearchBinding binding;
    private ArrayList<String> usernames = new ArrayList<>();
    private final ArrayList<String> searchResults = new ArrayList<>();
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

        RecyclerView searchResultView = root.findViewById(R.id.recycler_view);
        searchResultAdapter = new SearchResultAdapter(this.searchResults);

        searchResultView.setAdapter(searchResultAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        searchResultView.setLayoutManager(linearLayoutManager);

        searchView.setQuery("", false);

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
        this.updateSearchMatches(s);
        return false;
    }

    @Override
    public <T> void OnDataCallback(T data) {
        this.usernames = (ArrayList<String>) data;
    }

    private void updateSearchMatches(String s){
        this.searchResults.clear();
        if (!s.equals("")) {
            for (String username : this.usernames) {
                if (username.contains(s)) {
                    this.searchResults.add(username);
                }
            }
        }
        // removes current user
        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", " ");
        searchResults.remove(username);
        this.searchResultAdapter.notifyDataSetChanged();
        Log.d("LOG", "DSADSASA" + s);
    }
}
