package com.markdrewry.intervaltimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton addTimer,editTimerButton, darkModeButton;
    private ArrayList<TimerObj> timers;
    private ListView timerOptions;
    private TimerListAdapter adapter;
    private Drawable saveChangesBackground, editBackground;
    private TextView addTimerText,darkModeText;
    private CardView cardViewAdd,cardViewDarkMode;
    public static boolean edit, darkMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getList();
        declareFields();
        //buttons for navigation
        editTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edit)
                    editTimers();
                else
                    saveChanges();
            }
        });
        timerOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                storeList();
                Intent intent = new Intent(MainActivity.this,timer.class);
                intent.putExtra("selectedAlarm",timers.get(position));
                intent.putExtra("darkMode",darkMode);
                startActivity(intent);
            }
        });
        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,addTimer.class);
                storeList();
                intent.putExtra("darkMode",darkMode);
                startActivity(intent);
            }
        });
        darkModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(darkMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    darkMode = false;
                    recreate();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    darkMode = true;
                    recreate();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        storeList();
    }
    private void getList(){
        SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        darkMode = sharedPrefs.getBoolean("darkMode",false);
        if(darkMode)
            setTheme(R.style.NightMode);
        else
            setTheme(R.style.AppTheme);
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
        editor.putBoolean("darkMode",darkMode);
        editor.apply();
    }
    private void editTimers(){
        addTimer.setVisibility(View.VISIBLE);
        cardViewAdd.setVisibility(View.VISIBLE);
        darkModeButton.setVisibility(View.VISIBLE);
        cardViewDarkMode.setVisibility(View.VISIBLE);
        darkModeText.setVisibility(View.VISIBLE);
        addTimerText.setVisibility(View.VISIBLE);
        editTimerButton.setBackground(saveChangesBackground);
        edit = true;
        adapter.notifyDataSetChanged();
    }
    private void saveChanges(){
        addTimer.setVisibility(View.INVISIBLE);
        cardViewAdd.setVisibility(View.INVISIBLE);
        darkModeButton.setVisibility(View.INVISIBLE);
        cardViewDarkMode.setVisibility(View.INVISIBLE);
        darkModeText.setVisibility(View.INVISIBLE);
        addTimerText.setVisibility(View.INVISIBLE);
        editTimerButton.setBackground(editBackground);
        edit = false;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeList();
    }

    private void declareFields(){
        //ui objects declared
        setContentView(R.layout.activity_main);
        addTimer = findViewById(R.id.addTimer);
        timerOptions = findViewById(R.id.timerListview);
        editTimerButton = findViewById(R.id.editTimers);
        cardViewDarkMode = findViewById(R.id.cardViewDarkMode);
        cardViewAdd = findViewById(R.id.cardViewAdd);
        darkModeButton = findViewById(R.id.darkModeButton);
        addTimerText = findViewById(R.id.addTimerText);
        darkModeText = findViewById(R.id.changeDarkModeText);
        saveChangesBackground = getDrawable(R.drawable.savebutton);
        editBackground = getDrawable(R.drawable.settings);
        edit = false;
        darkModeText.setVisibility(View.INVISIBLE);
        addTimerText.setVisibility(View.INVISIBLE);
        addTimer.setVisibility(View.INVISIBLE);
        darkModeButton.setVisibility(View.INVISIBLE);
        cardViewDarkMode.setVisibility(View.INVISIBLE);
        cardViewAdd.setVisibility(View.INVISIBLE);
        adapter = new TimerListAdapter(this,timers);
        timerOptions.setAdapter(adapter);
    }

    public class TimerListAdapter extends BaseAdapter {
        private Activity mActivity;
        private  LayoutInflater inflater;

        public TimerListAdapter(Activity a,ArrayList<TimerObj> ts){
            mActivity = a;
            timers = ts;
            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            if(timers == null)
                return 0;
            return timers.size();
        }

        @Override
        public TimerObj getItem(int position) {
            return timers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            itemView = (itemView == null) ? inflater.inflate(R.layout.timermainview,null): itemView;
            TextView timerName = itemView.findViewById(R.id.nameOfTimerInList);
            TextView numIntervalsInList = itemView.findViewById(R.id.numIntervalsInList);
            TextView lengthIntervalInList = itemView.findViewById(R.id.lengthIntervalInList);
            TextView lengthBreakInList = itemView.findViewById(R.id.lengthBreakInList);
            ImageButton deleteB = itemView.findViewById(R.id.deleteTimerButton);
            CardView cardViewDelete = itemView.findViewById(R.id.cardViewDelete);
            CardView cardViewNumIntervals = itemView.findViewById(R.id.cardViewNumIntervals);
            CardView cardViewLengthIntervals = itemView.findViewById(R.id.cardViewLengthIntervals);
            CardView cardViewLengthBreak = itemView.findViewById(R.id.cardViewLengthBreak);
            numIntervalsInList.setText(""+timers.get(position).getNumIntervals());
            lengthIntervalInList.setText(""+timers.get(position).getIntervalLength());
            lengthBreakInList.setText(""+timers.get(position).getBreakLength());
            timerName.setText(timers.get(position).getName());
            itemView.setBackgroundResource(R.drawable.listviewselector);
            if(MainActivity.edit) {
                deleteB.setVisibility(View.VISIBLE);
                cardViewDelete.setVisibility(View.VISIBLE);
                cardViewNumIntervals.setVisibility(View.INVISIBLE);
                cardViewLengthBreak.setVisibility(View.INVISIBLE);
                cardViewLengthIntervals.setVisibility(View.INVISIBLE);
                numIntervalsInList.setVisibility(View.INVISIBLE);
                lengthIntervalInList.setVisibility(View.INVISIBLE);
                lengthBreakInList.setVisibility(View.INVISIBLE);
                deleteB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timers.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            else {
                deleteB.setVisibility(View.INVISIBLE);
                cardViewDelete.setVisibility(View.INVISIBLE);
                cardViewNumIntervals.setVisibility(View.VISIBLE);
                cardViewLengthBreak.setVisibility(View.VISIBLE);
                cardViewLengthIntervals.setVisibility(View.VISIBLE);
                numIntervalsInList.setVisibility(View.VISIBLE);
                lengthIntervalInList.setVisibility(View.VISIBLE);
                lengthBreakInList.setVisibility(View.VISIBLE);
            }
            return itemView;
        }
    }
}
