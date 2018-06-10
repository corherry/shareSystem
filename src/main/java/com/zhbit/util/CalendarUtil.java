package com.zhbit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtil {

    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(c.getTime());
    }

    /**
     * 判断两个日期是否为同一天
     */
    public static boolean isSameDay(String d1, String d2){
        d1 = d1.substring(0, 10);
        d2 = d2.substring(0, 10);
        return d1.equals(d2);
    }

    /**
     * 判断两个日期是否为同一nian
     */
    public static boolean isSameYear(String d1, String d2){
        d1 = d1.substring(0, 4);
        d2 = d2.substring(0, 4);
        return d1.equals(d2);
    }

    /**
     * 两个日期相差天数
     */
    public static long getDistanceDays(String d1, String d2) throws Exception{
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long days=0;
        try {
            one = df.parse(d1);
            two = df.parse(d2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff ;
            if(time1<time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static String formatDateTime(String datetime){
        return datetime.substring(0, 19);
    }

    public static String getDate(String datetime){
        return datetime.substring(0, 10);
    }

    public static String formatShowDateTime(String datetime){
        String curDateTime = getCurrentDateTime();
        datetime = formatDateTime(datetime);
        if(isSameYear(curDateTime, datetime) == false){
            return datetime;
        }else{
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long diff = (df.parse(curDateTime).getTime() - df.parse(datetime).getTime()) / 1000; //相差秒数
                if (diff < 60) {
                    return diff + "秒前";
                } else if (diff < 60 * 60) {
                    return diff / 60 + "分钟前";
                } else if (isSameDay(curDateTime, datetime)) {
                    return "今天 " + datetime.substring(11, 16);
                } else {
                    df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(datetime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    return month + "月" + day + "日 " + datetime.substring(11, 16);
                }
            }catch (ParseException e){
                e.printStackTrace();
                return datetime;
            }
        }
    }

    public static String formatCSTDateTime(String datetime){

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
        Date date;
        try {
            date = (Date) sdf.parse(datetime);
            datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }

        String curDateTime = getCurrentDateTime();
        if (isSameYear(curDateTime, datetime) == false) {
            return datetime;
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                long diff = (df.parse(curDateTime).getTime() - df.parse(datetime).getTime()) / 1000; //相差秒数
                if (diff < 60) {
                    return diff + "秒前";
                } else if (diff < 60 * 60) {
                    return diff / 60 + "分钟前";
                } else if (isSameDay(curDateTime, datetime)) {
                    return "今天 " + datetime.substring(11, 16);
                } else {
                    df = new SimpleDateFormat("yyyy-MM-dd");
                    date = df.parse(datetime);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    int month = cal.get(Calendar.MONTH) + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    return month + "月" + day + "日 " + datetime.substring(11, 16);
                }
            } catch(ParseException e) {
                e.printStackTrace();
                return datetime;
            }
        }
    }

    public static String localToCST(String localTime) {
        localTime = formatDateTime(localTime);
        localTime = localTime.substring(0, 10) + "T" + localTime.substring(11, 19) + "Z";
        return localTime;
    }
}
