package com.ibicnCloud.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil extends DateUtils {
	public static int getDateDiffInDay(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null))
			throw new NullPointerException("输入不能为 NULL");

		return (int) ((date1.getTime() - date2.getTime()) / 86400000L);
	}

	public static int getDateDiffInHour(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null))
			throw new NullPointerException("输入不能为 NULL");

		return (int) ((date1.getTime() - date2.getTime()) / 3600000L);
	}

	public static int getDateDiffInMinute(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null))
			throw new NullPointerException("输入不能为 NULL");

		return (int) ((date1.getTime() - date2.getTime()) / 60000L);
	}

	public static int getDateDiffInSecond(Date date1, Date date2) {
		if ((date1 == null) || (date2 == null))
			throw new NullPointerException("输入不能为 NULL");

		return (int) ((date1.getTime() - date2.getTime()) / 1000L);
	}

	public static String parseDateByString(String strDate, String style, String outStyle) throws ParseException {
		if (StringUtil.isEmpty(strDate)) {
			return "";
		}
		strDate = StringUtil.substr(strDate, 19);
		Date date = DateUtil.parseDate(strDate, style);
		return DateFormatUtil.format(date, outStyle);
	}

	public static String getShijian(String date) {
		try {
			return getShijian(DateUtil.parseDate(date, "yyyy-MM-dd HH:mm:ss"));
		} catch (ParseException e) {
			return "";
		}
	}

	// 根据当前时间得到参数至现在的时间差，比如1分钟前，2小时，5天前，当达到15天以后时显示日期
	public static String getShijian(Date beginDate) {
		if (beginDate == null) {
			return "";
		}
		Date now = new Date();
		// 判断分钟数是否在60秒内
		long chaju = (now.getTime() - beginDate.getTime()) / (1000);
		if (chaju <= 60) {
			return chaju + "秒钟前";
		} else {
			// 判断分钟数是否在60分钟内
			chaju = (now.getTime() - beginDate.getTime()) / (60 * 1000);
			if (chaju <= 60) {
				return chaju + "分钟前";
			} else { // 判断分钟数是否在24小时内
				chaju = (now.getTime() - beginDate.getTime()) / (60 * 60 * 1000);
				if (chaju <= 24) {
					return chaju + "小时前";
				} else {// 判断分钟数是否在15天内
					chaju = (now.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
					if (chaju <= 15) {
						return chaju + "天前";
					} else {
						return DateFormatUtil.format(beginDate, "yyyy-MM-dd HH:mm");
					}
				}
			}
		}

	}

	public static void main(String[] args) throws ParseException {
		System.out.println(DateUtil.getDateDiffInMinute(new Date(), parseDate("2014-11-09 10:40:11","yyyy-MM-dd HH:mm:ss")));
	}
}
