package com.sanzhs.dota2helper.util;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sanzhs on 2017/8/31.
 */

public class StringUtils {

    public static String unixTimeStampToDate(long unixTimeStamp, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        Date date = new Date(unixTimeStamp * 1000);
        return fmt.format(date);
    }
}
