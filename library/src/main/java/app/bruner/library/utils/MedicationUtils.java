package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.bruner.library.models.Medication;

/**
 * Deal with medication data in SharedPreferences.
 */
public class MedicationUtils {

    public static final String SHARED_PREFS_NAME = "medications";
    public static final String KEY_MEDICATION_LIST = "medication_list";

    private static Gson gson = new Gson();

    //save with option to sync or not
    public static void save(Context context, List<Medication> medicationList, boolean shouldSync) {
        // get the shared preferences
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit(); // create the editor
        String json = gson.toJson(medicationList); // convert the list to JSON
        editor.putString(KEY_MEDICATION_LIST, json); // save the JSON string
        editor.apply();

        if (shouldSync) { // sync the data if needed
            Log.d("Sync service", "MedicationUtils: send update!");
            DataSyncUtils.sendUpdate(context); // send update to the other device
        }
    }

    // get the medication list
    public static ArrayList<Medication> getAll(Context context) {
        // get the shared preferences
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEDICATION_LIST, null); // get the JSON string
        if (json == null) {
            return new ArrayList<>();
        }
        // convert the JSON string back to a list
        Medication[] medsArray = gson.fromJson(json, Medication[].class);
        ArrayList<Medication> list = new ArrayList<>(Arrays.asList(medsArray));
        return list;
    }

    // delete a medication by ID
    public static void delete(Context context, long medicationId) {
        List<Medication> list = getAll(context); // get the current list
        List<Medication> updatedList = new ArrayList<>(); // create a new list
        for (Medication med : list) {
            // if the ID not match keep on the medication list
            if (med.getId() != medicationId) {
                updatedList.add(med);
            }
        }
        // save the updated list and sync
        save(context, updatedList, true);
    }

    // add a medication
    public static void add(Context context, Medication medication) {
        List<Medication> list = getAll(context);
        list.add(medication);
        save(context, list, true);
    }

    // update a medication
    public static void update(Context context, Medication medication) {
        delete(context, medication.getId());
        add(context, medication);
    }
}