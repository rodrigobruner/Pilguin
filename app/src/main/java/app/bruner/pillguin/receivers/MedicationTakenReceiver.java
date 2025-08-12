package app.bruner.pillguin.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.utils.ScheduleAlarmUtils;

/**
 * A BroadcastReceiver that handle with taking medication on notification
 */
public class MedicationTakenReceiver extends BroadcastReceiver {
    private static final String EXTRA_MEDICATION = "medication";

    @Override
    public void onReceive(Context context, Intent intent) {

        // get the medication from the intent
        String medicationJson = intent.getStringExtra(EXTRA_MEDICATION);

        if (medicationJson != null) {

            // convert the medication to Medication object
            Medication medication = new Gson().fromJson(medicationJson, Medication.class);

            if (medication != null && medication.getSchedule() != null) { // if not null
                // save the time
                medication.getSchedule().addWhenTook(new Date());
                MedicationUtils.update(context, medication);

                ScheduleAlarmUtils.scheduleTaskAlarm(context, medication, ScheduleAlarmUtils.TYPE_SIDE_EFFECT);

                // cancel/clean the notification
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(medicationJson.hashCode());

                // show confirmation notification
                String message = context.getString(R.string.msg_you_took, medication.getName());
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}