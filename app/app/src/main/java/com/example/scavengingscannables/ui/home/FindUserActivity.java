package com.example.scavengingscannables.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.LoginActivity;
import com.example.scavengingscannables.MainActivity;
import com.example.scavengingscannables.R;
import com.example.scavengingscannables.ui.notifications.NotificationsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {
    private Button btn_search;
    private Button btn_back;
    private EditText searchinput;
    private ListView searchresult;
    private String target;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    ArrayList<String> allusername;
    ArrayList<String> output = new ArrayList<>();

    ArrayAdapter<String> searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        searchResultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, output);
        searchresult = (ListView) findViewById(R.id.search_result_list);
        btn_search = (Button) findViewById(R.id.search_button2);
        btn_back = (Button) findViewById(R.id.search_back_button);
        searchinput = (EditText) findViewById(R.id.search_box_input);
        dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                allusername = (ArrayList<String>) data;
                Log.d("SEARCH", String.valueOf(allusername));
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchresult.setAdapter(searchResultAdapter);
                searchResultAdapter.clear();
                String input = searchinput.getText().toString();
                if(input.length() > 0){
                    for (int i=0; i < allusername.size(); i++){
                        if(allusername.get(i).contains(input) ){
                            output.add(allusername.get(i));
                        }
                    }
                }else{
                    Toast.makeText(FindUserActivity.this,"Please input a name", Toast.LENGTH_SHORT).show();
                }

                searchresult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        target = output.get(i);
                        Intent intent = new Intent(FindUserActivity.this, DisplaySearch.class);
                        intent.putExtra("user",target);
                        startActivity(intent);
                    }
                });
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}