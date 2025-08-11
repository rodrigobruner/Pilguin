package app.bruner.pillguin.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.ActivityHomeBinding;
import app.bruner.pillguin.ui.medication.AddMedicationFragment;
import app.bruner.pillguin.ui.medication.MedicationDetailFragment;
import app.bruner.pillguin.ui.medication.MedicationDetailsFragment;
import app.bruner.pillguin.ui.medication.MedicationFragment;
import app.bruner.pillguin.utils.Constants;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;

    private static final String EXTRA_MEDICATION = "medication";
    private static final String EXTRA_NAVIGATION = "navigation_target";
    private static final String EXTRA_NAVIGATION_VALUE_DETAILS = "medication_details";
    private static final String EXTRA_NAVIGATION_VALUE_TOOK = "medication_took_it";

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
        requestPermitions();

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

        handleParams();

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

    // handle parameters passed to this activity by notification actions
    private void handleParams() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // get params
            String navigation = extras.getString(EXTRA_NAVIGATION);
            String medicationJson = extras.getString(EXTRA_MEDICATION);

            if (medicationJson != null) { // if have medication
                Medication medication = new Gson().fromJson(medicationJson, Medication.class); // create medication

                if (medication != null && EXTRA_NAVIGATION_VALUE_DETAILS.equals(navigation)) { // navigate to medication details
                    MedicationDetailFragment detailFragment = MedicationDetailFragment.newInstance(medication);
                    setFragment(detailFragment);

                } else if (medication != null && medication.getSchedule() != null && EXTRA_NAVIGATION_VALUE_TOOK.equals(navigation)) { // took medication action
                    medication.getSchedule().addWhenTook(new Date());
                    MedicationUtils.update(getApplicationContext(), medication);
                    Toast.makeText(getApplicationContext(), getString(R.string.msg_you_took) + medication.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void requestPermitions() {
        // Solicitar apenas POST_NOTIFICATIONS (única que precisa de dialog)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33+
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {

                String[] permissions = { Manifest.permission.POST_NOTIFICATIONS };
                ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_CODE);
            }
        }

        // Tratar SCHEDULE_EXACT_ALARM separadamente
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }
    }

    // deal with the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMISSION_CODE) { // Check if all permissions were granted
            for (int i = 0; i < permissions.length; i++) {
                // If the permission denied, show a toast
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    String perm = permissions[i];
                    String msg = getString(R.string.txt_permition_denied) + perm;
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}