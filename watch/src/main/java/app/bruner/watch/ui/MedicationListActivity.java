package app.bruner.watch.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableRecyclerView;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.watch.adapter.MedicationAdapter;
import app.bruner.watch.databinding.ActivityMedicationListBinding;
import app.bruner.library.viewModels.MedicationViewModel;

/**
 * Activity to show medication list
 */
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

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        // initialize view model
        viewModel = new ViewModelProvider(this).get(MedicationViewModel.class);
        setupRecyclerView();
        observeMedications();
    }

    // set up the recycler view
    private void setupRecyclerView() {
        binding.rcvMedications.setLayoutManager(new androidx.wear.widget.WearableLinearLayoutManager(this));
        adapter = new MedicationAdapter(this);
        binding.rcvMedications.setAdapter(adapter);
        binding.rcvMedications.setEdgeItemsCenteringEnabled(true);
    }

    // observe medications from view model
    private void observeMedications() {
        // observe today medication
        viewModel.getTodayMedications().observe(this, medications -> {
            if (medications != null) {
                adapter.setMedications(medications);
                if (!medications.isEmpty()) { // if not empty show recycler view
                    binding.rcvMedications.setVisibility(View.VISIBLE);
                    binding.txtNoMedication.setVisibility(View.GONE);
                } else { // if empty show no medication text
                    binding.rcvMedications.setVisibility(View.GONE);
                    binding.txtNoMedication.setVisibility(View.VISIBLE);
                }
            } else { // if null show no medication text
                binding.rcvMedications.setVisibility(View.GONE);
                binding.txtNoMedication.setVisibility(View.VISIBLE);
            }
        });
    }
}