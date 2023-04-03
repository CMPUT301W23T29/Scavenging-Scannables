package com.example.scavengingscannables.ui.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.databinding.DetailQrcodeCommentsBinding;
import com.example.scavengingscannables.ui.profile.Comment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Fragment to display qrcode comments
 */
public class DetailQRCodeCommentFragment extends Fragment {
    private ArrayList<Comment> comments;
    private RecyclerView commentsView;
    private String qrID;

    DetailQrcodeCommentsBinding binding;
    public DetailQRCodeCommentFragment() {
        // Required empty public constructor
    }

    public DetailQRCodeCommentFragment(ArrayList<Comment> comments, String qrID){
        this.comments = comments;
        this.qrID = qrID;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_qrcode_comments, container, false);

        commentsView = view.findViewById(R.id.display_comments_recyclerview);

        DetailQRCodeCommentAdapter adapter = new DetailQRCodeCommentAdapter(comments);

        commentsView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        commentsView.setLayoutManager(linearLayoutManager);

        EditText commentTextView = view.findViewById(R.id.comment_field);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageButton commentSubmitButton = view.findViewById(R.id.comment_submit_button);

        commentSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirestoreDatabaseController dbc = new FirestoreDatabaseController();
                String input = commentTextView.getText().toString();
                dbc.GetQRCodeByID(qrID, new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        QrCode q = (QrCode) data;
                        HashMap<String,String> currentComments = q.getComments();

                        SharedPreferences sharedPref = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
                        String username = sharedPref.getString("username", "ERROR NO USERNAME FOUND");

                        currentComments.put(username, input);
                        commentTextView.setText("");
                        q.setComments(currentComments);
                        dbc.SaveQRCodeByID(q);
                        dbc.GetQRCodeByID(qrID, new FirestoreDatabaseCallback() {
                            @Override
                            public <T> void OnDataCallback(T data) {
                                comments.clear();
                                QrCode q = (QrCode) data;

                                ArrayList<Comment> commentArrayList = new ArrayList<>();
                                q.getComments().forEach((key, value) -> {
                                    Comment comment = new Comment(key, value);
                                    comments.add(comment);
                                });

                                adapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
            }
        });

        binding = DetailQrcodeCommentsBinding.inflate(inflater, container, false);

        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.getRoot().requestLayout();
    }
}
