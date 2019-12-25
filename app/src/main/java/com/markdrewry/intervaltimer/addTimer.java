package com.markdrewry.intervaltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

public class addTimer extends AppCompatActivity {
    private ImageButton saveTimer;
    private EditText nameText;
    private EditText numIntText;
    private EditText lenInt;
    private EditText lenBreak;
    private TimerObj newT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timer);
        nameText = findViewById(R.id.editNameOfTimer);
        numIntText = findViewById(R.id.editNumOfIntervals);
        lenInt = findViewById(R.id.editTimeOfIntervals);
        lenBreak = findViewById(R.id.editTimeOfBreak);
        saveTimer = findViewById(R.id.saveTimerButton);
        newT = new TimerObj();
        saveTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameText.getText().toString().equals("") && !numIntText.getText().toString().equals("") && !lenInt.getText().toString().equals("") && !lenBreak.getText().toString().equals("")){
                    createTimer();
                    Intent exit = new Intent(addTimer.this,MainActivity.class);
                    exit.putExtra("timer",newT);
                    startActivity(exit);
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
    }

}
