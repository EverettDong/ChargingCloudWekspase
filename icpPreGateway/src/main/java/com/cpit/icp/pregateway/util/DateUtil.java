package com.cpit.icp.pregateway.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public class DateUtil {

	/**
	 * 获取当前月
	 * 
	 * @param sDate1
	 * @return
	 */
	public static int getMonth() {
		Calendar cDay1 = Calendar.getInstance();
		int month = cDay1.get(Calendar.MONTH) + 1;
		return month;
	}

	/**
	 * 获取当前时间到明天凌晨0点相差秒数
	 * 
	 * @return
	 */
	public static Long getSecsToEndOfCurrentDay() {

		Long secsOfNextDay = nextDayFirstDate().getTime();
		// 将当前时间转为毫秒数
		Long secsOfCurrentTime = new Date().getTime();
		// 将时间转为秒数
		Long distance = (secsOfNextDay - secsOfCurrentTime) / 1000;
		if (distance > 0 && distance != null) {
			return distance;
		}

		return new Long(0);

	}
	
	/**
	 * 时间间隔 单位秒
	 * @param tdate
	 * @param ldate
	 * @return
	 */
	public static Long getInterval(Date tdate,Date ldate) {

		Long second = (tdate.getTime()-ldate.getTime())/1000;

		return second;

	}

	/**
	 * 获取第二天凌晨0点毫秒数
	 * 
	 * @return
	 */
	public static Date nextDayFirstDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static String getStrDate(Date date) {
		return DateFormatUtils.format(date, "yyyyMMddHHmmss");
	}

}