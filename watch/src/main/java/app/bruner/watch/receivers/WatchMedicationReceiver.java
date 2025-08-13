package app.bruner.watch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import app.bruner.library.models.Medication;
import app.bruner.watch.ui.MedicationActivity;

/**
 * Receiver to deal with medication notification action on the watch
 * TODO: it does not work properly
 */
public class WatchMedicationReceiver extends BroadcastReceiver {

    private static final String TAG = "WatchMedicationReceiver";
    private static final String EXTRA_MEDICATION = "medication_obj";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "WatchMedicationReceiver run");

        String medicationJson = intent.getStringExtra(EXTRA_MEDICATION);
        if (medicationJson != null) {
            Medication medication = new Gson().fromJson(medicationJson, Medication.class);

            // Create intent to open MedicationActivity
            Intent activityIntent = new Intent(context, MedicationActivity.class);
            activityIntent.putExtra(EXTRA_MEDICATION, medicationJson);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            context.startActivity(activityIntent);
            Log.d(TAG, "open MedicationActivity for: " + medication.getName());
        }
    }
}