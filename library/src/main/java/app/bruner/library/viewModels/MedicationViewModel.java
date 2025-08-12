package app.bruner.library.viewModels;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import app.bruner.library.models.Medication;
import app.bruner.library.repositories.MedicationRepository;

/**
 * ViewModel to deal with medication data.
 */
public class MedicationViewModel extends AndroidViewModel {

    private final MedicationRepository repository;
    private LiveData<ArrayList<Medication>> medications;

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        repository = MedicationRepository.getInstance();
        medications = repository.getMedications(getApplication().getApplicationContext());
    }

    // get all medications
    public LiveData<ArrayList<Medication>> getMedications() {
        if (medications == null) {
            medications = repository.getMedications(getApplication().getApplicationContext());
        }
        return medications;
    }

    // get medication by ID
    public LiveData<Medication> getMedicationById(long medicationId) {
        return repository.getMedicationById(getApplication().getApplicationContext(), medicationId);
    }

    // delete medication and refresh data
    public void deleteMedication(long medicationId) {
        repository.deleteMedication(getApplication().getApplicationContext(), medicationId);
    }

    // add medication and refresh data
    public void addMedication(Medication medication) {
        repository.addMedication(getApplication().getApplicationContext(), medication);
    }

    // update medication and refresh data
    public void updateMedication(Medication medication) {
        repository.updateMedication(getApplication().getApplicationContext(), medication);
    }

    // get medications for today
    public LiveData<ArrayList<Medication>> getTodayMedications() {
        return repository.getTodayMedications(getApplication().getApplicationContext());
    }

    // get latest medication
    public LiveData<ArrayList<Medication>> getLatestMedication(Context context) {
        return repository.getLatestMedication(context);
    }

    // get next medications
    public LiveData<ArrayList<Medication>> getNextMedications() {
        return repository.getNextMedications(getApplication().getApplicationContext());
    }
}