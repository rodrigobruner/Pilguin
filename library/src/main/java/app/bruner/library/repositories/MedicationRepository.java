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

        // if not found, return null
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
            // check null value
            if (med1 == null || med2 == null) {
                return 0;
            }

            Date nextTime1 = getNextTimeDate(med1);
            Date nextTime2 = getNextTimeDate(med2);

            // check null value
            if (nextTime1 == null && nextTime2 == null) {
                return 0; // equal
            }
            if (nextTime1 == null) {
                return 1; // after, 1 < 2
            }
            if (nextTime2 == null) {
                return -1; // before, 1 > 2
            }

            return nextTime1.compareTo(nextTime2);  // compare
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
    public LiveData<ArrayList<Medication>> getTodayMedications(Context context) {
        ArrayList<Medication> all = MedicationUtils.getAll(context);
        ArrayList<Medication> todayMedications = new ArrayList<>();

        // get current date
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date todayStart = today.getTime();

        today.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrowStart = today.getTime(); // start next day

        for (Medication med : all) { // foreach medication

            // skip null
            if (med == null || med.getSchedule() == null) {
                continue;
            }

            Schedule schedule = med.getSchedule(); // get schedule

            // flags to check
            boolean isScheduledToday = false;
            boolean wasTakenToday = false;

            // check if scheduled for today
            Date nextTime = schedule.getNextTime();
            if (nextTime != null &&
                    nextTime.compareTo(todayStart) >= 0 &&
                    nextTime.compareTo(tomorrowStart) < 0) {
                isScheduledToday = true;
            }

            // check if taken today
            ArrayList<Date> whenTook = schedule.getWhenTook();
            if (whenTook != null && !whenTook.isEmpty()) {
                Date lastTaken = whenTook.get(whenTook.size() - 1);
                if (lastTaken != null &&
                        lastTaken.compareTo(todayStart) >= 0 &&
                        lastTaken.compareTo(tomorrowStart) < 0) {
                    wasTakenToday = true;
                }
            }

            // add if one of the conditions is true
            if (isScheduledToday || wasTakenToday) {
                todayMedications.add(med);
            }
        }

        // Sort by next time (nulls last)
        todayMedications.sort((med1, med2) -> {
            Date nextTime1 = med1.getSchedule().getNextTime();
            Date nextTime2 = med2.getSchedule().getNextTime();

            // check null value
            if (nextTime1 == null && nextTime2 == null) {
                return 0; // equal
            }
            if (nextTime1 == null) {
                return 1; // after, 1 < 2
            }
            if (nextTime2 == null) {
                return -1; // before, 1 > 2
            }

            //compare
            return nextTime1.compareTo(nextTime2);
        });

        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(todayMedications);
        return data;
    }

    // get the latest medication
    public LiveData<ArrayList<Medication>> getLatestMedication(Context context) {

        ArrayList<Medication> all = MedicationUtils.getAll(context); // get all medications
        ArrayList<Medication> sortedMedications = new ArrayList<>();

        for (Medication med : all) { // foreach medication

            if (isValidLastTaken(med)) { // check if have last taken date
                Date lastTaken = getLastTakenDate(med);

                int insertPosition = 0;
                for (int i = 0; i < sortedMedications.size(); i++) { // order insert
                    Date existingLastTaken = getLastTakenDate(sortedMedications.get(i));

                    // If current medication is older, insert here
                    if (lastTaken.before(existingLastTaken)) {
                        insertPosition = i + 1;
                    } else {
                        break;
                    }
                }

                sortedMedications.add(insertPosition, med);
            }
        }

        MutableLiveData<ArrayList<Medication>> data = new MutableLiveData<>();
        data.setValue(sortedMedications);
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