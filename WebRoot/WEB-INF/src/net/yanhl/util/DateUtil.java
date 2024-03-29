package net.yanhl.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;

/**
 * <p><b>Title：</b> 日期工具类</p>
 * <p><b>Description：</b></p>
 *
 * @author 闫洪磊
 * @since  1.0
 * @version 1.0.0.20090605
 */
public class DateUtil {

	public final static String FORMAT_DATE = "yyyy-MM-dd";
	public final static String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";

	public final static String FORMAT_DATE_ZH = "yyyy年MM月dd日";
	public final static String FORMAT_DATETIME_ZH = "yyyy年MM月dd日 HH时mm分ss秒";

	public final static String TYPE_DATE = "date";
	public final static String TYPE_DATETIME = "datetime";

	/**
	 * 日期排序类型-升序
	 */
	public final static int DATE_ORDER_ASC = 0;

	/**
	 * 日期排序类型-降序
	 */
	public final static int DATE_ORDER_DESC = 1;

	/**
	 * 用字符串获得日期
	 * @throws ParseException
	 * @dateValue 日期字符串
	 * @dateType 格式化的类型,date和datetime
	 */
	public static Date getDate(String dateValue, String dateType) throws ParseException {
		if (dateValue == null)
			return null;
		if (dateType.equals(TYPE_DATE)) {
			SimpleDateFormat sfdate = new SimpleDateFormat(FORMAT_DATE);
			return sfdate.parse(dateValue);
		} else if (dateType.equals(TYPE_DATETIME)) {
			SimpleDateFormat sftime = new SimpleDateFormat(FORMAT_DATETIME);
			return sftime.parse(dateValue);
		}
		return null;
	}

	/**
	 * 用字符串获得java.sql.Date日期
	 * @throws ParseException
	 * @dateValue 日期字符串
	 * @dateType 格式化的类型,date和datetime
	 */
	public static java.sql.Date getSqlDate(String dateValue, String dateType) throws ParseException {
		Date date = getDate(dateValue, dateType);
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 *将日期加上某些天或减去天数)返回字符串
	 * @param date 待处理日期
	 * @param to 加减的天数
	 * @return 日期
	 */
	public static Date dateAdd(String date, int to) {
		java.util.Date d = null;
		try {
			d = java.sql.Date.valueOf(date);
		} catch (Exception e) {
			e.printStackTrace();
			d = new java.util.Date();
		}
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(d);
		strDate.add(Calendar.DATE, to); // 日期减 如果不够减会将月变动
		return strDate.getTime();
	}

	/**
	 *将日期加上某些天或减去天数)返回字符串
	 * @param date 待处理日期
	 * @param to 加减的天数
	 * @return 日期
	 */
	public static java.sql.Date dateAdd(java.sql.Date date, int to) {
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(date);
		strDate.add(Calendar.DATE, to); // 日期减 如果不够减会将月变动
		return new java.sql.Date(strDate.getTime().getTime());
	}

	/**
	 * 格式化日期
	 * @param date		日期对象
	 * @param splitChar	分隔字符
	 * @return
	 */
	public static String formatDate(Date date, String splitChar) {
		java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat("yyyy" + splitChar
				+ "MM" + splitChar + "dd");
		return sfdate.format(date);
	}

	/**
	 * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
	 * @dateType 格式化的类型,date和datetime
	 */
	public static String format(Object dateValue, String dateType) {
		if (dateValue == null)
			return "";
		if (dateValue instanceof java.sql.Date) {
			return dateValue.toString();
		} else if (dateValue instanceof java.util.Date) {
			if (dateType.equals(TYPE_DATE)) {
				java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat(FORMAT_DATE);
				return sfdate.format(dateValue);
			} else if (dateType.equals(TYPE_DATETIME)) {
				java.text.SimpleDateFormat sftime = new java.text.SimpleDateFormat(FORMAT_DATETIME);
				return sftime.format(dateValue);
			} else {
				return "非法日期格式[" + dateType + "]";
			}
		} else {
			return "非日期类型";
		}
	}

