package test;

import java.util.Calendar;

public class TestDate {

	public static String getLastDateDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		// 某年某月的最后一天
		int lastDate = cal.getActualMaximum(Calendar.DATE);
		return year + "-" + month + "-" + lastDate;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

}
