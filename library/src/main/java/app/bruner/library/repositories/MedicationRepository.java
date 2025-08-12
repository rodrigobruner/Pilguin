package app.bruner.library.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import app.bruner.library.models.Medication;
import app.bruner.library.models.Schedule;
import app.bruner.library.utils.MedicationUtils;

/**
 * Repository for managing medications
 */
public class MedicationRepository {

    // singleton instance
    private static MedicationRepository instance;

    // LiveData for medications
    private MutableLiveData<ArrayList<Medication>> medications = new MutableLiveData<>();

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
        if (medications.getValue() == null) {
            medications.setValue(MedicationUtils.getAll(context));
        }
        return medications;
    }

    // get medication by ID
    public LiveData<Medication> getMedicationById(Context context, long medicationId) {
        MutableLiveData<Medication> medicationLiveData = new MutableLiveData<>();

        // get all medications and compare ID
        ArrayList<Medication> allMedications = MedicationUtils.getAll(context);
        for (Medication med : allMedications) {
            if (med.getId() == medicationId) {
                medicationLiveData.setValue(med);
                return medicationLiveData;
            }
        }

        // If not found, return null
        medicationLiveData.setValue(null);
        return medicationLiveData;
    }

    // add a new medication
    public void addMedication(Context context, Medication medication) {
        MedicationUtils.add(context, medication);
        // Update LiveData after addition
        medications.setValue(MedicationUtils.getAll(context));
    }

    // delete a medication by ID
    public void deleteMedication(Context context, long medicationId) {
        MedicationUtils.delete(context, medicationId);
        // Update LiveData after deletion
        medications.setValue(MedicationUtils.getAll(context));
    }

    // update a medication
    public void updateMedication(Context context, Medication medication) {
        MedicationUtils.update(context, medication);
        // Update LiveData after update
        medications.setValue(MedicationUtils.getAll(context));
    }

    // get next medications sorted by next time
    public LiveData<ArrayList<Medication>> getNextMedications(Context context){
        ArrayList<Medication> nextMedications = MedicationUtils.getAll(context);
        nextMedications.sort((med1, med2) -> {
            // Use the same null-safe approach
            if (med1 == null || med2 == null) {
                return 0;
            }

            Date nextTime1 = getNextTimeDate(med1);
            Date nextTime2 = getNextTimeDate(med2);

            // Handle null dates
            if (nextTime1 == null && nextTime2 == null) {
                return 0;
            }
            if (nextTime1 == null) {
                return 1; // Put null dates at the end
            }
            if (nextTime2 == null) {
                return -1;
            }

            return nextTime1.compareTo(nextTime2);
        });

        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(nextMedications);
        return data;
    }

    // get the next time, prevent null problems
    private Date getNextTimeDate(Medication medication) {
        if (medication == null ||
                medication.getSchedule() == null) {
            return null;
        }
        return medication.getSchedule().getNextTime();
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

        Medication latestMedication = null;
        Date latestDate = null;

        for (Medication med : all) {
            if (isValidLastTaken(med)) {
                Date lastTaken = getLastTakenDate(med);
                if (lastTaken != null && (latestDate == null || lastTaken.after(latestDate))) {
                    latestDate = lastTaken;
                    latestMedication = med;
                }
            }
        }

        ArrayList<Medication> result = new ArrayList<>();
        if (latestMedication != null) {
            result.add(latestMedication);
        }

        data.setValue(result);
        return data;
    }

    // check if the values is not null
    private boolean isValidLastTaken(Medication medication) {
        return medication != null &&
                medication.getSchedule() != null &&
                medication.getSchedule().getWhenTook() != null &&
                !medication.getSchedule().getWhenTook().isEmpty();
    }

    // get the last taken date from the medication
    private Date getLastTakenDate(Medication medication) {
        if (!isValidLastTaken(medication)) {
            return null;
        }
        ArrayList<Date> whenTook = medication.getSchedule().getWhenTook();
        return whenTook.get(whenTook.size() - 1);
    }
}