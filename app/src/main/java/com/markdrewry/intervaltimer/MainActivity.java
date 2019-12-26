package com.markdrewry.intervaltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton addTimer;
    private ArrayList<TimerObj> timers;
    private ListView timerOptions;
    private TimerListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ui objects declared
        setContentView(R.layout.activity_main);
        addTimer = findViewById(R.id.addTimer);
        timerOptions = findViewById(R.id.timerListview);
        populateListView();
        //buttons for navigation
        timerOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,timer.class);
                intent.putExtra("selectedAlarm",timers.get(position));
                Log.d("IntervalTimer","Alarm selected: "+timers.get(position).getName());
                startActivity(intent);
            }
        });
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addTimer.class);
                startActivity(intent);
            }
        });

    }
    private void populateListView(){
        getList();
        adapter = new TimerListAdapter(this,timers);
        timerOptions.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        storeList();
    }
    private void getList(){
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("timers",null);
        Type type = new TypeToken<ArrayList<TimerObj>>() {}.getType();
        timers = gson.fromJson(json,type);
        if(timers == null){
            timers = new ArrayList<TimerObj>();
        }
    }
    private void storeList(){
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(timers);
        editor.putString("timers",json);
        editor.apply();
    }
}
