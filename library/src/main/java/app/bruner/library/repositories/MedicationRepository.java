package app.bruner.library.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;
import app.bruner.library.utils.MedicationUtils;

/**
 * Repository for managing medications
 */
public class MedicationRepository {

    // singleton instance
    private static MedicationRepository instance;

    private MedicationRepository() {

    }

    // get singleton instance
    public static synchronized MedicationRepository getInstance() {
        if (instance == null) {
            instance = new MedicationRepository();
        }
        return instance;
    }

    // get all medications
    public LiveData<ArrayList<Medication>> getMedications(Context context) {
        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(MedicationUtils.getAll(context));
        return data;
    }

    // add a new medication
    public void addMedication(Context context, Medication medication) {
        MedicationUtils.add(context, medication);
    }

    // delete a medication by ID
    public void deleteMedication(Context context, long medicationId) {
        MedicationUtils.delete(context, medicationId);
    }

    // get next medications sorted by next time
    public LiveData<ArrayList<Medication>> getNextMedications(Context context){
        ArrayList<Medication> nextMedications = MedicationUtils.getAll(context);
        nextMedications.sort((med1, med2) -> {
            if (med1.getSchedule() == null || med2.getSchedule() == null) {
                return 0;
            }
            return med1.getSchedule().getNextTime().compareTo(med2.getSchedule().getNextTime());
        });
        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(nextMedications);
        return data;
    }

    // get medications scheduled for today
    public LiveData<ArrayList<Medication>> getMedicationsForToday(Context context) {
        ArrayList<Medication> all = MedicationUtils.getAll(context);
        ArrayList<Medication> todayList = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        for (Medication med : all) {
            Schedule schedule = med.getSchedule();
            if (schedule != null && schedule.getNextTime() != null) {
                Calendar next = Calendar.getInstance();
                next.setTime(schedule.getNextTime());
                next.set(Calendar.HOUR_OF_DAY, 0);
                next.set(Calendar.MINUTE, 0);
                next.set(Calendar.SECOND, 0);
                next.set(Calendar.MILLISECOND, 0);

                if (today.getTime().equals(next.getTime())) {
                    todayList.add(med);
                }
            }
        }
        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(todayList);
        return data;
    }

    // get the latest medication based on the last time it was taken
    public LiveData<ArrayList<Medication>> getLatestMedication(Context context) {
        ArrayList<Medication> all = MedicationUtils.getAll(context);
        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        if (all.isEmpty()) {
            data.setValue(new ArrayList<>());
            return data;
        }
        all.sort((med1, med2) -> {
            if (med1.getSchedule() == null || med2.getSchedule() == null) {
                return 0;
            }
            ArrayList<Date> list1 = med1.getSchedule().getWhenTook();
            ArrayList<Date> list2 = med2.getSchedule().getWhenTook();
            if (list1 == null || list1.isEmpty() || list2 == null || list2.isEmpty()) {
                return 0;
            }
            return list1.get(list1.size() - 1).compareTo(list2.get(list2.size() - 1));
        });
        ArrayList<Medication> latest = new ArrayList<>();
        latest.add(all.get(0));
        data.setValue(latest);
        return data;
    }
}