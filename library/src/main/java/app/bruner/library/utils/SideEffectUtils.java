package app.bruner.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.bruner.library.models.SideEffect;

public class SideEffectUtils {

    private static final String SHARED_PREFS_NAME = "side_effect_prefs";
    private static final String KEY_SIDE_EFFECT_LIST = "side_effect_list";

    private static Gson gson = new Gson();



    // get all side effects
    public static ArrayList<SideEffect> getAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_SIDE_EFFECT_LIST, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<SideEffect>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // get side effects by medication
    public static ArrayList<SideEffect> getByMedication(Context context, long medicationId) {
        List<SideEffect> allSideEffects = getAll(context);
        ArrayList<SideEffect> medicationSideEffects = new ArrayList<>();
        for (SideEffect sideEffect : allSideEffects) {
            if (sideEffect.getMedicationId() == medicationId) {
                medicationSideEffects.add(sideEffect);
            }
        }
        return medicationSideEffects;
    }

    // save side effect list
    public static void save(Context context, List<SideEffect> sideEffectList) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(sideEffectList);
        editor.putString(KEY_SIDE_EFFECT_LIST, json);
        editor.apply();
    }

    //add a side effect
    public static void add(Context context, SideEffect sideEffect) {
        List<SideEffect> list = getAll(context);
        list.add(sideEffect);
        save(context, list);
    }

    // delete side effect
    public static void delete(Context context, long sideEffectId) {
        List<SideEffect> list = getAll(context);
        List<SideEffect> updatedList = new ArrayList<>();
        for (SideEffect sideEffect : list) {
            if (sideEffect.getId() != sideEffectId) {
                updatedList.add(sideEffect);
            }
        }
        save(context, updatedList);
    }
}