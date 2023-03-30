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
import com.example.scavengingscannables.Player;
import com.example.scavengingscannables.QrCode;
import com.example.scavengingscannables.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows user to search for other players
 */
public class FindUserActivity extends AppCompatActivity {
    private Button btnSearch;
    private Button btnBack;
    private Button btnTotal;
    private Button btnHighest;
    private EditText searchInput;
    private ListView searchResult;
    private String target;
    private String highest;
    private final HashMap<String,Integer> lowestHighest = new HashMap<>();
    private ArrayList<String> qrCodes = new ArrayList<>();
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
        btnTotal = findViewById(R.id.top_total);
        btnHighest = findViewById(R.id.top_highest);
        searchInput = findViewById(R.id.search_box_input);
        btnTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        allUsernames = (ArrayList<String>) data;
                        Log.d("SEARCH1", String.valueOf(allUsernames));
                        for (int i = 0; i < allUsernames.size(); i++) {
                            int finalI1 = i;
                            dbc.GetPlayerByUsername(allUsernames.get(i), new FirestoreDatabaseCallback() {
                                @Override
                                public <T> void OnDataCallback(T data) {
                                    Player p = (Player) data;
                                    lowestHighest.put(allUsernames.get(finalI1),Integer.valueOf(p.getTotal()));
                                    List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(lowestHighest.entrySet());
                                    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
                                        @Override
                                        public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                            return o1.getValue().compareTo(o2.getValue());
                                        }
                                    });
                                    if(list.size() <= 10){
                                        output.clear();
                                        searchResult.setAdapter(searchResultAdapter);
                                        searchResultAdapter.clear();
                                        for(int j=1;j<list.size()+1;j++){
                                            output.add(list.get(list.size()-j).getKey());
                                        }
                                    }else{
                                        output.clear();
                                        searchResult.setAdapter(searchResultAdapter);
                                        searchResultAdapter.clear();
                                        for(int j=1;j<11;j++) {
                                            output.add(list.get(list.size() - j).getKey());
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
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
        btnHighest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        allUsernames = (ArrayList<String>) data;
                        Log.d("SEARCH2", String.valueOf(allUsernames));
                        for (int i = 0; i < allUsernames.size(); i++){
                            int finalI2 = i;
                            dbc.GetPlayerByUsername(allUsernames.get(i), new FirestoreDatabaseCallback() {
                                @Override
                                public <T> void OnDataCallback(T data) {
                                    Player p = (Player) data;
                                    highest = p.getHighest();
                                    dbc.GetQRCodeByID(highest, new FirestoreDatabaseCallback() {
                                        @Override
                                        public <T> void OnDataCallback(T data) {
                                            QrCode q = (QrCode) data;
                                            lowestHighest.put(allUsernames.get(finalI2),Integer.valueOf(q.getScore()));
                                            List<Map.Entry<String, Integer>> list1 = new ArrayList<Map.Entry<String, Integer>>(lowestHighest.entrySet());
                                            Collections.sort(list1, new Comparator<Map.Entry<String, Integer>>() {
                                                @Override
                                                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                                                    return o1.getValue().compareTo(o2.getValue());
                                                }
                                            });
                                            if(list1.size() <= 10){
                                                output.clear();
                                                searchResult.setAdapter(searchResultAdapter);
                                                searchResultAdapter.clear();
                                                for(int j=1;j<list1.size()+1;j++){
                                                    output.add(list1.get(list1.size()-j).getKey());
                                                }
                                            }else{
                                                output.clear();
                                                searchResult.setAdapter(searchResultAdapter);
                                                searchResultAdapter.clear();
                                                for(int j=1;j<11;j++) {
                                                    output.add(list1.get(list1.size() - j).getKey());
                                                }
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }
                });
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbc.GetAllUsernames(new FirestoreDatabaseCallback() {
                    @Override
                    public <T> void OnDataCallback(T data) {
                        allUsernames = (ArrayList<String>) data;
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
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}