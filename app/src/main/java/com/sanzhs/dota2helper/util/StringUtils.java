package com.sanzhs.dota2helper.util;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sanzhs on 2017/8/31.
 */

public class StringUtils {

    /**
     * convert unixTimeStamp to a formatted Date String
     * @param unixTimeStamp
     * @param format
     * @return
     */
    public static String unixTimeStampToDate(long unixTimeStamp, String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        Date date = new Date(unixTimeStamp * 1000);
        return fmt.format(date);
    }

    /**
     * render html String
     * @param html
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}
