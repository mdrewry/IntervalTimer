package com.markdrewry.intervaltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.xml.datatype.Duration;

public class timer extends AppCompatActivity {

    private ImageButton playButton;
    private ProgressBar intervalBar, restBar, totalBar;
    private TextView timerName, intervalLeft, restLeft, countdownText, totalLeftText;
    private TimerObj t;
    private CountDownTimer mCountDownTimer;
    private boolean timeRunning;
    private int numIntervals,intervalLength,breakLength;
    private int incrementIntervalProgress, incrementTotalProgress, incrementBreakProgress;
    private int counter=0;
    private SoundPool sounds;
    private int soundID1,soundID2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        playButton = findViewById(R.id.playButton);
        intervalBar = findViewById(R.id.progressBar);
        restBar = findViewById(R.id.progressBarBreak);
        totalBar = findViewById(R.id.progressBarTotal);
        timerName = findViewById(R.id.timerNameText);
        intervalLeft = findViewById(R.id.progressLeftText);
        countdownText = findViewById(R.id.countdownStartText);
        totalLeftText = findViewById(R.id.totalLeftText);
        restLeft = findViewById(R.id.breakText);
        t = getIntent().getParcelableExtra("selectedAlarm");
        numIntervals = t.getNumIntervals();
        intervalLength = t.getIntervalLength();
        breakLength = t.getBreakLength();
        intervalBar.setMax(intervalLength);
        incrementIntervalProgress = 1;
        restBar.setMax(breakLength);
        incrementBreakProgress = 1;
        totalBar.setMax(numIntervals);
        incrementTotalProgress = 1;
        incrementIntervalProgress = 1;
        Drawable draw= getDrawable(R.drawable.progressb);
        Drawable draw2 = getDrawable(R.drawable.progressb);
        Drawable draw3 = getDrawable(R.drawable.progressb);
        intervalBar.setProgressDrawable(draw);
        restBar.setProgressDrawable(draw2);
        totalBar.setProgressDrawable(draw3);
        sounds = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
        soundID1 = sounds.load(getApplicationContext(),R.raw.beepmain,1);
        soundID2 = sounds.load(getApplicationContext(),R.raw.beepsecondary,2);
        updateUI();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeRunning)
                    cdTimer();
            }
        });
    }
    private void updateUI(){
        countdownText.setVisibility(View.INVISIBLE);
        timerName.setText(t.getName());
        intervalLeft.setText(""+intervalLength);
        restLeft.setText(""+breakLength);
        totalLeftText.setText(numIntervals+"/"+numIntervals);
        intervalBar.setProgress(intervalBar.getMax());
        restBar.setProgress(restBar.getMax());
        totalBar.setProgress(totalBar.getMax());
    }
    private void cdTimer(){
        timeRunning = true;
        totalBar.setProgress(0);
        restBar.setProgress(0);
        intervalBar.setProgress(0);
        playButton.setVisibility(View.INVISIBLE);
        countdownText.setVisibility(View.VISIBLE);
        mCountDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(""+(1+(millisUntilFinished/1000)));
                if(millisUntilFinished<=1000)
                    playSound(false);
                else
                    playSound(true);
            }

            @Override
            public void onFinish() {
                countdownText.setVisibility(View.INVISIBLE);
                executeTimer();
            }
        }.start();
    }
    private void executeTimer(){
        mCountDownTimer = new CountDownTimer((intervalLength+breakLength)*1000*numIntervals,(intervalLength+breakLength)*1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(totalBar.getProgress()+incrementTotalProgress>totalBar.getMax()-incrementTotalProgress)
                    totalBar.setProgress(totalBar.getMax());
                else
                    totalBar.incrementProgressBy(incrementTotalProgress);
                totalLeftText.setText(counter+1+"/"+numIntervals);
                intervalTimer();
            }
            @Override
            public void onFinish() {
                new CountDownTimer(150,150) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }
                    @Override
                    public void onFinish() {
                        resetTimer();
                    }
                }.start();
            }
        }.start();
    }
    private void intervalTimer(){
        mCountDownTimer = new CountDownTimer(intervalLength*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intervalLeft.setText(""+(Math.round((double)millisUntilFinished / 1000.0)));
                intervalBar.incrementProgressBy(incrementIntervalProgress);
                if(millisUntilFinished<=3000){
                    if(millisUntilFinished<=1000)
                        playSound(false);
                    else
                        playSound(true);
                }
            }

            @Override
            public void onFinish() {
                counter++;
                intervalBar.setProgress(0);
                intervalLeft.setText(""+0);
                if(counter>=numIntervals){
                    timerName.setText("Rest");
                    restLeft.setText("");
                    restBar.setProgress(0);
                }
                breakTimer();
            }
        }.start();
    }
    private void breakTimer(){
        mCountDownTimer = new CountDownTimer(breakLength * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                restLeft.setText("" + (Math.round((double) millisUntilFinished / 1000.0)));
                restBar.incrementProgressBy(incrementBreakProgress);
                if(millisUntilFinished<=3000){
                    if(millisUntilFinished<=1000)
                        playSound(false);
                    else
                        playSound(true);
                }
            }
            @Override
            public void onFinish() {
                restLeft.setText("" + 0);
                restBar.setProgress(0);
            }
        }.start();
    }
    private void resetTimer(){
        counter = 0;
        timerName.setText(t.getName());
        totalBar.setProgress(totalBar.getMax());
        restBar.setProgress(restBar.getMax());
        intervalBar.setProgress(intervalBar.getMax());
        intervalLeft.setText(""+intervalLength);
        restLeft.setText("" + breakLength);
        totalLeftText.setText(numIntervals+"/"+numIntervals);
        timeRunning = false;
        playButton.setVisibility(View.VISIBLE);
    }
    private void playSound(boolean main){
        if(main)
            sounds.play(soundID1,1,1,1,0,1);
        else
            sounds.play(soundID2,1,1,1,0,1);
    }
}
