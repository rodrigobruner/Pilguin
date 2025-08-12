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

    public MedicationViewModel(@NonNull Application application) {
        super(application);
        repository = MedicationRepository.getInstance();
    }

    public LiveData<ArrayList<Medication>> getMedications() {
        return repository.getMedications(getApplication().getApplicationContext());
    }

    public void deleteMedication(long medicationId) {
        repository.deleteMedication(getApplication().getApplicationContext(), medicationId);
    }

    public void addMedication(Medication medication) {
        repository.addMedication(getApplication().getApplicationContext(), medication);
    }

    public LiveData<ArrayList<Medication>> getMedicationsForToday(Context context){
        return repository.getMedicationsForToday(context);
    }

    public LiveData<ArrayList<Medication>> getLatestMedication(Context context) {
        return repository.getLatestMedication(context);
    }

    public LiveData<ArrayList<Medication>> getNextMedications() {
        return repository.getNextMedications(getApplication().getApplicationContext());
    }
}