package com.teamred.checkmate.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static SimpleDateFormat briefFormat = new SimpleDateFormat("yyyy-mm-dd");
//    private static SimpleDateFormat detailFormat = new SimpleDateFormat("yyyy-mm-dd ");

    public static String getSimpleDateString(Date time){
        return briefFormat.format(time);
    }
}
