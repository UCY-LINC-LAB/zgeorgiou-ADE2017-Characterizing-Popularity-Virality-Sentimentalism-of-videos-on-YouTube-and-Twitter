package com.zgeorg03.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class DateUtil {

    private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    public static long dayInMillis = 86400000;
    public static String toDate(long timestamp){

        return fmt.format(new Date(timestamp));
    }
}
