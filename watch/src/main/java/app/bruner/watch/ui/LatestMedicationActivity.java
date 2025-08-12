package app.bruner.watch.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.wear.widget.WearableLinearLayoutManager;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.watch.adapter.LatestMedicationAdapter;
import app.bruner.watch.databinding.ActivityLatestMedicationBinding;

public class LatestMedicationActivity extends AppCompatActivity {

    private ActivityLatestMedicationBinding binding;
    private ArrayList<Medication> medicationList = new ArrayList<>();
    private MedicationViewModel viewModel;
    private LatestMedicationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLatestMedicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        setupRecyclerView();
        observeMedications();
    }

    private void setupRecyclerView() {
        binding.rcvMedications.setLayoutManager(new WearableLinearLayoutManager(this));
        adapter = new LatestMedicationAdapter(this);
        binding.rcvMedications.setAdapter(adapter);
        binding.rcvMedications.setEdgeItemsCenteringEnabled(true);
    }

    private void observeMedications() {
        viewModel.getMedications().observe(this, medications -> {
            adapter.setMedications(medications);
            if (medications != null && !medications.isEmpty()) {
                binding.rcvMedications.setVisibility(View.VISIBLE);
                binding.txtNoMedication.setVisibility(View.GONE);
            } else {
                binding.rcvMedications.setVisibility(View.GONE);
                binding.txtNoMedication.setVisibility(View.VISIBLE);
            }
        });
    }
}