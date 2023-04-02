package com.example.scavengingscannables.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private ArrayList<String> usernames;

    public SearchResultAdapter(ArrayList<String> usernames){
        this.usernames = usernames;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.playername_text);
        }

        public TextView getTextView(){
            return textView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playername_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.getTextView().setText(this.usernames.get(position));
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }
}
