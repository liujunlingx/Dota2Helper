package com.sanzhs.dota2helper.util;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Convert game_mode value to a descriptive string
     * @param gameModeValue game_mode value
     * @return a descriptive string
     */
    public static String gameModeConversion(int gameModeValue){
        switch (gameModeValue){
            case 0:
                return "None";
            case 1:
                return "全阵营选择";
            case 2:
                return "队长模式";
            case 3:
                return "随机征召";
            case 4:
                return "单一征召";
            case 5:
                return "全阵营随机";
            case 6:
                return "None";
            case 7:
                return "None";
            case 8:
                return "None";
            case 9:
                return "None";
            case 10:
                return "None";
            case 11:
                return "None";
            case 12:
                return "None";
            case 13:
                return "None";
            case 14:
                return "None";
            case 15:
                return "None";
            case 16:
                return "None";
            case 18:
                return "None";
            case 20:
                return "None";
            case 21:
                return "None";
            case 22:
                return "None";
            default:
                return "未知";
        }
    }

    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
