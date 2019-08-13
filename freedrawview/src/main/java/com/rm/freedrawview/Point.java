package com.rm.freedrawview;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

public class Point implements Parcelable, Serializable {

    private static String TAG = "Point";

    static final long serialVersionUID = 42L;

    float x, y;

    private long t;

    Point() {
        Log.d(TAG, "Constructor");
        x = y = -1;
        t = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public String toString() {
        return "" + x + " : " + y + " - ";
    }


    // Parcelable stuff
    private Point(Parcel in) {
        Log.d(TAG, "Point");
        x = in.readFloat();
        y = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.d(TAG, "writeToParcel");
        dest.writeFloat(x);
        dest.writeFloat(y);
    }

    // Parcelable CREATOR class
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            Log.d(TAG, "createFromParcel");
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            Log.d(TAG, "newArray");
            return new Point[size];
        }
    };

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public long getT() {
        return t;
    }
}
