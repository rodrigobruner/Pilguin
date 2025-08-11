package app.bruner.pillguin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.receivers.MedicationAlarmReceiver;

/**
 * Utility class to schedule alarms
 */
public class ScheduleAlarmUtils {

    public static void scheduleTaskAlarm(Context context, Medication medication) {

        // if task is null or due date null, abort schedule
        if (medication == null || medication.getSchedule().getNextTime() == null) {
            return;
        }

        // 2 minutes before due
        long triggerAtMillis = medication.getSchedule().getNextTime().getTime() - 2 * 60 * 1000;

        // convert task to JSON
        Gson gson = new Gson();
        String json = gson.toJson(medication);

        // prepare the intent to send to receiver
        Intent intent = new Intent(context, MedicationAlarmReceiver.class);
        intent.putExtra("MEDICATION", json);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) medication.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // get alarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Check permission
        if (!alarmManager.canScheduleExactAlarms()) {
            return;
        }

        // set the alarm
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
    }
}