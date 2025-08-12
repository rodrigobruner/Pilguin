package app.bruner.watch.ui;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.utils.DateTimeParseUtils;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.library.utils.MedicineTypeIconMapper;
import app.bruner.library.viewModels.MedicationViewModel;
import app.bruner.watch.R;
import app.bruner.watch.databinding.ActivityMedicationBinding;
import utils.ConfirmUtils;

/**
 * Activity to show details of a medication
 */
public class MedicationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MEDICATION_PARAM = "medication_obj";

    ActivityMedicationBinding binding;

    MedicationViewModel viewModel;

    Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        // initialize view model
        viewModel = new MedicationViewModel(getApplication());
        getMedication();
        setupViews();
        binding.btnTookThisMedication.setOnClickListener(this);
        binding.btnReportSideEffects.setOnClickListener(this);
    }

    private void getMedication(){
        //get medication from intent
        medication = getIntent().getSerializableExtra(MEDICATION_PARAM, Medication.class);
        if(medication == null) { // if result is null
            // get first medication from database
            viewModel.getNextMedications().observe(this, medications -> {
                if (medications != null && !medications.isEmpty()) {
                    medication = medications.get(0); // first in the list
                    setupViews(); // setup views
                }
            });
            return;
        }
        binding.txtNextMedication.setVisibility(View.GONE);
    }

    // setup ui
    private void setupViews() {
        if (medication != null) {

            // set visibility of ui
            binding.txtNextMedication.setText(getString(R.string.txt_next_medication));
            binding.txtNextMedication.setTextColor(
                    getColor(app.bruner.library.R.color.light_red)
            );
            binding.linLayContent.setVisibility(View.VISIBLE);
            binding.buttons.setVisibility(View.VISIBLE);

            // set medication data
            binding.txtName.setText(medication.getName());
            binding.txtDosage.setText(medication.getDosage());
            binding.imgType.setImageResource(MedicineTypeIconMapper.getIconByType(getBaseContext(), medication.getType()));
            if (medication.getSchedule() != null) { // covert date to string
                Date nextTime = medication.getSchedule().getNextTime();
                String nextTimeString = DateTimeParseUtils.formatDateTime(getBaseContext(), nextTime);
                binding.txtTime.setText(nextTimeString);
                binding.txtLastTime.setText(DateTimeParseUtils.formatDateTime(getBaseContext(), medication.getSchedule().getLastWhenTook()));
            } else {
                binding.txtTime.setText("");
            }
        } else {
            // set visibility of ui
            binding.linLayContent.setVisibility(View.GONE);
            binding.buttons.setVisibility(View.GONE);
            binding.txtNextMedication.setText(getString(R.string.txt_msg_no_medication));
            binding.txtNextMedication.setTextColor(
                    getColor(app.bruner.library.R.color.blue_700)
            );
        }
    }

    @Override
    public void onClick(View v) { // deal with button clicks
        if(v.getId() == binding.btnTookThisMedication.getId()) { // took this medication
            medication.getSchedule().addWhenTook(new Date());
            MedicationUtils.update(this, medication);
            ConfirmUtils.showSavedMessage(getString(R.string.txt_you_took_it), this);

            // delay for 1 second to show confirmation message
            new Handler().postDelayed(() -> {
                Intent redirectIntent = new Intent(this, MedicationListActivity.class);
                redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(redirectIntent);
            }, 1000);
        }

        if(v.getId() == binding.btnReportSideEffects.getId()) { // report side effects
            Intent intent = new Intent(getBaseContext(), ReportSideEffectActivity.class);
            intent.putExtra(MedicationActivity.MEDICATION_PARAM, medication);
            startActivity(intent);
        }
    }
}