	/**
	 * 转换日期对象为中文化日期
	 * @dateValue 日期对象，可以是java.util.Date和java.sql.Date
	 * @dateType 格式化的类型,date和datetime
	 */
	public static String formatZh(Date dateValue, String dateType) {
		if (dateValue == null)
			return "";
		if (dateValue instanceof java.sql.Date) {
			return dateValue.toString();
		} else if (dateValue instanceof java.util.Date) {
			if (dateType.equals(TYPE_DATE)) {
				java.text.SimpleDateFormat sfdate = new java.text.SimpleDateFormat(FORMAT_DATE_ZH);
				return sfdate.format(dateValue);
			} else if (dateType.equals(TYPE_DATETIME)) {
				java.text.SimpleDateFormat sftime = new java.text.SimpleDateFormat(FORMAT_DATETIME_ZH);
				return sftime.format(dateValue);
			} else {
				return "非法日期格式[" + dateType + "]";
			}
		} else {
			return "非日期类型";
		}
	}

	/**
	   * 转化成年月日期
	   * @param sDate          字符型日期：2009-02-02
	   * @param DelimeterChar  分割符号比如 / -
	   * @return               年月日期 :2009年02月02日
	   */
	public static String chDateChange(String sDate, String DelimeterChar) {
		String tmpArr[] = sDate.split(DelimeterChar);
		tmpArr[0] = tmpArr[0] + "年";
		tmpArr[1] = tmpArr[1] + "月";
		tmpArr[2] = tmpArr[2] + "日";
		return tmpArr[0] + tmpArr[1] + tmpArr[2];
	}

	/**
	 * 得到系统日期
	 * @return YYYY-MM-DD
	 */
	public static String getSysdate() {
		java.sql.Timestamp timeNow = new java.sql.Timestamp(System.currentTimeMillis());
		return timeNow.toString().substring(0, 10);
	}

	/**
	 * 得到系统日期
	 * @return YYYY-MM-DD
	 */
	public static String getSysdate(String formatType) {
		java.sql.Timestamp timeNow = new java.sql.Timestamp(System.currentTimeMillis());
		return formatZh(timeNow, formatType);
	}

	/**
	 * 得到某天是周几
	 * @param strDay
	 * @return 周几
	 */
	public static int getWeekDay(String strDay) {
		Date day = DateUtil.dateAdd(strDay, -1);
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(day);
		int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
		return meStrDate;
	}

	/**
	 * 得到某天是周几
	 * @param strDay
	 * @return 周几
	 */
	public static int getWeekDay(Date date) {
		Date day = DateUtil.dateAdd(format(date, "date"), -1);
		Calendar strDate = Calendar.getInstance();
		strDate.setTime(day);
		int meStrDate = strDate.get(Calendar.DAY_OF_WEEK);
		return meStrDate;
	}

