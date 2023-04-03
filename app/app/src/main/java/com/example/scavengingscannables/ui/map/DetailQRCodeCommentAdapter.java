package com.example.scavengingscannables.ui.map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.profile.Comment;

import java.util.ArrayList;

/**
 * Adapter for detailed qrcode comments
 */
public class DetailQRCodeCommentAdapter extends RecyclerView.Adapter<DetailQRCodeCommentAdapter.ViewHolder>{
    private final ArrayList<Comment> comments;

    public DetailQRCodeCommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView comment;

        public ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.detail_qrcode_comment_name);
            comment = view.findViewById(R.id.detail_qrcode_comment);
        }

        public TextView getName(){
            return name;
        }
        public TextView getComment(){
            return comment;
        }
    }

    @NonNull
    @Override
    public DetailQRCodeCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_qrcode_comment_layout, parent, false);
        return new DetailQRCodeCommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailQRCodeCommentAdapter.ViewHolder viewHolder, int position) {
        Comment comment = this.comments.get(position);
        viewHolder.getName().setText(comment.getName());
        viewHolder.getComment().setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }
}
