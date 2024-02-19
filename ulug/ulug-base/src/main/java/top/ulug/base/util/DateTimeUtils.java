package top.ulug.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public abstract class DateTimeUtils {
    public static final String FORMAT_DATEITEM = "yyyy-MM-dd";
    public static final String FORMAT_TIME_M = "HH:mm";
    public static final String FORMAT_TIME = "HH:mm:ss";

    /**
     * @return 返回当前日期字符串
     */
    public static String getDateItem() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_DATEITEM);
        return df.format(new Date());
    }

    /**
     * @return 返回当前日期和时间字符串
     */
    public static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat(
                FORMAT_DATEITEM + " " + FORMAT_TIME_M);
        return df.format(new Date());
    }

    /**
     * @param format 格式化
     * @return 返回当前日期/时间字符串
     */
    public static String getDateTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * @param date   Date
     * @param format 格式化
     * @return 返回指定日期/时间字符串
     */
    public static String getDateTime(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 返回date
     *
     * @param dateItem
     * @param format   格式化
     * @return Date
     */
    public static Date getDate(String dateItem, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(dateItem);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回Calendar
     *
     * @param dateItem 日期字符串
     * @param format   格式化
     * @return 日期
     */
    public static Calendar getCalendar(String dateItem, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cr = Calendar.getInstance();
        try {
            cr.setTime(df.parse(dateItem));
            return cr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取当天是星期几 1-7
     */
    public static int getTodayWeek() {
        Integer weekId;
        Calendar cr = Calendar.getInstance();
        cr.setTime(new Date());
        weekId = cr.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                ? 7
                : (cr.get(Calendar.DAY_OF_WEEK) - 1);
        return weekId;
    }

    /**
     * 计算两日期相隔几天
     *
     * @param startDay 开始
     * @param endDay   结束
     * @param format   格式
     * @return long
     */
    public static Long countDays(String startDay, String endDay,
                                 String format) {
        long days = -1l;
        if (startDay == null || endDay == null) {
            return days;
        }
        if (startDay.equals(endDay))
            return 0l;
        if (format == null)
            format = FORMAT_DATEITEM;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
            Date d_s = df.parse(startDay);
            Date d_e = df.parse(endDay);
            days = countDays(d_s, d_e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return days;
    }

    /**
     * 计算两日期相隔几天
     *
     * @param StartDate 开始
     * @param endDate   结束
     * @return long
     */
    public static Long countDays(Date StartDate, Date endDate) {
        long days = -1l;
        long time0 = StartDate.getTime();
        long time1 = endDate.getTime();
        days = (time1 - time0) / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     * 返回两日期相隔几周
     *
     * @param startDay 开始
     * @param endDay   结束
     * @param format   格式
     * @return long
     */
    public static long countWeeks(String startDay, String endDay,
                                  String format) {
        long weeks = -1l;
        if (startDay == null || endDay == null) {
            return weeks;
        }
        if (startDay.equals(endDay))
            return 0l;
        if (format == null)
            format = FORMAT_DATEITEM;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format, Locale.CHINA);
            Date d_s = df.parse(startDay);
            Calendar cr_s = Calendar.getInstance();
            cr_s.setTime(d_s);
            Date d_e = df.parse(endDay);
            Calendar cr_e = Calendar.getInstance();
            cr_e.setTime(d_e);
            int year_s = cr_s.get(Calendar.YEAR);
            int year_e = cr_e.get(Calendar.YEAR);
            int week_s = cr_s.get(Calendar.WEEK_OF_YEAR);
            int week_e = cr_e.get(Calendar.WEEK_OF_YEAR);
            if (year_s == year_e) {
                weeks = week_e - week_s;
            } else {
                Calendar crtmp = Calendar.getInstance();
                crtmp.set(year_s, Calendar.DECEMBER, 31, 59, 59);
                weeks = crtmp.get(Calendar.WEEK_OF_YEAR) - week_s + week_e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeks;
    }

    /**
     * @param date 日期
     * @return 最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取某月最大天数
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        //格式化日期
        return calendar.getTime();
    }

    /**
     * 获取前n天日期
     *
     * @param n    前几天
     * @param date 坐标日期
     * @return date
     */
    public static Date getDateAgo(int n, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - n);
        return calendar.getTime();
    }
}
