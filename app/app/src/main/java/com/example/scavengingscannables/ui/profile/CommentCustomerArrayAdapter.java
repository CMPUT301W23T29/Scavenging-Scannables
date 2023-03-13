package com.example.scavengingscannables.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.scavengingscannables.R;

import java.util.ArrayList;

public class CommentCustomerArrayAdapter extends ArrayAdapter<Comment> {
    private final ArrayList<Comment> comments;
    private final Context context;

    public CommentCustomerArrayAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        this.comments = new ArrayList<Comment>();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);

        }
        Comment CurrentComment = super.getItem(position);



        TextView name = currentItemView.findViewById(R.id.comment_user);
        name.setText(CurrentComment.getName()+":");

        TextView comment = currentItemView.findViewById(R.id.comment);
        comment.setText("       "+CurrentComment.getComment());

        return currentItemView;
    }
}