	/**
	 * 取得两个日期段的日期间隔
	 *
	 * @author color
	 * @param t1 时间1
	 * @param t2 时间2
	 * @return t2 与t1的间隔天数
	 * @throws ParseException
	 *             如果输入的日期格式不是0000-00-00 格式抛出异常
	 */
	public static int getBetweenDays(String t1, String t2) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int betweenDays = 0;
		Date d1 = format.parse(t1);
		Date d2 = format.parse(t2);
		betweenDays = getBetweenDays(d1, d2);
		return betweenDays;
	}
	
	/**
	 * 取得两个日期段的日期间隔
	 *
	 * @author color
	 * @param t1 时间1
	 * @param t2 时间2
	 * @param swapDate	当日期1小于日期2时是否交换两个日期值
	 * @return t2 与t1的间隔天数
	 * @throws ParseException
	 *             如果输入的日期格式不是0000-00-00 格式抛出异常
	 */
	public static int getBetweenDays(String t1, String t2, boolean swapDate) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		int betweenDays = 0;
		Date d1 = format.parse(t1);
		Date d2 = format.parse(t2);
		betweenDays = getBetweenDays(d1, d2, swapDate);
		return betweenDays;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * @param d1	日期1
	 * @param d2	日期2
	 * @param swapDate	当日期1小于日期2时是否交换两个日期值
	 * @return	t2 与t1的间隔天数
	 */
	public static int getBetweenDays(Date d1, Date d2, boolean swapDate) {
		if (d1 == null || d2 == null) {
			return -1;
		}
		int betweenDays;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		if (swapDate) {
			// 保证第二个时间一定大于第一个时间
			if (c1.after(c2)) {
				c2.setTime(d1);
				c1.setTime(d2);
			}
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 取得两个日期段的日期间隔
	 * @param d1	日期1
	 * @param d2	日期2
	 * @return	t2 与t1的间隔天数
	 */
	private static int getBetweenDays(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			return -1;
		}
		int betweenDays;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		// 保证第二个时间一定大于第一个时间
		if (c1.after(c2)) {
			c2.setTime(d1);
			c1.setTime(d2);
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 判断指定日期是否在一个日期范围内
	 * @param fromDate	范围开始日期
	 * @param toDate	范围结束日期
	 * @param testDate	测试日期
	 * @return	在范围内true,否则false
	 */
	public static boolean betweenDays(java.sql.Date fromDate, java.sql.Date toDate, java.sql.Date testDate) {
		if (fromDate == null || toDate == null || testDate == null) {
			return false;
		}

		//1、 交换开始和结束日期
		if (fromDate.getTime() > toDate.getTime()) {
			java.sql.Date tempDate = fromDate;
			fromDate = toDate;
			toDate = tempDate;
		}

		//2、缩小范围
		long testDateTime = testDate.getTime();
		if ( (testDateTime > fromDate.getTime() && testDateTime > toDate.getTime())
				|| testDateTime < fromDate.getTime() && testDateTime < toDate.getTime()) {
			return false;
		}

		return true;
	}


	/**
	 * 得到指定年、月的最后一天
	 * @param year	年
	 * @param month	月
	 * @return	本年月的最后一天，如果2009,10，返回结果：2009-10-31
	 */
	public static String getLastDateDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 某年某月的最后一天
		int lastDate = cal.getActualMaximum(Calendar.DATE);
		return year + "-" + (month + 1) + "-" + lastDate;
	}

	/**
	 * 判断两个日期是否为同一天
	 * @param d1	日期一
	 * @param d2	日期二
	 * @return	同一天true，不是同一天false
	 */
	public static boolean isSameDate(Date d1, Date d2) {
		boolean result = false;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(d1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(d2);

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
				&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
				&& c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
			result = true;
		}
		return result;
	}

	/**
	 * 获取当前系统时间，24小时制
	 * @return	当前系统时间
	 */
	public static Time getSystemTime() {
		Calendar c1 = Calendar.getInstance();
		int hour = c1.get(Calendar.HOUR_OF_DAY);
		int minute = c1.get(Calendar.MINUTE);
		int second = c1.get(Calendar.SECOND);
		Time systemTime = Time.valueOf(hour + ":" + minute + ":" + second);
		return systemTime;
	}

	/**
	 * 是否为周末
	 * @param strDate
	 * @return true|false
	 */
	public static boolean isWeekend(String strDate) {
		int weekDay = getWeekDay(strDate);
		if (weekDay == 6 || weekDay == 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为周末
	 * @param strDate
	 * @return true|false
	 */
	public static boolean isWeekend(Date date) {
		int weekDay = getWeekDay(format(date, "date"));
		if (weekDay == 6 || weekDay == 7) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 是否为法定节假日
	 * @param strDate
	 * @return true|false
	 */
	public static boolean isHoliday(String strDate) {
		return false;
	}

	/**
	 * 是否为法定节假日
	 * @param strDate
	 * @return true|false
	 * @throws ConfigurationException 读取系统配置文件时
	 */
	public static boolean isHoliday(Date date) throws ConfigurationException {
		String specialDay = ReadProperties.getSystemValue("specialDay");

		// 未设置法定节假日价格
		if (!StringUtils.isEmpty(specialDay) && date != null) {
			String strDate = format(date, StringUtil.TYPE_DATE);
			String[] specialDays = specialDay.split(";");
			if (StringUtil.hasInArray(specialDays, strDate)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 日期排序
	 * @param dates		日期列表
	 * @param orderType	排序类型
	 * 			<br/>{@link #DATE_ORDER_ASC}<br/>
	 * 			{@link #DATE_ORDER_DESC}
	 * @return	排序结果
	 */
	public static List<? extends java.util.Date> orderDate(List<? extends java.util.Date> dates, int orderType) {
		DateComparator comp = new DateComparator(orderType);
		Collections.sort(dates, comp);
		return dates;
	}

	/**
	 * 日期分组<br/>
	 * 能够对指定日期列表按照连续性分组<br/>
	 * 例如：[2010-01-15, 2010-01-16, 2010-01-17, 2010-01-20, 2010-01-21, 2010-01-25]<br/>
	 * 分组结果为：<br/>
	 * <ul>
	 * <li>[2010-01-15, 2010-01-16, 2010-01-17]</li>
	 * <li>[2010-01-20, 2010-01-21]</li>
	 * <li>[2010-01-25]</li>
	 * </ul>
	 * @param dates	日期对象
	 * @return	连续性分组结果
	 */
	public static List<List<? extends java.util.Date>> groupDates(List<? extends java.util.Date> dates) {
		List<List<? extends java.util.Date>> result = new ArrayList<List<? extends java.util.Date>>();

		// 按照升序排序
		orderDate(dates, DateUtil.DATE_ORDER_ASC);

		// 临时结果
		List<Date> tempDates = null;

		// 上一组最后一个日期
		Date lastDate = null;

		// 当前读取日期
		Date cdate = null;
		for (int i = 0; i < dates.size(); i++) {
			cdate = dates.get(i);

			// 第一次增加
			if (tempDates == null) {
				tempDates = new ArrayList<Date>();
				tempDates.add(cdate);
				result.add(tempDates);
			} else {
				/**
				 * 差距为1是继续在原有的列表中添加，大于1就是用新的列表
				 */
				lastDate = tempDates.get(tempDates.size() - 1);
				int days = getBetweenDays(lastDate, cdate);
				if (days == 1) {
					tempDates.add(cdate);
				} else {
					tempDates = new ArrayList<Date>();
					tempDates.add(cdate);
					result.add(tempDates);
				}
			}

		}

		return result;
	}

	public static List<java.sql.Date> getBetweenDates(java.sql.Date fromDate, java.sql.Date toDate) {
		List<java.sql.Date> result = new ArrayList<java.sql.Date>();
		// 如果开始日期大于结束日期交换
		if (toDate.getTime() < fromDate.getTime()) {
			java.sql.Date tempDate = fromDate;
			fromDate = toDate;
			toDate = tempDate;
		}

		Calendar ca = Calendar.getInstance();
		while ( fromDate.getTime() <= toDate.getTime() ) {
			ca.setTime(fromDate);
			java.sql.Date tempDate = new java.sql.Date(ca.getTime().getTime());
			result.add(tempDate);
			ca.add(Calendar.DATE, 1);
			fromDate = new java.sql.Date(ca.getTime().getTime());
		}

		return result;
	}

	public static List<java.sql.Date> getAllDate(List<java.sql.Date[]> dateList) {
		List<java.sql.Date> result = new ArrayList<java.sql.Date>();
		for (Object[] objs : dateList) {
			if (objs[0] == null || objs[1] == null) {
				continue;
			}
			java.sql.Date date1 = (java.sql.Date) objs[0];
			java.sql.Date date2 = (java.sql.Date) objs[1];
			List<java.sql.Date> betweenDates = getBetweenDates(date1, date2);
			for (java.sql.Date date : betweenDates) {
				if (!result.contains(date)) {
					result.add(date);
				}
			}
		}
		return result;
	}
}

/**
 * <p><b>Title：</b>日期大小比较</p>
 * <p><b>Description：</b>实现比较接口，按照排序类型[升序,降序]排列日期集合</p>
 *
 * @author 闫洪磊
 */
class DateComparator implements Comparator<Date> {

	int orderType;

	public DateComparator(int orderType) {
		this.orderType = orderType;
	}

	public int compare(Date d1, Date d2) {
		if (d1.getTime() > d2.getTime()) {
			if (orderType == DateUtil.DATE_ORDER_ASC) {
				return 1;
			} else {
				return -1;
			}
		} else {
			if (d1.getTime() == d2.getTime()) {
				return 0;
			} else {
				if (orderType == DateUtil.DATE_ORDER_DESC) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}

}