package com.markdrewry.intervaltimer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class TimerListAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<TimerObj> timers;
    private static LayoutInflater inflater = null;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.timermainview,null): itemView;
        TextView timerName = itemView.findViewById(R.id.nameOfTimerInList);
        timerName.setText(timers.get(position).getName());
        ImageButton deleteB = itemView.findViewById(R.id.deleteTimerButton);
        deleteB.setVisibility(View.INVISIBLE);
        return itemView;
    }
}
