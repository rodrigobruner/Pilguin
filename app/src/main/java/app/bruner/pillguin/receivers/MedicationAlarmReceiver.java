package app.bruner.pillguin.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.R;
import app.bruner.pillguin.ui.HomeActivity;
import app.bruner.pillguin.ui.MainActivity;

public class MedicationAlarmReceiver extends BroadcastReceiver {

    private static final String MEDICATION_PARAMETER = "MEDICATION";
    private static final String MEDICATION_CHANNEL_ID = "medication_due_soon";
    private static final String MEDICATION_CHANNEL_NAME = "Medication due soon";

    private static final String EXTRA_MEDICATION = "medication";
    private static final String EXTRA_NAVIGATION = "navigation_target";
    private static final String EXTRA_NAVIGATION_VALUE_DETAILS = "medication_details";
    private static final String EXTRA_NAVIGATION_VALUE_TOOK = "medication_took_it";


    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the medication from the intent
        String stringMedication = intent.getStringExtra(MEDICATION_PARAMETER);
        Medication medication = new Gson().fromJson(stringMedication, Medication.class);

        if (medication == null) {
            return;
        }

        // Check notification permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // =========== APP
        PendingIntent appShowMedicationIntent = createPendingIntent(
                context,
                medication,
                EXTRA_NAVIGATION_VALUE_DETAILS,
                HomeActivity.class
        );

        PendingIntent appITookMedicationIntent = createMedicationTakenIntent(context, medication);

        // =========== WATCH

        PendingIntent watchShowMedicationIntent = createWatchPendingIntent(
                context,
                medication,
                "app.bruner.watch.SHOW_MEDICATION_DETAILS"
        );

        PendingIntent watchITookMedicationIntent2 = createMedicationTakenIntent(context, medication);

        // set wearable extender for watch
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_agenda,
                        context.getString(R.string.txt_notification_action_taken),
                        watchITookMedicationIntent2
                ).build())
                .addAction(new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_view,
                        context.getString(R.string.txt_notification_action_see_detail),
                        watchShowMedicationIntent
                ).build());


        // Build notification title and message
        String title = context.getString(R.string.txt_notification_title);
        String message = context.getString(R.string.txt_notification_msg, medication.getName());

        // Create notification channel
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                MEDICATION_CHANNEL_ID,
                MEDICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);

        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MEDICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(appShowMedicationIntent)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                // set buttons for message on app
                .addAction(android.R.drawable.ic_menu_agenda,
                        context.getString(R.string.txt_notification_action_taken),
                        appITookMedicationIntent)
                .addAction(android.R.drawable.ic_menu_view,
                        context.getString(R.string.txt_notification_action_see_detail),
                        appShowMedicationIntent)
                .extend(wearableExtender); // add buttons for watch

        // Show notification
        notificationManager.notify(stringMedication.hashCode(), builder.build());
    }

    private PendingIntent createPendingIntent(Context context, Medication medication, String action, Class className) {
        Intent intent = new Intent(context, className);
        intent.setAction(action);
        intent.putExtra(EXTRA_MEDICATION, new Gson().toJson(medication));
        intent.putExtra(EXTRA_NAVIGATION, action);

        // new intent for activits
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(), // generate a unique request code
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    private PendingIntent createWatchPendingIntent(Context context, Medication medication, String action) {
        Intent intent = new Intent();

        // use the action to differentiate between watch and phone
        intent.setAction("app.bruner.watch.MEDICATION_DETAILS");
        intent.setPackage("app.bruner.pillguin");

        intent.putExtra(EXTRA_MEDICATION, new Gson().toJson(medication));
        intent.putExtra(EXTRA_NAVIGATION, EXTRA_NAVIGATION_VALUE_DETAILS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(  // create a pending intent for the watch
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }

    // create a pending intent for took medication receiver
    private PendingIntent createMedicationTakenIntent(Context context, Medication medication) {
        Intent intent = new Intent(context, MedicationTakenReceiver.class);
        intent.putExtra(EXTRA_MEDICATION, new Gson().toJson(medication));

        return PendingIntent.getBroadcast(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}