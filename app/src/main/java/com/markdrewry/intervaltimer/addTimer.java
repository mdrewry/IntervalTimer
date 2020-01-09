package com.markdrewry.intervaltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;

public class addTimer extends AppCompatActivity {
    private ImageButton saveTimer;
    private EditText nameText, numIntText, lenInt, lenBreak;
    private TimerObj newT;
    private ArrayList<TimerObj> timers;
    private Boolean darkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        darkMode = getIntent().getBooleanExtra("darkMode",false);
        setDarkMode();
        setContentView(R.layout.activity_add_timer);
        nameText = findViewById(R.id.editNameOfTimer);
        numIntText = findViewById(R.id.editNumOfIntervals);
        lenInt = findViewById(R.id.editTimeOfIntervals);
        lenBreak = findViewById(R.id.editTimeOfBreak);
        saveTimer = findViewById(R.id.saveTimerButton);
        newT = new TimerObj();
        getList();
        saveTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameText.getText().toString().equals("") && !numIntText.getText().toString().equals("") && !lenInt.getText().toString().equals("") && !lenBreak.getText().toString().equals("")){
                    if(nameText.getText().length()>=11){
                        Toast.makeText(addTimer.this, "Name must be 10 or less characters",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        createTimer();
                        storeList();
                        Intent exit = new Intent(addTimer.this, MainActivity.class);
                        finish();
                        startActivity(exit);
                    }
                }
                else{
                    Toast.makeText(addTimer.this, "fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createTimer(){
        newT.setBreakLength(Integer.parseInt(lenBreak.getText().toString()));
        newT.setIntervalLength(Integer.parseInt(lenInt.getText().toString()));
        newT.setNumIntervals(Integer.parseInt(numIntText.getText().toString()));
        newT.setName(nameText.getText().toString());
        timers.add(newT);
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
    private void setDarkMode(){
        if(darkMode)
            setTheme(R.style.NightMode);
        else
            setTheme(R.style.AppTheme);
    }
}
