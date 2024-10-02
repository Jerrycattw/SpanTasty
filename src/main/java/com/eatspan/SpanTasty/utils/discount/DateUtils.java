package com.eatspan.SpanTasty.utils.discount;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateUtils {
	public static Date GetDateFromString(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			return null;
		}

	}
	
	public static String getStringFromDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (date ==null) {
				return "";
			}
			return dateFormat.format(date);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static LocalDate convertLocalDate(String date) {
		try {
			LocalDate locakDate = LocalDate.parse(date);
			return locakDate;	
		} catch (Exception e) {
			return null;
		}
	}
}
