package com.example.scavengingscannables.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.scavengingscannables.FirestoreDatabaseCallback;
import com.example.scavengingscannables.FirestoreDatabaseController;
import com.example.scavengingscannables.R;

import java.util.ArrayList;

/**
 * Allows user to search for other players
 */
public class FindUserActivity extends AppCompatActivity {
    private Button btnSearch;
    private Button btnBack;
    private EditText searchInput;
    private ListView searchResult;
    private String target;
    FirestoreDatabaseController dbc = new FirestoreDatabaseController();
    ArrayList<String> allUsernames = new ArrayList<>();
    ArrayList<String> output = new ArrayList<>();

    ArrayAdapter<String> searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        searchResultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, output);
        searchResult = findViewById(R.id.search_result_list);
        btnSearch = findViewById(R.id.search_button2);
        btnBack = findViewById(R.id.search_back_button);
        searchInput = findViewById(R.id.search_box_input);
        dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
            @Override
            public <T> void OnDataCallback(T data) {
                allUsernames = (ArrayList<String>) data;
                Log.d("SEARCH", String.valueOf(allUsernames));
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchResult.setAdapter(searchResultAdapter);
                searchResultAdapter.clear();
                String input = searchInput.getText().toString();
                if(input.length() > 0){
                    for (int i = 0; i < allUsernames.size(); i++){
                        if(allUsernames.get(i).contains(input) ){
                            output.add(allUsernames.get(i));
                        }
                    }
                }else{
                    Toast.makeText(FindUserActivity.this,"Please input a name", Toast.LENGTH_SHORT).show();
                }

                searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}