package app.bruner.pillguin.ui.medication;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;
import app.bruner.library.models.Medication;
import app.bruner.library.repository.MedicationRepository;

public class MedicationViewModel extends AndroidViewModel {

    private final MedicationRepository repository;
    private final LiveData<ArrayList<Medication>> medications;

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        repository = new MedicationRepository();
        medications = repository.getMedications(application.getApplicationContext());
    }

    public LiveData<ArrayList<Medication>> getMedications() {
        return medications;
    }

    public void deleteMedication(long medicationId) {
        repository.deleteMedication(getApplication().getApplicationContext(), medicationId);
    }
}