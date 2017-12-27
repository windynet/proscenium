package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author taochaung
 * @since 17.12.26
 *
 */
public final class DateUtil {

	private static final String NEW_FORMAT = "yyMMddHH";
	
	/**
	 * 两个日期相差的天数
	 * @param startTime 起始日期
	 * @param endTime 结束日期
	 * @return 天数
	 */
	public static int dataCompare(Date startTime, Date endTime) {
		int days = (int) ((endTime.getTime() - startTime.getTime()) / (1000*60*60*24));
		return days < 0 ? 0 : days;
	}
	
	/**
	 * 将传入日期字符串的格式转换为yyMMddHH
	 * @param time 字符串
	 * @param pattern 传入的字符串的日期格式
	 * @return 日期字符串
	 */
	public static String parseToString(String time, String pattern) {
		if(time == null) return "";
		Date date = parseToDate(time, pattern);
		DateFormat dateFormat = new SimpleDateFormat(NEW_FORMAT);
		return dateFormat.format(date);
	}
	
	/**
	 * 按格式转换为日期类
	 * @param time 字符串
	 * @param pattern 传入的字符串的日期格式
	 * @return 日期
	 */
	public static Date parseToDate(String Time, String pattern) {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			date = dateFormat.parse(Time);
		} catch (ParseException e) {
			return null;
		}
		return date;
	}
	
}
