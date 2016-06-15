
package com.yunmall.ymsdk.utility;

import android.text.TextUtils;

import android.util.Log;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期时间工具类 Created by Zhp on 2014/7/8.
 */
public final class DateTimeUtils {
	
	public final static String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	
	public final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public final static String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";

	public final static String PATTERN_MM_DD = "MM-dd";

    /**
     * 格式化显示日期yyyy-MM-dd
     * 
     * @param timeInMillis 需要格式化显示的毫秒数
     * @return
     */
    public static String formatDate(long timeInMillis) {
        return formatDateTime(timeInMillis, false, 0);
    }
    
    /**
     * 格式化时间显示
     * @param timeInMillis
     * @param isTime 是否需要显示时分
     * @return
     */
    public static String formatDate(long timeInMillis,boolean isTime){
        return formatDateTime(timeInMillis, isTime, 0);
    }

    /**
     * 格式化显示日期yyyy-MM-dd
     *
     * @param timeString 需要格式化显示的时间字符串，例如2014-8-8 12:10
     * @return
     */
    public static String formatDate(String timeString,String fromPattern,String toPattern) {
        return formatDateTime(timeString,fromPattern,toPattern);
    }
    
    
    
    /**
     * 格式化显示日期和时间
     * 
     * @param timeInMillis 需要格式化显示的毫秒数
     * @param time 是否需要时间显示
     * @param type 格式化样式0：yyyy-MM-dd HH:mm
     *             格式化样式1：MM-dd HH:mm
     * @return
     */
    public static String formatDateTime(long timeInMillis, boolean time, int type) {
        DateTime dateTime = new DateTime(timeInMillis);
        String result = null;
        switch (type) {
            case 0:
                if (time) {
                    String pattern = PATTERN_YYYY_MM_DD_HH_MM;
                    result = dateTime.toString(pattern, Locale.US);
                } else {
                    String pattern = PATTERN_YYYY_MM_DD;
                    result = dateTime.toString(pattern, Locale.US);
                }
                break;
            case 1:
                result = dateTime.toString(PATTERN_MM_DD);
                break;
            case 2:
                result = dateTime.toString(PATTERN_YYYY_MM_DD_HH_MM_SS);
                break;
            default:
                String pattern = PATTERN_YYYY_MM_DD;
                result = dateTime.toString(pattern, Locale.US);
                break;
        }
        return result;
    }

    /**
     * 格式化显示日期和时间
     *
     * @param timeString 需要格式化显示的时间字符串，例如2014-8-8 12:10
     * @param fromPattern 是否需要时间显示
     * @param toPattern 格式化样式0：yyyy-MM-dd HH:mm
     * @return
     */
    public static String formatDateTime(String timeString, String fromPattern,String toPattern) {
		String result = null;
		try {
			if (!TextUtils.isEmpty(fromPattern)
					&& !TextUtils.isEmpty(toPattern)) {
				DateTimeFormatter formater = DateTimeFormat
						.forPattern(fromPattern);
				DateTime dateTime = DateTime.parse(timeString, formater);
				result = dateTime.toString(toPattern, Locale.US);
			}
		} catch (Exception e) {
			return timeString;
		}
		return result;
	}
    
    /**
     * 计算时间间隔
     * 
     * @param startMs 开始时间
     * @return 几天前或几小时前等等
     */
    public static String periodBetween(long startMs) {
        DateTime now = DateTime.now();
        DateTime start = new DateTime(startMs);
        int days = Days.daysBetween(start, now).getDays();
        if (days > 0) {
            if (days > 7) {
                if (start.getYear() == now.getYear()) {
                    return formatDateTime(startMs, false, 1);
                } else {
                    return formatDateTime(startMs, false, 0);

                }
            }
            if (days < 7) {
                return days + "天前";
            }
        }
        int hours = Hours.hoursBetween(start, now).getHours() % 24;
        if (hours > 0) {
            return hours + "小时前";
        }
        int minutes = Minutes.minutesBetween(start, now).getMinutes() % 60;
        if (minutes > 0) {
            return minutes + "分钟前";
        }
        int seconds = Seconds.secondsBetween(start, now).getSeconds() % 60;
        if (seconds > 0) {
            return "刚刚";
        }
        return "刚刚";
    }

    /**
     * 返回一段间隔分钟数
     * @return
     */
    public static int getMinutes(long currentMs,long priviousMs){
        DateTime current = new DateTime(currentMs);
        DateTime privious = new DateTime(priviousMs);
        int minutes = Minutes.minutesBetween(privious, current).getMinutes() % 60;
        return minutes;
    }
    public static String formatPeriod(long start, long end) {
        Period period = new Period(end- start);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .printZeroIfSupported()
                .appendHours().appendSeparator(":")
                .minimumPrintedDigits(2)
                .appendMinutes().appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        return formatter.print(period);
    }
    
    /**
     * 将2013:10:08 11:48:07如此格式的时间 转化为毫秒数
     * 
     * @param datetime 字符串时间
     * @return 毫秒数
     */
    public static long dateTimeToMS(String datetime) {
        if (TextUtils.isEmpty(datetime))
            return 0;
        DateTime dateTime = DateTime.parse(datetime);
        return dateTime.getMillis();
    }
    
    public static void main(String[] args) {
        String dateStart = "01/14/2012 09:29:58";
        String dateStop = "01/15/2012 10:31:48";
        
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        
        Date d1 = null;
        Date d2 = null;
        long end = 1409587200000l;
        long cur = 1409586359512l;
        Period period = new Period(cur- end);
        PeriodFormatter formatter = new PeriodFormatterBuilder().appendHours().appendSeparator(":").appendMinutes().appendSeparator(":").toFormatter();
        System.out.print(formatPeriod(cur, end));
//        try {
//            d1 = format.parse(dateStart);
//            d2 = format.parse(dateStop);
//
//            DateTime dt1 = new DateTime(d1);
//            DateTime dt2 = new DateTime(d2);
//            System.out.print(dt1.getZone().getOffset(System.currentTimeMillis()) + "\n");
//            System.out.print(Days.daysBetween(dt1, dt2).getDays() + " days, ");
//            System.out.print(Hours.hoursBetween(dt1, dt2).getHours() % 24 + " hours, ");
//            System.out.print(Minutes.minutesBetween(dt1, dt2).getMinutes() % 60 + " minutes, ");
//            System.out.print(Seconds.secondsBetween(dt1, dt2).getSeconds() % 60 + " seconds.\n");
//            System.out.print(formatDateTime(System.currentTimeMillis(), true, 0));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
