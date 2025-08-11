package app.bruner.library.utils;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Util to formatting date and time strings.
 */
public class DateTimeParseUtils {

    // transforms a date time string into a Date object
    public static Date parseDateTime(Context context, String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
                    Constants.DATE_FORMATTER + " " + Constants.TIME_FORMATTER,
                    Locale.CANADA);
            return dateTimeFormat.parse(dateTimeStr);
        } catch (ParseException e) {
            Log.i("DateTimePickerUtils", "Trying to parse as date only: " + dateTimeStr);
            return parseDate(context, dateTimeStr);
        }
    }

    // transforms a date string into a Date object
    public static Date parseDate(Context context, String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATTER, Locale.CANADA);
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            Log.i("DateTimePickerUtils", "Invalid date format: " + dateStr, e);
        }
        return null;
    }

    // formats a Date object into a date time string
    public static String formatDateTime(Context context, Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
                Constants.DATE_FORMATTER + " " + Constants.TIME_FORMATTER,
                Locale.CANADA);
        return dateTimeFormat.format(date);
    }
}
