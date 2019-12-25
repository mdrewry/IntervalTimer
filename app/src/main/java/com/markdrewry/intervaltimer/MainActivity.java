package com.markdrewry.intervaltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton addTimer;
    private ArrayList<TimerObj> timers;
    private ListView timerOptions;
    private TimerListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTimer = findViewById(R.id.addTimer);
        timerOptions = findViewById(R.id.timerListview);
        if(savedInstanceState!=null){
            timers = savedInstanceState.getParcelableArrayList("timers");
        }
        else{
            timers = new ArrayList<TimerObj>();
        }
        adapter = new TimerListAdapter(this,timers);
        timerOptions.setAdapter(adapter);
        timerOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,timer.class);
                intent.putExtra("timerSelected",timers.get(position));
            }
        });
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent onAdd = new Intent(MainActivity.this,addTimer.class);
                startActivity(onAdd);
            }
        });
    }
    private void populateListView(){

    }
    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if(intent.hasExtra("timer")) {
            Log.d("IntervalTimer", "not a problem getting timer");
            if(intent.hasExtra("timers"))
                timers = intent.getParcelableArrayListExtra("timers");

            TimerObj temp = intent.getParcelableExtra("timer");
            timers.add(temp);
            Log.d("IntervalTimer","Num of Timers: "+timers.size());
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("timers",timers);
    }
}
