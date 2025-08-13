package app.bruner.pillguin.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import app.bruner.library.models.Medication;
import app.bruner.pillguin.R;
import app.bruner.pillguin.ui.HomeActivity;

public class SideEffectAlarmReceiver extends BroadcastReceiver {

    // constant for medication notification
    private static final String MEDICATION_PARAMETER = "MEDICATION";
    private static final String MEDICATION_CHANNEL_ID = "medication_side_effect";
    private static final String MEDICATION_CHANNEL_NAME = "Medication side effect tracking";

    // constant for intent
    private static final String EXTRA_MEDICATION = "medication";
    private static final String EXTRA_NAVIGATION = "navigation_target";
    private static final String EXTRA_NAVIGATION_VALUE_DETAILS = "medication_details";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SideEffectAlarmReceiver", "onReceive called");

        // get the medication from the intent
        String stringMedication = intent.getStringExtra(MEDICATION_PARAMETER);
        Medication medication = new Gson().fromJson(stringMedication, Medication.class);

        // check notification permission
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // =========== APP

        // create pending intents to show medication detail on app
        PendingIntent appShowMedicationIntent = createPendingIntent(
                context,
                medication,
                EXTRA_NAVIGATION_VALUE_DETAILS,
                HomeActivity.class
        );

        // =========== WATCH

        // create pending intent to show medication detail on watch
        // FIXME: not working yet, how to open an activity on watch from phone?
        PendingIntent watchShowMedicationIntent = createPendingIntent(
                context,
                medication,
                EXTRA_NAVIGATION_VALUE_DETAILS,
                HomeActivity.class
        );

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .addAction(new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_menu_agenda,
                        context.getString(R.string.txt_notification_action_report_side_effect),
                        watchShowMedicationIntent
                ).build());


        // =========== NOTIFICATION

        // build notification title and message
        String title = context.getString(R.string.txt_notification_side_effect_title);
        String message = context.getString(R.string.txt_notification_side_effect_msg, medication.getName());

        // create notification channel
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                MEDICATION_CHANNEL_ID,
                MEDICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);


        // build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MEDICATION_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // set small icon
                .setContentTitle(title) //set title
                .setContentText(message) //set message
                .setPriority(NotificationCompat.PRIORITY_HIGH) // set priority
                .setContentIntent(appShowMedicationIntent) // set intent to show medication detail on app
                .setAutoCancel(true) // close notification when clicked
                .setCategory(NotificationCompat.CATEGORY_ALARM) // set category for alarm
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // set visibility for public
                // set buttons for message on app
                .addAction(android.R.drawable.ic_menu_view,
                        context.getString(R.string.txt_notification_action_report_side_effect),
                        appShowMedicationIntent)
                .extend(wearableExtender); // add buttons for watch

        // Show notification
        notificationManager.notify(stringMedication.hashCode(), builder.build());
    }

    // create a pending intent for app to shows medication details
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
}
