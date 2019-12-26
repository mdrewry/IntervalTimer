package com.markdrewry.intervaltimer;

import android.os.Parcel;
import android.os.Parcelable;


public class TimerObj implements Parcelable {
    private String name;
    private int numIntervals,intervalLength,breakLength;
    public TimerObj(){
    }
    protected TimerObj(Parcel in){
        this.name = in.readString();
        this.numIntervals = in.readInt();
        this.intervalLength = in.readInt();
        this.breakLength = in.readInt();
    }
    public TimerObj(String n, int ni, int il, int bl){
        this.name = n;
        this.numIntervals = ni;
        this.intervalLength = il;
        this.breakLength = bl;
    }
    public static final Creator<TimerObj> CREATOR = new Creator<TimerObj>() {
        @Override
        public TimerObj createFromParcel(Parcel in) {
            return new TimerObj(in);
        }

        @Override
        public TimerObj[] newArray(int size) {
            return new TimerObj[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(numIntervals);
        dest.writeInt(intervalLength);
        dest.writeInt(breakLength);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumIntervals() {
        return numIntervals;
    }

    public void setNumIntervals(int numIntervals) {
        this.numIntervals = numIntervals;
    }

    public int getIntervalLength() {
        return intervalLength;
    }

    public void setIntervalLength(int intervalLength) {
        this.intervalLength = intervalLength;
    }

    public int getBreakLength() {
        return breakLength;
    }

    public void setBreakLength(int breakLength) {
        this.breakLength = breakLength;
    }

    public String toString(){
        return name;
    }

}
