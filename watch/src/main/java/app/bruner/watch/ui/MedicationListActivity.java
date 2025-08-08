package app.bruner.watch.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableRecyclerView;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.watch.adapter.MedicationAdapter;
import app.bruner.watch.databinding.ActivityMedicationListBinding;
import app.bruner.library.viewModels.MedicationViewModel;

public class MedicationListActivity extends AppCompatActivity {

    private ActivityMedicationListBinding binding;

    private ArrayList<Medication> medicationList = new ArrayList<>();

    private MedicationViewModel viewModel;

    private MedicationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        setupRecyclerView();
        observeMedications();
    }

    private void setupRecyclerView() {
        binding.rcvMedications.setLayoutManager(new androidx.wear.widget.WearableLinearLayoutManager(this));
        adapter = new MedicationAdapter(this);
        binding.rcvMedications.setAdapter(adapter);
        binding.rcvMedications.setEdgeItemsCenteringEnabled(true);
    }

    private void observeMedications() {
        viewModel.getMedications().observe(this, medications -> {
            adapter.setMedications(medications);
        });
    }
}