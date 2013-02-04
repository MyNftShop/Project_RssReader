package com.reader.rss.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Html.TagHandler;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class MyDate {

	private static final String TAG = "MyDate";

	/***
	 * format time string to another format string
	 * 
	 * @param dateString
	 * @param inStringFormat
	 * @param outStringFormat
	 * @return
	 */
	public static final String format(String dateString, String inStringFormat,
			String outStringFormat) {
		Log.i(TAG, "input " + dateString);
		// change to date
		SimpleDateFormat inDf = new SimpleDateFormat(inStringFormat);
		try {
			Date date = inDf.parse(dateString);
			dateString = (String) DateFormat.format(outStringFormat, date);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// format date and return
		Log.i(TAG, "ret " + dateString);
		return dateString;
	}

	/**
	 * get now datetime string by format string pattern
	 * 
	 * @param string
	 * @return
	 */
	public static final String getNowByFormat(String pattern) {
		// TODO Auto-generated method stub
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date curDate = new Date(System.currentTimeMillis());
		Log.i(TAG, "getNowByFormat ret " + df.format(curDate));
		return df.format(curDate);
	}

	/**
	 * 转换GMT时间到指定格式的本地时间，
	 * 
	 * @param string
	 * @param format
	 * @return 有异常返回源字符串
	 */
	@SuppressWarnings("deprecation")
	public static final String formatGMTToLocal(String string, String format) {
		// TODO Auto-generated method stub
		// get date
		try {
			Date date;
			try {
				java.text.DateFormat formatter = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss zzz");
				date = formatter.parse(string);
			} catch (ParseException e1) {
				// TODO GMT false try utc
				try {
					java.text.DateFormat formatter = new SimpleDateFormat(
							"EEE MMM d HH:mm:ss Z yyyy");
					date = formatter.parse(string);
				} catch (Exception e) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					date = formatter.parse(string);// utc time
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
					calendar.add(Calendar.MILLISECOND,zoneOffset);
					date=calendar.getTime();
				}
			}
			// if get date succ,then format
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(TimeZone.getDefault());
			return simpleDateFormat.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return string;
		}
	}

}
