package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.Medication;

public class MedicationUtils {

    private static final String SHARED_PREFS_NAME = "medication_prefs";
    private static final String KEY_MEDICATION_LIST = "medication_list";

    private static Gson gson = new Gson();

    // save the medication
    public static void saveMedicationList(Context context, List<Medication> medicationList) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(medicationList);
        editor.putString(KEY_MEDICATION_LIST, json);
        editor.apply();
    }

    // get the medication list
    public static ArrayList<Medication> getMedicationList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEDICATION_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Medication>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // add a medication by ID
    public static void deleteMedication(Context context, long medicationId) {
        List<Medication> list = getMedicationList(context);
        List<Medication> updatedList = new ArrayList<>();
        for (Medication med : list) {
            if (med.getId() != medicationId) {
                updatedList.add(med);
            }
        }
        saveMedicationList(context, updatedList);
    }
}