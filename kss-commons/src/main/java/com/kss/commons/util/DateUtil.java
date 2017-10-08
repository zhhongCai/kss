package com.kss.commons.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期处理工具类
 * 
 */
public class DateUtil {
	public static final String DATE_FORMATE_STRING_DEFAULT = "yyyyMMddHHmmss";
	private static Map<String, SimpleDateFormat> formats;	
	private static final String DATE_FORMATE_STRING_A = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMATE_STRING_B = "yyyy-MM-dd";
	private static final String DATE_FORMATE_STRING_C = "MM/dd/yyyy HH:mm:ss a";
	private static final String DATE_FORMATE_STRING_D = "yyyy-MM-dd HH:mm:ss a";
	private static final String DATE_FORMATE_STRING_E = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String DATE_FORMATE_STRING_F = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final String DATE_FORMATE_STRING_G = "yyyy-MM-dd'T'HH:mm:ssz";
	private static final String DATE_FORMATE_STRING_H = "yyyyMMdd";
	private static final String DATE_FORMATE_STRING_I = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String DATE_FORMATE_STRING_J = "yyyyMMddHHmmss.SSS";
	private static final String DATE_FORMATE_STRING_K = "yyyyMM";
	private static final String DATE_FORMATE_STRING_L = "yyyy-MM-dd-HH";
	private static final String DATE_FORMATE_STRING_M = "yyyy/MM/dd HH:mm:ss";
	
		
	/**
	 * 静态块，首先执行
	 */
	static {
		formats = new HashMap<String, SimpleDateFormat>();

		formats.put(DATE_FORMATE_STRING_DEFAULT, new SimpleDateFormat(DATE_FORMATE_STRING_DEFAULT));
		formats.put(DATE_FORMATE_STRING_A, new SimpleDateFormat(DATE_FORMATE_STRING_A));
		formats.put(DATE_FORMATE_STRING_B, new SimpleDateFormat(DATE_FORMATE_STRING_B));
		formats.put(DATE_FORMATE_STRING_C, new SimpleDateFormat(DATE_FORMATE_STRING_C));
		formats.put(DATE_FORMATE_STRING_D, new SimpleDateFormat(DATE_FORMATE_STRING_D));
		formats.put(DATE_FORMATE_STRING_E, new SimpleDateFormat(DATE_FORMATE_STRING_E));
		formats.put(DATE_FORMATE_STRING_F, new SimpleDateFormat(DATE_FORMATE_STRING_F));
		formats.put(DATE_FORMATE_STRING_G, new SimpleDateFormat(DATE_FORMATE_STRING_G));
		formats.put(DATE_FORMATE_STRING_H, new SimpleDateFormat(DATE_FORMATE_STRING_H));
		formats.put(DATE_FORMATE_STRING_I, new SimpleDateFormat(DATE_FORMATE_STRING_I));
		formats.put(DATE_FORMATE_STRING_J, new SimpleDateFormat(DATE_FORMATE_STRING_J));
		formats.put(DATE_FORMATE_STRING_K, new SimpleDateFormat(DATE_FORMATE_STRING_K));
		formats.put(DATE_FORMATE_STRING_L, new SimpleDateFormat(DATE_FORMATE_STRING_L));
		formats.put(DATE_FORMATE_STRING_M, new SimpleDateFormat(DATE_FORMATE_STRING_M));
	}

	/**
	 * 将Date转换为 pattern 格式的字符串，格式为：
	 *  yyyyMMddHHmmss
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd
	 *	MM/dd/yyyy HH:mm:ss a
	 *	yyyy-MM-dd HH:mm:ss a
	 *	yyyy-MM-dd'T'HH:mm:ss'Z'
	 *	yyyy-MM-dd'T'HH:mm:ssZ
	 *	yyyy-MM-dd'T'HH:mm:ssz
	 *  yyyyMM
	 *  yyyy-MM-dd-HH
	 * @param date 日期
	 * @param pattern 日期格式
	 * @return String --格式化的日期字符串
	 * @see Date
	 */
	public static String getFormatTimeString(Date date, String pattern) {
		SimpleDateFormat sDateFormat = getDateFormat(pattern);
		return sDateFormat.format(date);
	}

