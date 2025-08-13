package app.bruner.pillguin.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.DataSyncUtils;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.utils.ScheduleAlarmUtils;

/**
 * A BroadcastReceiver to dial with taking medication on notification
 */
public class MedicationTakenReceiver extends BroadcastReceiver {
    private static final String EXTRA_MEDICATION = "medication";

    @Override
    public void onReceive(Context context, Intent intent) {
        String medicationJson = intent.getStringExtra(EXTRA_MEDICATION);

        if (medicationJson != null) {
            Medication medication = new Gson().fromJson(medicationJson, Medication.class);

            if (medication != null && medication.getSchedule() != null) {

                //TODO: refectory here to use ViewModel to do it, I create a getMedicationById
                // get the current medication from storage
                ArrayList<Medication> currentMedications = MedicationUtils.getAll(context);
                Medication currentMedication = null;

                for (Medication med : currentMedications) {
                    if (med.getId() == medication.getId()) {
                        currentMedication = med;
                        break;
                    }
                }

                if (currentMedication != null) {
                    // Add that took date
                    currentMedication.getSchedule().addWhenTook(new Date());
                    MedicationUtils.update(context, currentMedication); // save

                    // force data sync
                    DataSyncUtils.sendUpdate(context);

                    // create a side effect alarm
                    ScheduleAlarmUtils.scheduleTaskAlarm(context, currentMedication, ScheduleAlarmUtils.TYPE_SIDE_EFFECT);
                }

                // clear the notification
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(medicationJson.hashCode());

                // shoe message
                String message = context.getString(R.string.msg_you_took, medication.getName());
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();

            }
        }
    }
}