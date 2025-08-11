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

/**
 * Util class for show date and time pickers.
 */
public class DateTimePickerUtils {

    // Method to show just a date picker dialog
    public static void showDatePicker(Context context, TextView field) {
        field.setFocusable(false); // set not focusable field
        field.setClickable(true); // set clickable field

        // set OnClickListener
        field.setOnClickListener(v -> {

            // get current date and set as default
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // create and show DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // format the date to the TextView
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

    // Method to show just a time picker dialog
    public static void showTimePicker(Context context, TextView field) {
        field.setFocusable(false); // set not focusable field
        field.setClickable(true); // set clickable field

        field.setOnClickListener(v -> {
            // get current time and set as default
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, selectedHour, selectedMinute) -> {
                        // format the time to the TextView
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

    // Method to show a date and time picker dialog
    public static void showDateTimePicker(Context context, TextView field) {
        final Calendar calendar = Calendar.getInstance();

        field.setFocusable(false); // set not focusable field
        field.setClickable(true); // set clickable field

        field.setOnClickListener(v -> {
            // create a DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (dateView, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);

                        // create a TimePickerDialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                context,
                                (timeView, selectedHour, selectedMinute) -> {
                                    // format the date and time to the TextView
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
}