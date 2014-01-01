package com.ibicnCloud.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateUtil extends DateUtils{
	public static void main(String[] args) throws ParseException {
		Date date = DateUtil.parseDate("2013-11-13 11", "yyyy-MM-dd HH");
		date = DateUtil.addWeeks(date, 2);
		System.out.println(DateFormatUtil.format(date, "MM-dd/yyyy_HH"));
	}
}
