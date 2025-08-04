package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.MedicationDiary;

public class MedicationDiaryUtils {

    private static final String SHARED_PREFS_NAME = "medication_diary_prefs";
    private static final String KEY_MEDICATION_DIARY_LIST = "medication_diary_list";

    private static Gson gson = new Gson();



    // get all registers
    public static ArrayList<MedicationDiary> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_MEDICATION_DIARY_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<MedicationDiary>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // get regiters by medication ID
    public static ArrayList<MedicationDiary> getByMedication(Context context, long medicationId) {
        List<MedicationDiary> allEntries = getAll(context);
        ArrayList<MedicationDiary> medicationEntries = new ArrayList<>();
        for (MedicationDiary entry : allEntries) {
            if (entry.getMedicationId() == medicationId) {
                medicationEntries.add(entry);
            }
        }
        return medicationEntries;
    }

    // save
    public static void save(Context context, List<MedicationDiary> medicationDiaryList) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(medicationDiaryList);
        editor.putString(KEY_MEDICATION_DIARY_LIST, json);
        editor.apply();
    }

    // delete a register by ID
    public static void delete(Context context, long diaryId) {
        List<MedicationDiary> list = getAll(context);
        List<MedicationDiary> updatedList = new ArrayList<>();
        for (MedicationDiary entry : list) {
            if (entry.getId() != diaryId) {
                updatedList.add(entry);
            }
        }
        save(context, updatedList);
    }

    // add a register
    public static void add(Context context, MedicationDiary medicationDiary) {
        List<MedicationDiary> list = getAll(context);
        list.add(medicationDiary);
        save(context, list);
    }
}