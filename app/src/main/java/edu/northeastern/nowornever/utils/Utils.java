package edu.northeastern.nowornever.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String date() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        return ft.format(dNow);
    }

}
