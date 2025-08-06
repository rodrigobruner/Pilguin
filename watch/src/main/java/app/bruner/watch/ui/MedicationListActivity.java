package app.bruner.watch.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.WearableRecyclerView;

import java.util.ArrayList;

import app.bruner.library.models.Medication;
import app.bruner.watch.adapter.MedicationAdapter;
import app.bruner.watch.databinding.ActivityMedicationListBinding;
import app.bruner.library.moc.MedicationsMoc;

public class MedicationListActivity extends AppCompatActivity {

    ActivityMedicationListBinding binding;

    ArrayList<Medication> medicationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        getMedicationList();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        WearableRecyclerView recyclerView = binding.rcvMedications;

        // Adicione o LayoutManager
        recyclerView.setLayoutManager(new androidx.wear.widget.WearableLinearLayoutManager(this));

        MedicationAdapter adapter = new MedicationAdapter(getBaseContext(), medicationList);
        recyclerView.setAdapter(adapter);
        recyclerView.setEdgeItemsCenteringEnabled(true);
    }

    private void getMedicationList() {
        medicationList = MedicationsMoc.getMedications();

    }
}