	/**
	 * 将Date转换为默认的YYYYMMDDHHMMSS 格式的字符串
	 * @param date
	 * @return
	 */
	public static String getDefaultFormateTimeString(Date date) {
		return getFormatTimeString(date, DATE_FORMATE_STRING_DEFAULT);
	}

	/**
	 * 根据pattern取得的date formate
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat getDateFormat(String pattern) {
		SimpleDateFormat sDateFormat = formats.get(pattern);
		if (sDateFormat == null) {
			sDateFormat = new SimpleDateFormat(pattern);
			formats.put(pattern, sDateFormat);
		}
		return sDateFormat;
	}

	/**
	 * 将格式将日期字符串转换为Date对象
	 *
	 * @param date 字符串
	 * @param pattern 格式如下：
	 * 	yyyyMMddHHmmss
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd
	 *	MM/dd/yyyy HH:mm:ss a
	 *	yyyy-MM-dd HH:mm:ss a
	 *	yyyy-MM-dd'T'HH:mm:ss'Z'
	 *	yyyy-MM-dd'T'HH:mm:ssZ
	 *	yyyy-MM-dd'T'HH:mm:ssz
	 * @return 日期Date对象
	 * @throws ParseException
	 * @see Date
	 */
	public static Date getDateFromString(String date, String pattern) throws ParseException {
		SimpleDateFormat sDateFormat = getDateFormat(pattern);
		return sDateFormat.parse(date);
	}

	/**
	 * 将日期字符串转化成默认格式YYYYMMDDHHMMSS的Date对象
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDefaultDateFromString(String date) throws ParseException {
		return getDateFromString(date, DATE_FORMATE_STRING_DEFAULT);
	}

	/**
	 * 取当前时间,格式为YYYYMMDDHHMMSS的日期字符串
	 *
	 * @return 当前日期字符串
	 * @throws ParseException
	 * @see Date
	 */
	public static String getNowDefault() {
		return getNow(DATE_FORMATE_STRING_DEFAULT);
	}

	/**
	 * 按照pattern格式取当前日期字符串
	 * @param pattern	日期字符串格式
	 * @return			格式化后的当前日期字符串
	 */
	public static String getNow(String pattern) {
		return getFormatTimeString(new Date(), pattern);
	}

	/**
	 * 取当前时间,格式为YYYYMMDD
	 *
	 * @return 当前日期字符串
	 * @throws ParseException
	 * @see Date
	 */
	public static String getNowII() {
		return getFormatTimeString(new Date(), DATE_FORMATE_STRING_H);
	}

	/**
	 * 将输入pattern格式的日期字符串转换为取时间的毫秒数 since 1976
	 *
	 * @return 时间毫秒数
	 * @throws ParseException
	 * @see Date
	 */
	public static long dateStringToLong(String str, String pattern) throws ParseException {
		return getDateFromString(str, pattern).getTime();
	}

	/**
	 * 把since1976的毫秒数转成默认格式yyyyMMddHHmmss的String日期字符串
	 * @param time
	 * @return
	 */
	public static String longToDateStringDefault(long time) {
		return getFormatTimeString(new Date(time), DATE_FORMATE_STRING_DEFAULT);
	}

	/**
	 * 将时间的毫秒数 since 1976转换为按照pattern格式的日期字符串
	 *
	 * @return 日期字符串
	 * @see Date
	 */
	public static String longToDateString(long time, String pattern) {
		return getFormatTimeString(new Date(time), pattern);
	}
	
	/**
	 * 将Date对象转成since 1976的毫秒数
	 * @param date
	 * @return	since1976的毫秒数
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}
	
	/**
	 * 将since1976毫秒数转成Date对象
	 * @param time
	 * @return
	 */
	public static Date longToDate(long time){
		return new Date(time);
	}
	
