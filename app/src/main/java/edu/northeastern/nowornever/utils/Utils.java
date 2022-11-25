package edu.northeastern.nowornever.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'z '('Z')'");
        return ft.format(dNow);
    }

    @SuppressLint("SimpleDateFormat")
    public static String simpleDate() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEE MMM dd yyyy");
        return ft.format(dNow);
    }

}
