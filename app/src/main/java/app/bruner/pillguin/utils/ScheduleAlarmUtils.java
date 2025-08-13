package app.bruner.pillguin.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.SideEffect;
import app.bruner.pillguin.receivers.MedicationAlarmReceiver;
import app.bruner.pillguin.receivers.SideEffectAlarmReceiver;

/**
 * Util class to schedule alarms
 */
public class ScheduleAlarmUtils {

    public static final String TYPE_MEDICATION_REMINDER = "reminder";
    public static final String TYPE_SIDE_EFFECT = "side_effect";

    public static void scheduleTaskAlarm(Context context, Medication medication, String type) {

        // if task is null or due date null, abort schedule
        if (medication == null || medication.getSchedule().getNextTime() == null) {
            return;
        }

        // convert task to JSON
        Gson gson = new Gson();
        String json = gson.toJson(medication);

        Intent intent;
        long triggerAtMillis;

        if (type.equals(TYPE_SIDE_EFFECT)){ // message about side effect
            // alarm to ask side effect
            Date now = new Date(); // current time + TIME_TO_ASK_SIDE_EFFECT_MINUTES minutes
            triggerAtMillis = now.getTime() + Constants.TIME_TO_ASK_SIDE_EFFECT_MINUTES * 60 * 1000;
            // prepare the intent to send to receiver
            intent = new Intent(context, SideEffectAlarmReceiver.class);
            intent.putExtra("MEDICATION", json);
        } else {
            // 2 minutes before due
            triggerAtMillis = medication.getSchedule().getNextTime().getTime() - Constants.TIME_TO_REMIND_MINUTES * 60 * 1000;
            // prepare the intent to send to receiver
            intent = new Intent(context, MedicationAlarmReceiver.class);
            intent.putExtra("MEDICATION", json);
        }

        // create the pending intent to redirect to receiver
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