	/**
	 * 自动适配两种格式的日期字符串转换为date对象
	 * A格式	:	yyyy-MM-dd HH:mm:ss
	 * B格式	:	yyyy-MM-dd
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromStringAdaptTwoPattern(String date) throws ParseException {
		try{
			return getDateFromString(date, DATE_FORMATE_STRING_A);
		}catch(ParseException e){
			return getDateFromString(date, DATE_FORMATE_STRING_B);
		}
	}
	
	/**
	 * 计算调用时间
	 * @param baseTime
	 * @return
	 */
	public static String calcInterval(Date baseTime) {
        String temp = String.valueOf(System.currentTimeMillis() - baseTime.getTime());
        baseTime.setTime(System.currentTimeMillis());
        return temp;
    }

	/**
	 * 获取当前时间前一天【格式为：yyyy-MM-dd-HH】
	 * @return
	 */
	public static String getDayOfBefore(){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return getFormatTimeString(calendar.getTime(), "yyyy-MM-dd-HH");
	}
	
	/**
	 * 获取当前时间前一天
	 * @return
	 */
	public static String getDayOfBefore(String pattern){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return getFormatTimeString(calendar.getTime(), pattern);
	}
	
	/**
	 * 获取指定时间前一天
	 * @return
	 * @throws Exception 
	 */
	public static String getDayOfBefore(String dateStr, String pattern) throws Exception{
		Date toDate = getDateFromString(dateStr, pattern);
		GregorianCalendar calendar = new GregorianCalendar();   
		calendar.setTime(toDate);   
		calendar.add(GregorianCalendar.DATE,   -1);   
		toDate = calendar.getTime();   
		return getFormatTimeString(toDate, pattern);
	}
	
	/**
	 * 获取当前时间前两天【格式为：yyyy-MM-dd-HH】
	 * @return
	 */
	public static String getDayBeforeYesterday(){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		return getFormatTimeString(calendar.getTime(), "yyyy-MM-dd-HH");
	}
	
