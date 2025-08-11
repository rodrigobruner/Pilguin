package app.bruner.library.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import app.bruner.library.models.Medication;
import app.bruner.library.repositories.MedicationRepository;

/**
 * MedicationViewModel
 */
public class MedicationViewModel extends AndroidViewModel {

    private final MedicationRepository repository;
    private final LiveData<ArrayList<Medication>> medications;

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        repository = MedicationRepository.getInstance();
        medications = repository.getMedications(application.getApplicationContext());
    }

    public LiveData<ArrayList<Medication>> getMedications() {
        return medications;
    }

    public void deleteMedication(long medicationId) {
        repository.deleteMedication(getApplication().getApplicationContext(), medicationId);

    }

    public void getNextMedications() {
        repository.getNextMedications(getApplication().getApplicationContext());
    }

    public void addMedication(Medication medication) {
        repository.addMedication(getApplication().getApplicationContext(), medication);
    }
}