package app.bruner.pillguin.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.bruner.library.utils.Constants;

public class DateTimePickerUtils {

    public static void showDatePicker(Context context, TextView field) {
        field.setFocusable(false);
        field.setClickable(true);

        field.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMATTER, Locale.CANADA);
                        Calendar cal = Calendar.getInstance();
                        cal.set(selectedYear, selectedMonth, selectedDay);
                        String dateStr = dateFormat.format(cal.getTime());
                        field.setText(dateStr);
                    },
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    public static void showTimePicker(Context context, TextView field) {
        field.setFocusable(false);
        field.setClickable(true);

        field.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, selectedHour, selectedMinute) -> {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(Constants.TIME_FORMATTER, Locale.CANADA);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                        cal.set(Calendar.MINUTE, selectedMinute);
                        String timeStr = timeFormat.format(cal.getTime());
                        field.setText(timeStr);
                    },
                    hour, minute, false
            );

            timePickerDialog.show();
        });
    }

    public static void showDateTimePicker(Context context, TextView field) {
        final Calendar calendar = Calendar.getInstance();

        field.setFocusable(false);
        field.setClickable(true);

        field.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (dateView, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                context,
                                (timeView, selectedHour, selectedMinute) -> {
                                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
                                            Constants.DATE_FORMATTER + " " + Constants.TIME_FORMATTER,
                                            Locale.CANADA);
                                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                    calendar.set(Calendar.MINUTE, selectedMinute);
                                    String dateTimeStr = dateTimeFormat.format(calendar.getTime());
                                    field.setText(dateTimeStr);
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false
                        );
                        timePickerDialog.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

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
}