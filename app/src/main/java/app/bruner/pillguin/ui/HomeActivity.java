package app.bruner.pillguin.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.ActivityHomeBinding;
import app.bruner.pillguin.ui.medication.AddMedicationFragment;
import app.bruner.pillguin.ui.medication.MedicationFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        // start with the MedicationFragment
        setFragment(new MedicationFragment());

        // set up navigation bar buttons
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.medicine) { //medicine
                setFragment(new MedicationFragment());
                return true;
            } else if (item.getItemId() == R.id.reports) { //reports
                setFragment(new ReportFragment());
                return true;
            }
            return false;
        });

        // set up add medicine button
        binding.btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new AddMedicationFragment());
            }
        });

    }

    // set a fragment on frame layout
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}