package com.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public final class Utilities {
	public static final String TRACE_TAG = "Ordernow_trace";
	public static final String DEAL_MAKER_STORAGE = "OrderNowValues";
	public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
	// android sqlite stores datetime as locale format
	// EEE MMM dd HH:mm:ss zzz yyyy
	// but still this might change in other countries
	// therefore trying minimum possible date formats while reading from DB
	public static final List<String> SQLITE_LOCALE_FORMAT = new ArrayList<String>(
			Arrays.asList("EEE MMM dd HH:mm:ss zzz yyyy",
					"yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss",
					"yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy"));

	public static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat(
			DEFAULT_DATE_FORMAT);
	private static int MAX = 10;
	private static int MIN = 1;

	/**
	 * Utility to convert locale SQLite date format to given date format
	 * 
	 * @param inDate
	 *            assumed data String with locale date format
	 * @param newFormat
	 *            given new data format
	 * @return formatted date string
	 */
	public static String formatDate(String inDate, String newFormat) {
		for (String format : SQLITE_LOCALE_FORMAT) {
			try {
				SimpleDateFormat dt1 = new SimpleDateFormat(format);
				SimpleDateFormat dt2 = new SimpleDateFormat(newFormat);
				return dt2.format(dt1.parse(inDate));
			} catch (Exception e) {
			}
		}
		return inDate;
	}

	/**
	 * Utility to convert locale SQLite date format to given date format
	 * 
	 * @param inDate
	 *            assumed data String with locale date format
	 * @param newFormat
	 *            given new data format
	 * @return formatted date object
	 */
	public static Date dateFormat(String inDate, String newFormat) {
		SimpleDateFormat dt2 = new SimpleDateFormat(newFormat);
		String formatedDate = formatDate(inDate, newFormat);
		if (formatedDate != null) {
			try {
				return dt2.parse(formatedDate);
			} catch (Exception e) {
				return null;
			}
		} else {
			info("No format matched this date " + inDate);
			return null;
		}
	}

	/**
	 * Utility to format a Date object to default date format String
	 * 
	 * @param inDate
	 *            date object
	 * @return default formatted string
	 */
	public static String defaultDateFormat(Date inDate) {
		try {
			if (inDate != null)
				return defaultDateFormat.format(inDate);
			else
				return null;
		} catch (Exception e) {
			return inDate.toString();
		}
	}

	public static void overrideFonts(final Context context, final View v) {
		try {

			if (v instanceof ViewGroup) {
				ViewGroup vg = (ViewGroup) v;
				for (int i = 0; i < vg.getChildCount(); i++) {
					View child = vg.getChildAt(i);
					overrideFonts(context, child);
				}
			} else if (v instanceof EditText) {

				((EditText) v).setTypeface(Typeface.createFromAsset(
						context.getAssets(), "CALIBRI.TTF"));
			} else if (v instanceof TextView) {
				((TextView) v).setTypeface(Typeface.createFromAsset(
						context.getAssets(), "CALIBRI.TTF"));
			}
		} catch (Exception e) {
			Utilities.error("Error while overriding fonts" + e.toString());
		}
	}

	public static Map<String, String> getMappings(String alternate) {
		Map<String, String> altMapping = new HashMap<String, String>();
		try {
			if (alternate != null && !alternate.trim().equals("")) {
				String[] cols = alternate.split(",");
				for (String col : cols) {
					altMapping.put(col.split("~")[0], col.split("~")[1]);
				}
			}
		} catch (Exception e) {
		}
		return altMapping;
	}

	public static String fieldToColumnMap(String jsonField,
			Map<String, String> altMapping) {
		String dbColumn = toDBCase(jsonField);
		String alDbColumn = altMapping.get(dbColumn.toUpperCase());
		if (alDbColumn == null)
			alDbColumn = altMapping.get(dbColumn.toLowerCase());
		if (alDbColumn == null)
			alDbColumn = altMapping.get(initUpperLower(dbColumn));
		if (alDbColumn != null)
			info("Altername column Found for " + jsonField + " = " + alDbColumn);
		return alDbColumn;
	}

	/**
	 * Get current week no, start date and END date.
	 */
	public static Map getCurrentWeekNo() {
		Map weekMap = new HashMap();
		Calendar cal = Calendar.getInstance();
		// cal.set(2013, 10 - 1, 12);

		// "calculate" the start date of the week
		Calendar first = (Calendar) cal.clone();
		first.add(Calendar.DAY_OF_WEEK,
				first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

		// and add six days to the end date
		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_YEAR, 6);

		// print the result
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		weekMap.put("WeekNO", "W" + cal.get(Calendar.WEEK_OF_MONTH));
		weekMap.put("StartDate", df.format(first.getTime()));
		weekMap.put("EndDate", df.format(last.getTime()));

		return weekMap;
	}

	public static String getSharedValue(final Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(
				DEAL_MAKER_STORAGE, 0);
		return settings.getString(name, null);
	}

	public static void setSharedValue(final Context context, String name,
			String newValue) {
		SharedPreferences settings = context.getSharedPreferences(
				DEAL_MAKER_STORAGE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, newValue);
		editor.commit();
	}

	public static void setSharedBooleanValue(final Context context,
			String name, boolean newValue) {
		SharedPreferences settings = context.getSharedPreferences(
				DEAL_MAKER_STORAGE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, newValue);
		editor.commit();
	}

	public static boolean getSharedBooleanValue(final Context context,
			String name) {
		SharedPreferences settings = context.getSharedPreferences(
				DEAL_MAKER_STORAGE, 0);
		return settings.getBoolean(name, false);
	}

	/**
	 * Utility function to check the provided column matches any column name
	 * exists in the real table Example: sometimes android SQlite maintains
	 * table column first letter capital therefore "CREATION_DATE",
	 * "creation_date" and "Creation_date" all are same
	 * 
	 * @param colNames
	 *            list of column names in a table
	 * @param assumedColumnName
	 *            given column name
	 * @return the read column name in table
	 */
	public static String getRealColumnName(List<String> colNames,
			String assumedColumnName) {
		if (colNames.contains(assumedColumnName)) {
			return assumedColumnName;
		} else if (colNames.contains(assumedColumnName.toUpperCase())) {
			return assumedColumnName.toUpperCase();
		} else if (colNames.contains(Utilities
				.initUpperLower(assumedColumnName))) {
			return Utilities.initUpperLower(assumedColumnName);
		}
		return null;
	}

	public static void clearSharedValue(final Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(
				DEAL_MAKER_STORAGE, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(name);
		editor.commit();
	}

	public static boolean haveInternetConnection(final Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = conMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo activeNetInfo = conMgr.getActiveNetworkInfo();

		if (activeNetInfo != null
				&& activeNetInfo.isConnected()
				&& (mobile != null && mobile.isAvailable() || wifi != null
						&& wifi.isAvailable())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean haveNetworkConnection(final Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public static void toast(final Context context, String message) {
		Toast.makeText(context.getApplicationContext(), message,
				Toast.LENGTH_LONG).show();
	}

	public static AlertDialog okDialog(final Context context, String message) {
		return okDialog(context, "Deal Maker", message, null, null);
	}

	public static AlertDialog okDialog(final Context context, String title,
			String message) {
		return okDialog(context, title, message, null, null);
	}

	public static AlertDialog okDialog(final Context context, String title,
			String message, String okText,
			DialogInterface.OnClickListener okOnclick) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(message)
				.setCancelable(false)
				.setNeutralButton(
						okText == null ? "Ok" : okText,
						okOnclick != null ? okOnclick
								: new DialogInterface.OnClickListener() {
									public void onClick(
											final DialogInterface dialog,
											final int id) {
										dialog.dismiss();
									}
								});
		return builder.create();
	}

	/**
	 * Utility function to convert a string MY_COLUMN_NAME to myColumnName
	 * 
	 * @param src
	 *            - input string
	 * @return converted string
	 */
	public static String toCamelCase(final String src) {
		if (src == null)
			return null;
		if (src.indexOf("_") == -1)
			return src.toLowerCase();

		StringBuilder finalCamel = new StringBuilder();

		Pattern pattern = Pattern.compile("(_)");
		boolean first = true;
		for (String s : pattern.split(src)) {
			finalCamel.append(first ? String.valueOf(s.charAt(0)).toLowerCase()
					: String.valueOf(s.charAt(0)).toUpperCase());
			finalCamel.append(s.substring(1).toLowerCase());
			first = false;
		}

		return finalCamel.toString();
	}

	/**
	 * Utility function to reverse convert a string myColumnName to
	 * MY_COLUMN_NAME
	 * 
	 * @param src
	 *            - input string
	 * @return converted string
	 */
	public static String toDBCase(final String src) {
		if (src == null)
			return null;

		Pattern pattern = Pattern.compile("(?=\\p{Lu})");
		StringBuilder finalDb = new StringBuilder();
		for (String s : pattern.split(src)) {
			finalDb.append("_" + s.toLowerCase());
		}
		while (finalDb.toString().startsWith("_"))
			finalDb.deleteCharAt(0);

		return finalDb.toString();
	}

	public static void deleteLast(String last, StringBuilder sb) {
		while (sb.toString().endsWith(last))
			sb.deleteCharAt(sb.length() - 1);
	}

	public static void deleteFirst(String first, StringBuilder sb) {
		while (sb.toString().startsWith(first))
			sb.deleteCharAt(0);
	}

	public static void invokeSetter(Object obj, String fieldName) {
		try {
			obj.getClass().getMethod("set" + initUpper(fieldName),
					new Class[] { Object.class });
		} catch (NoSuchMethodException e) {
		}
	}

	public static String initUpper(String str) {
		return String.valueOf(str.charAt(0)).toUpperCase() + str.substring(1);
	}

	public static String initUpperLower(String str) {
		return String.valueOf(str.charAt(0)).toUpperCase()
				+ str.substring(1).toLowerCase();
	}

	public static String initLower(String str) {
		return String.valueOf(str.charAt(0)).toLowerCase() + str.substring(1);
	}

	public static List<String> getBeanFields(Class inClass) {
		List<String> fields = new ArrayList<String>();
		Method[] methods = inClass.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set")) {
				String fieldName = method.getName().substring(3);
				fields.add(initLower(fieldName));
			}
		}
		return fields;
	}

	public static void info(String message) {
		Log.i(TRACE_TAG, message);
	}

	public static void error(String message) {
		Log.e(TRACE_TAG, message);
	}

	public static void warning(String message) {
		Log.w(TRACE_TAG, message);
	}

	public static boolean isNulOrEmpty(String inString) {
		return inString == null || inString.trim().equals("");
	}

	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			Utilities.error(e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Utilities.error(e.toString());
				}
			}
		}

		return sb.toString();

	}

	public static Integer getIntegerFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			Utilities.error(e.toString());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Utilities.error(e.toString());
				}
			}
		}

		return Integer.parseInt(sb.toString());

	}

	public static void showDecisionAlert(Activity context, String title,
			String message, String yesLabel, String noLabel,
			OnClickListener yesCallback, OnClickListener noCallback) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(title).setMessage(message).setCancelable(false)
				.setPositiveButton(yesLabel, yesCallback)
				.setNegativeButton(noLabel, noCallback);
		builder.create().show();
	}

	public static String getAddressSuitsForMap(String Address) {
		String[] parts = null;
		if (Address != null) {
			parts = Address.split("\\,");
		}
		StringBuffer addressBuffer = new StringBuffer();
		if (parts != null && parts.length > 1) {
			for (int i = 2; i < parts.length; i++) {
				addressBuffer.append(parts[i]);
			}
		}
		return addressBuffer.toString();
	}

	public static String getDateInDisplayFormat(String dateString,
			String inputFormat) {

		// Format the date into display format
		String formatedDate = "";
		SimpleDateFormat initialFormat = new SimpleDateFormat(inputFormat);
		SimpleDateFormat displayFormart = new SimpleDateFormat("dd/MM/yyyy");
		try {
			formatedDate = displayFormart.format(initialFormat
					.parse(dateString));
		} catch (ParseException e) {
			Utilities.error("Unable to parse the date" + e.toString());
		}
		return formatedDate;
	}

	public static String formatDate(String dateString, String inputFormat,
			String outputFormat) {

		// Format the date into display format
		String formatedDate = "";
		SimpleDateFormat initialFormat = new SimpleDateFormat(inputFormat);
		SimpleDateFormat displayFormart = new SimpleDateFormat(outputFormat);
		try {
			formatedDate = displayFormart.format(initialFormat
					.parse(dateString));
		} catch (ParseException e) {
			Utilities.error("Unable to parse the date" + e.toString());
		}
		return formatedDate;
	}

	public static void dismisProgressDialog(ProgressDialog progressDialog) {
		try {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
		} catch (Exception e) {
		}
	}
	
	public static int randInt() {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((MAX - MIN) + 1) + MIN;

	    return randomNum;
	}

}

