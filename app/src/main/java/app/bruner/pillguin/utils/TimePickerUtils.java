package app.bruner.pillguin.utils;

import android.content.Context;
import android.widget.EditText;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerUtils {

    public static void showTimePicker(Context context, EditText field) {
        field.setFocusable(false);
        field.setClickable(true);

        field.setOnClickListener(v -> {
            // get current time
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // create a time picker dialog
            new android.app.TimePickerDialog(
                    context,
                    (view, selectedHour, selectedMinute) -> {
                        // Format time as HH:mm
                        String timeStr = String.format(Locale.getDefault(), Constants.TIME_FORMATTER,
                                selectedHour, selectedMinute);
                        field.setText(timeStr);
                    },
                    hour, minute, true
            ).show();
        });
    }
}