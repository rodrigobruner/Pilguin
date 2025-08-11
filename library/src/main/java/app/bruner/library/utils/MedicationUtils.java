package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.wearable.DataMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.Medication;

public class MedicationUtils {

    public static final String SHARED_PREFS_NAME = "medications";
    public static final String KEY_MEDICATION_LIST = "medication_list";

    private static Gson gson = new Gson();

    // save the medication
    public static void save(Context context, List<Medication> medicationList) {
        save(context, medicationList, true); // Por padrão, sincroniza
    }

    // NOVO: save com controle de sync
    public static void save(Context context, List<Medication> medicationList, boolean shouldSync) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(medicationList);
        editor.putString(KEY_MEDICATION_LIST, json);
        editor.apply();

        // Sync apenas se solicitado (evita loops)
        if (shouldSync) {
            DataSyncUtils.sendUpdate(context);
        }
    }

    // get the medication list
    public static ArrayList<Medication> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEDICATION_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Medication>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // delete a medication by ID
    public static void delete(Context context, long medicationId) {
        List<Medication> list = getAll(context);
        List<Medication> updatedList = new ArrayList<>();
        for (Medication med : list) {
            if (med.getId() != medicationId) {
                updatedList.add(med);
            }
        }
        save(context, updatedList);
    }

    // add a medication
    public static void add(Context context, Medication medication) {
        List<Medication> list = getAll(context);
        list.add(medication);
        save(context, list);
    }

    public static void update(Context context, Medication medication) {
        delete(context, medication.getId());
        add(context, medication);
    }
}