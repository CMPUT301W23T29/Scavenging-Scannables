package com.example.scavengingscannables.ui.notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentCustomerArrayAdapter extends ArrayAdapter {
    private HashMap<String,String> comments;
    private Context context;

    public CommentCustomerArrayAdapter(@NonNull Context context) {
        super(context, 0);
        this.context = context;
        this.comments = new HashMap<String,String>();
    }



    @Override
    public View getView(int position, View currentItemView, ViewGroup parent) {
        final View result;

        if (currentItemView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        } else {
            result = currentItemView;
        }


        TextView name = currentItemView.findViewById(R.id.comment_user);
        name.setText("User: "+NotificationsFragment.name);

        TextView comment = currentItemView.findViewById(R.id.comment);
        comment.setText("Comment: "+comments.get(NotificationsFragment.name));

        return result;
    }
}