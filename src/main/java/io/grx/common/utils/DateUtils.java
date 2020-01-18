package io.grx.common.utils;

import static java.time.temporal.ChronoUnit.DAYS;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import io.jsonwebtoken.lang.Assert;

/**
 * 日期处理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static String formateDate(Date date) {
        return formateDate(date, DATE_PATTERN);
    }

    public static String formateDateTime(Date date) {
        return formateDate(date, DATE_TIME_PATTERN);
    }

    public static String formateDate(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String getStringDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static Date parseDate(String s) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);
            return df.parse(s);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse date: " + s);
        }
    }

    public static Date parseDateTime(String s) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            return df.parse(s);
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse date: " + s);
        }
    }

    public static long daysBetween(Date date1, Date date2) {
        Assert.notNull(date1);
        Assert.notNull(date2);

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return DAYS.between(localDate1, localDate2);
    }

    public static void main(String[] args) throws Exception {
	    Long dd  = daysBetween(parseDate("2017-01-05 11:12:11"), parseDate("2017-01-04"));
	    System.out.println(dd.intValue());

	    Double a = 0.6332;

	   int i = 10;
    }

}