	/**
	 * 获取当前时间前两天
	 * @return
	 */
	public static String getDayBeforeYesterday(String pattern){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.DAY_OF_MONTH, -2);
		return getFormatTimeString(calendar.getTime(), pattern);
	}
	
	/**
	 * 获取指定时间前两天
	 * @return
	 * @throws Exception 
	 */
	public static String getDayBeforeYesterday(String dateStr, String pattern) throws Exception{
		Date toDate = getDateFromString(dateStr, pattern);
		GregorianCalendar calendar = new GregorianCalendar();   
		calendar.setTime(toDate);   
		calendar.add(GregorianCalendar.DATE,   -2);   
		toDate = calendar.getTime();   
		return getFormatTimeString(toDate, pattern);
	}
	
	/**
	 * 获得当月第一天【格式：yyyy-MM-dd】
	 * @return
	 */
	public static String getFirstDayOfMonth() {
		Calendar lastDate = Calendar.getInstance();
		// 设为当前月的1号
		lastDate.set(Calendar.DATE, 1);
		return getFormatTimeString(lastDate.getTime(), "yyyy-MM-dd");
	}
	
	/**
	 * 获得当月最后一天【格式：yyyy-MM-dd】
	 * @return
	 */
	public static String getLastDayOfMonthy() {
		Calendar lastDate = Calendar.getInstance();
		// 设为当前月的1号
		lastDate.set(Calendar.DATE, 1);
		// 加一个月，变为下月的1号
		lastDate.add(Calendar.MONTH, 1);
		// 减去一天，变为当月最后一天
		lastDate.add(Calendar.DATE, -1);

		return getFormatTimeString(lastDate.getTime(), "yyyy-MM-dd");
	}
	
	/**
	 * 获得当月最后一天
	 * @param pattern
	 * @return
	 */
	public static String getLastDayOfMonthy(String pattern) {
		Calendar lastDate = Calendar.getInstance();
		// 设为当前月的1号
		lastDate.set(Calendar.DATE, 1);
		// 加一个月，变为下月的1号
		lastDate.add(Calendar.MONTH, 1);
		// 减去一天，变为当月最后一天
		lastDate.add(Calendar.DATE, -1);

		return getFormatTimeString(lastDate.getTime(), pattern);
	}
	
	/**
	 * 获得次月第一天【格式：yyyy-MM-dd】
	 * @return
	 */
	public static String getNextMonthFirstDay() {
		Calendar lastDate = Calendar.getInstance();
		// 减一个月
		lastDate.add(Calendar.MONTH, 1);
		// 把日期设置为当月第一天
		lastDate.set(Calendar.DATE, 1);
		return getFormatTimeString(lastDate.getTime(), "yyyy-MM-dd");
	}
	
	/**
	 * 获得次月第一天
	 * @param pattern
	 * @return
	 */
	public static String getNextMonthFirstDay(String pattern) {
		Calendar lastDate = Calendar.getInstance();
		// 减一个月
		lastDate.add(Calendar.MONTH, 1);
		// 把日期设置为当月第一天
		lastDate.set(Calendar.DATE, 1);
		return getFormatTimeString(lastDate.getTime(), pattern);
	}
	
	/**
	 * 获得次月最后一天【格式：yyyy-MM-dd】
	 * @return
	 */
	public static String getNextMonthLastDay() {
		Calendar lastDate = Calendar.getInstance();
		// 加一个月
		lastDate.add(Calendar.MONTH, 1);
		// 把日期设置为当月第一天
		lastDate.set(Calendar.DATE, 1);
		// 日期回滚一天，也就是本月最后一天
		lastDate.roll(Calendar.DATE, -1);
		return getFormatTimeString(lastDate.getTime(), "yyyy-MM-dd");
	}
	
	/**
	 * 获取当前时间上一月【格式为：yyyy-MM-dd-HH】
	 * @return
	 */
	public static String getMonthOfBefore(){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.MONTH, -1);
		return getFormatTimeString(calendar.getTime(), "yyyy-MM-dd-HH");
	}
	
	/**
	 * 获取当前时间上一月
	 * @return
	 */
	public static String getMonthOfBefore(String pattern){
		Calendar calendar = Calendar.getInstance(); 
		calendar.add(Calendar.MONTH, -1);
		return getFormatTimeString(calendar.getTime(), pattern);
	}
	
	/**
	 * 获取指定时间上一月
	 * @return
	 * @throws Exception 
	 */
	public static String getMonthOfBefore(String dateStr, String pattern) throws Exception{
		Date toDate = getDateFromString(dateStr, pattern);
		GregorianCalendar calendar = new GregorianCalendar();   
		calendar.setTime(toDate);   
		calendar.add(GregorianCalendar.MONTH, -1);   
		toDate = calendar.getTime();   
		return getFormatTimeString(toDate, pattern);
	}
	
	/**
	 * 获得当前15分钟前的时间(时间格式为yyyy-MM-dd-HH-mm)
	 * @param dateStr
	 * @return
	 * @throws Exception 
	 */
	public static String getMinuteOfBefore(String dateStr, String pattern) throws Exception{
		Date toDate = getDateFromString(dateStr, pattern);
		GregorianCalendar calendar = new GregorianCalendar();   
		calendar.setTime(toDate);   
		calendar.add(GregorianCalendar.MINUTE, -15);   
		toDate = calendar.getTime();   
		return getFormatTimeString(toDate, pattern);
	}
	
	/**
	 * 获得当前年份的最后一天
	 * @return
	 */
	public static String getYearOfEnd(){
		Calendar calendar = Calendar.getInstance();
		//到12月
		calendar.set(Calendar.MONTH, 11); 
		//获得12月的总天数
		int day = calendar.getMaximum(Calendar.DAY_OF_MONTH); 
		calendar.set(Calendar.DAY_OF_MONTH, day);
		return getFormatTimeString(calendar.getTime(), "yyyyMMdd");
	} 
}
