package app.bruner.pillguin.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.bruner.pillguin.R;

public class DatePickerUtil {
    public static void showDatePicker(Context context, TextView field) {
        field.setFocusable(false);
        field.setClickable(true);

        field.setOnClickListener(v -> {
            // inicial value
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // create a dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        // format the date to yyyy-MM-dd
                        String dateTimeStr = String.format(Locale.getDefault(), Constants.DATE_FORMATTER,
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1, // month start in 0
                                calendar.get(Calendar.DAY_OF_MONTH));
                        field.setText(dateTimeStr);
                    },
                    year, month, day
            );

            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            // show the date picker dialog
            datePickerDialog.show();
        });
    }


    // try convert string to date format
    public static Date parseDate(Context context, String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        String[] parts = dateStr.split("-");

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(context.getString(R.string.date_picker_invalid_format), e);
        }
    }
}
