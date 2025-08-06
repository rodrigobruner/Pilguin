package app.bruner.library.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;

public class MedicationRepository {

    private final MutableLiveData<ArrayList<Medication>> medicationsLiveData = new MutableLiveData<>();

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
        MedicationUtils.save(context, medicationList);
        medicationsLiveData.setValue(MedicationUtils.getAll(context));
    }
}