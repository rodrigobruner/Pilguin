package app.bruner.library.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;

/**
 * MedicationRepository class to deal with medication data
 */
public class MedicationRepository {

    private static MedicationRepository instance; // singleton

    private final MutableLiveData<ArrayList<Medication>> medicationsLiveData = new MutableLiveData<>();

    private MedicationRepository() {}

    public static synchronized MedicationRepository getInstance() {
        if (instance == null) {
            instance = new MedicationRepository();
        }
        return instance;
    }

    public LiveData<ArrayList<Medication>> getMedications(Context context) {
        medicationsLiveData.setValue(MedicationUtils.getAll(context));
        return medicationsLiveData;
    }

    public void addMedication(Context context, Medication medication) {
        MedicationUtils.add(context, medication);
        medicationsLiveData.setValue(MedicationUtils.getAll(context));
    }

    public void deleteMedication(Context context, long medicationId) {
        MedicationUtils.delete(context, medicationId);
        medicationsLiveData.setValue(MedicationUtils.getAll(context));
    }

    public void saveMedications(Context context, List<Medication> medicationList) {
        MedicationUtils.save(context, medicationList, true);
        medicationsLiveData.setValue(MedicationUtils.getAll(context));
    }

    public void getNextMedications(Context context){
        // get all medications
        ArrayList<Medication> nextMedications = MedicationUtils.getAll(context);
        //sort medications by schedule nextTime
        nextMedications.sort((m1, m2) -> {  // foreach pair medication,

            // if no schedule unchanged
            if (m1.getSchedule() == null || m2.getSchedule() == null) {
                return 0;
            }

            // order by nextTime ascenting
            return m1.getSchedule().getNextTime().compareTo(m2.getSchedule().getNextTime());
        });
        medicationsLiveData.setValue(nextMedications);
    }
}