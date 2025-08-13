package app.bruner.watch.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.wear.widget.WearableLinearLayoutManager;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.watch.adapter.MedicationAdapter;
import app.bruner.watch.databinding.ActivityLatestMedicationBinding;

/**
 * Activity that show the latest medications taken
 */
public class LatestMedicationActivity extends AppCompatActivity {

    private ActivityLatestMedicationBinding binding;
    private ArrayList<Medication> medicationList = new ArrayList<>();
    private MedicationViewModel viewModel;
    private MedicationAdapter adapter;

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

    // setup RecyclerView
    private void setupRecyclerView() {
        binding.rcvMedications.setLayoutManager(new androidx.wear.widget.WearableLinearLayoutManager(this));
        adapter = new MedicationAdapter(this);
        binding.rcvMedications.setAdapter(adapter);
        binding.rcvMedications.setEdgeItemsCenteringEnabled(true);
    }

    private void observeMedications() {
        // observe the latest medications taken
        viewModel.getLatestMedication(getApplicationContext()).observe(this, medications -> {
            adapter.setShowNextTime(false);
            adapter.setMedications(medications);

            // show/hide no medication text
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