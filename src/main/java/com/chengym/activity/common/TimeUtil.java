package com.chengym.activity.common;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间转换公共方法
 */
public class TimeUtil {

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 字符串转date
     */
    public static Date dateFormat(String time) {
        if (!StringUtils.hasText(time)) {
            return new Date();
        }
        try {
            return threadLocal.get().parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    
    /**
     * Date to String
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return threadLocal.get().format(date);
    }
    
    /**
     * 获取当前时间标准格式的字符串
     * @author daiyan
     * @return 当前时间标准格式的字符串
     */
    public static String currentTime() {
    	return dateToString(new Date());
    }

    /**
     * 获取传入时间N天后的时间值
     *
     * @param date 传入的时间
     * @param days 传入的天数N
     * @return
     */
    public static Date getNextDays(Date date, int days)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * 将日期解析为calendar对象（业务包使用，勿删）
     *
     * @param baseDate 原始日期
     * @return calendar对象
     */
    public static Calendar parseDateTime(String baseDate)
    {
        Calendar cal = null;
        cal = new GregorianCalendar();
        int yy = Integer.parseInt(baseDate.substring(0, 4));
        int mm = Integer.parseInt(baseDate.substring(5, 7)) - 1;
        int dd = Integer.parseInt(baseDate.substring(8, 10));
        int hh = 0;
        int mi = 0;
        int ss = 0;
        if (baseDate.length() > 12)
        {
            hh = Integer.parseInt(baseDate.substring(11, 13));
            mi = Integer.parseInt(baseDate.substring(14, 16));
            ss = Integer.parseInt(baseDate.substring(17, 19));
        }
        cal.set(yy, mm, dd, hh, mi, ss);
        return cal;
    }

    /**
     * 由字符串转换为日期类型
     *
     * @param str    日期字符串
     * @param format 格式化字符串
     * @return 日期对象
     */
    public static Date getDate(String str, String format)
    {
        try
        {
            return new SimpleDateFormat(format).parse(str);
        }
        catch (ParseException e)
        {
            return null;
        }
        catch (RuntimeException e)
        {
            return null;
        }
    }

    /**
     * 返回date1 - date2 的分钟数
     *
     * @param date1 较小的时间
     * @param date2 较大的时间
     * @return 相隔分钟数[如果入参有一个为null，则返回一年的分钟数意为无穷大]
     */
    public static long getMinites(Date date1, Date date2)
    {
        if (date1 == null || date2 == null)
        {
            return 525600L;
        }
        long millSec = date2.getTime() - date1.getTime();

        return (millSec / 1000) / 60;
    }

    /**
     * 获取年月
     * [默认取当前的日期]
     *
     * @return
     */
    public static String getYYYYMMStr()
    {
        return getYYYYMMStr(new Date());
    }

    /**
     * 获取年月[格式yyyyMM]
     *
     * @param date 传入日期；传入null则默认当前日期
     * @return
     */
    public static String getYYYYMMStr(Date date)
    {
        if (null == date)
        {
            return "";
        }

        return new SimpleDateFormat("yyyyMM").format(date);
    }

    /**
     * 获取年月日
     * [默认取当前时间]
     *
     * @return
     */
    public static String getYYYYMMDDStr()
    {
        return getYYYYMMDDStr(new Date());
    }

    /**
     * 获取年月日[格式：yyyyMMdd]
     *
     * @param date 传入的日期
     * @return
     */
    public static String getYYYYMMDDStr(Date date)
    {
        if (null == date)
        {
            return "";
        }

        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    /**
     * 获取时分秒
     * [默认取当前的日期]
     *
     * @return
     */
    public static String getHHMMSSStr()
    {
        return getHHMMSSStr(new Date());
    }

    /**
     * 获取时分秒[格式HHmmss]
     *
     * @param date 传入的日期；传入null默认取当前日期
     * @return
     */
    public static String getHHMMSSStr(Date date)
    {
        if (null == date)
        {
            return "";
        }

        return new SimpleDateFormat("HHmmss").format(date);
    }

}
