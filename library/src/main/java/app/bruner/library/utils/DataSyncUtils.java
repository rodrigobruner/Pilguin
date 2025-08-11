package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import java.util.ArrayList;

import app.bruner.library.models.Medication;

public class DataSyncUtils {

    private static final String TAG = "DataSyncUtils";
    private static final String MEDICATIONS_SYNC_PATH = "/medications_sync";
    private static final String SOURCE_PHONE_TO_WATCH = "phone_to_watch";
    private static final String SOURCE_WATCH_TO_PHONE = "watch_to_phone";
    private static Gson gson = new Gson();


    // send from phone to watch
    public static void sendMedicationsToWatch(Context context, String medicationsJson) {
        sendData(context, medicationsJson, SOURCE_PHONE_TO_WATCH);
    }

    // NOVO: send from watch to phone
    public static void sendMedicationsToPhone(Context context, String medicationsJson) {
        sendData(context, medicationsJson, SOURCE_WATCH_TO_PHONE);
    }


    // Generic method to send data
    private static void sendData(Context context, String medicationsJson, String source) {

        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(MEDICATIONS_SYNC_PATH);
        DataMap dataMap = putDataMapRequest.getDataMap();
        dataMap.putString("medications", medicationsJson);
        dataMap.putString("source", source);
        dataMap.putLong("timestamp", System.currentTimeMillis());

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        putDataRequest.setUrgent();

        Task<DataItem> putDataTask = Wearable.getDataClient(context).putDataItem(putDataRequest);
        putDataTask.addOnSuccessListener(dataItem -> {
            Log.d(TAG, "sent from " + source);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "FAIL data from " + source, e);
        });
    }


    // send update to the other device
    public static void sendUpdate(Context context) {
        ArrayList<Medication> medications = MedicationUtils.getAll(context);
        String medicationsJson = gson.toJson(medications);

        // check if is a watch or phone
        if (isWatchDevice(context)) {
            // if watch send to phone
            DataSyncUtils.sendMedicationsToPhone(context, medicationsJson);
        } else {
            // if phone send to watch
            DataSyncUtils.sendMedicationsToWatch(context, medicationsJson);
        }
    }

    // check if the device is a watch
    private static boolean isWatchDevice(Context context) {
        try {
            // check the feature for watch
            boolean isWatch = context.getPackageManager().hasSystemFeature("android.hardware.type.watch");

            // check package name
            String packageName = context.getPackageName();
            boolean isWatchPackage = packageName != null && packageName.contains("watch");

            if (isWatch || isWatchPackage) { // if it is a watch, return true
                return true;
            } else {
                return false; // mobile
            }
        } catch (Exception e) {
            return false;
        }
    }

    // receive updates from the other device
    public static void receiveUpdate(Context context, DataMap dataMap) {
        if (dataMap.containsKey("medications")) {
            String json = dataMap.getString("medications");
            String source = dataMap.getString("source", "unknown");

            // save the medications to SharedPreferences
            // I not use the MedicationUtils.save() to avoid loop
            SharedPreferences prefs = context.getSharedPreferences(MedicationUtils.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(MedicationUtils.KEY_MEDICATION_LIST, json);
            editor.apply();
        }
    }
}