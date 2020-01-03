package com.markdrewry.intervaltimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    private Drawable muteTexture,unmuteTexture;
    private ImageButton playButton, cancelButton, muteButton;
    private ProgressBar intervalBar, restBar, totalBar;
    private TextView timerName, intervalLeft, restLeft, countdownText, totalLeftText;
    private TimerObj t;
    private CountDownTimer startTimer,totalTimer,intTimer,breakTimer;
    private boolean timeRunning, muted, darkMode;
    private int numIntervals,intervalLength,breakLength, incrementIntervalProgress, incrementTotalProgress, incrementBreakProgress, soundID1,soundID2,counter = 0;
    private SoundPool sounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeObjects();
        updateUI();
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeRunning)
                    beginTimer();
            }
        });
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(muted){
                    muted = false;
                    muteButton.setBackground(unmuteTexture);
                }
                else{
                    muted = true;
                    muteButton.setBackground(muteTexture);
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
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
    private void beginTimer(){
        timeRunning = true;
        totalBar.setProgress(0);
        restBar.setProgress(0);
        intervalBar.setProgress(0);
        playButton.setVisibility(View.INVISIBLE);
        countdownText.setVisibility(View.VISIBLE);
        startTimer = new CountDownTimer(3000,1000) {
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
        totalTimer = new CountDownTimer((intervalLength+breakLength)*1000*numIntervals,(intervalLength+breakLength)*1000) {
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
        intTimer = new CountDownTimer(intervalLength*1000,1000) {
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
        breakTimer = new CountDownTimer(breakLength * 1000, 1000) {
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
        if(startTimer!=null){
            startTimer.cancel();
            startTimer = null;
        }
        if(totalTimer!=null){
            totalTimer.cancel();
            totalTimer = null;
        }
        if(intTimer!=null){
            intTimer.cancel();
            intTimer = null;
        }
        if(breakTimer!=null){
            breakTimer.cancel();
            breakTimer = null;
        }
        counter = 0;
        timerName.setText(t.getName());
        totalBar.setProgress(0);
        restBar.setProgress(0);
        intervalBar.setProgress(0);
        countdownText.setVisibility(View.INVISIBLE);
        intervalLeft.setText(""+intervalLength);
        restLeft.setText("" + breakLength);
        totalLeftText.setText(numIntervals+"/"+numIntervals);
        timeRunning = false;
        playButton.setVisibility(View.VISIBLE);
    }
    private void playSound(boolean main){
        if(!muted) {
            if (main)
                sounds.play(soundID1, 1, 1, 1, 0, 1);
            else
                sounds.play(soundID2, 1, 1, 1, 0, 1);
        }
    }
    private void setDarkMode(){
        if(darkMode)
            setTheme(R.style.NightMode);
        else
            setTheme(R.style.AppTheme);
    }
    private void initializeObjects(){
        t = getIntent().getParcelableExtra("selectedAlarm");
        darkMode = getIntent().getBooleanExtra("darkMode",false);
        setDarkMode();
        setContentView(R.layout.activity_timer);
        playButton = findViewById(R.id.playButton);
        cancelButton = findViewById(R.id.cancelButton);
        muteButton = findViewById(R.id.muteButton);
        intervalBar = findViewById(R.id.progressBar);
        restBar = findViewById(R.id.progressBarBreak);
        totalBar = findViewById(R.id.progressBarTotal);
        timerName = findViewById(R.id.timerNameText);
        intervalLeft = findViewById(R.id.progressLeftText);
        countdownText = findViewById(R.id.countdownStartText);
        totalLeftText = findViewById(R.id.totalLeftText);
        restLeft = findViewById(R.id.breakText);
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
        muteTexture = getDrawable(R.drawable.mute);
        unmuteTexture = getDrawable(R.drawable.unmute);
        intervalBar.setProgressDrawable(draw);
        restBar.setProgressDrawable(draw2);
        totalBar.setProgressDrawable(draw3);
        sounds = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
        soundID1 = sounds.load(getApplicationContext(),R.raw.beepmain,1);
        soundID2 = sounds.load(getApplicationContext(),R.raw.beepsecondary,2);
        muted = false;
    }
}
