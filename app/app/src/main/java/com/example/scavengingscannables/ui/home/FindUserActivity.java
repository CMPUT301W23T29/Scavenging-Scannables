package com.example.scavengingscannables.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {
    private Button btn_search;
    private EditText searchinput;
    private ListView searchresult;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        searchresult = (ListView) findViewById(R.id.search_result_list);
        btn_search = (Button) findViewById(R.id.search_button2);
        searchinput = (EditText) findViewById(R.id.search_box_input);
        db.collection("root_collection")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                list.add(document.getId());
                            }
                            Log.d("SEARCH",list.toString());
                        }else {
                            Log.d("SEARCH","Error getting document: ",task.getException());
                        }
                    }
                });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = searchinput.getText().toString();
            }
        });
    }
}