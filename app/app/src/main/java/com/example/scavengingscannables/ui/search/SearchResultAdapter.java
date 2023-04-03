package com.example.scavengingscannables.ui.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.OtherPlayerProfileActivity;

import java.util.ArrayList;

/**
 * Adapter for search results
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private final ArrayList<String> usernames;

    public SearchResultAdapter(ArrayList<String> usernames){
        this.usernames = usernames;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;

        public ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.search_result_text);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), OtherPlayerProfileActivity.class);
                    intent.putExtra("user", textView.getText());
                    view.getContext().startActivity(intent);
                }
            });
        }

        public TextView getTextView(){
            return textView;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_layout, parent, false);
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
