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
import app.bruner.watch.R;
import app.bruner.watch.databinding.ActivityMedicationBinding;
import app.bruner.library.moc.MedicationsMoc;
import utils.ConfirmUtils;

public class MedicationActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MEDICATION_PARAM = "medication_obj";

    ActivityMedicationBinding binding;

    Medication medication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMedicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        getMedication();
        setupViews();
        binding.btnTookThisMedication.setOnClickListener(this);
        binding.btnReportSideEffects.setOnClickListener(this);
    }

    private void getMedication(){
        medication = getIntent().getSerializableExtra(MEDICATION_PARAM, Medication.class);
        if(medication == null) {
            // TODO: get NEXT medication
            medication = new MedicationsMoc().getMedication();
            return;
        }
        binding.txtNextMedication.setVisibility(View.GONE);
    }


    private void setupViews() {
        if (medication != null) {
            binding.txtName.setText(medication.getName());
            binding.txtDosage.setText(medication.getDosage());
            binding.imgType.setImageResource(MedicineTypeIconMapper.getIconByType(getBaseContext(), medication.getType()));
            if (medication.getSchedule() != null) {
                Date nextTime = medication.getSchedule().getNextTime();
                String nextTimeString = DateTimeParseUtils.formatDateTime(getBaseContext(), nextTime);
                binding.txtTime.setText(nextTimeString);
            } else {
                binding.txtTime.setText("");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.btnTookThisMedication.getId()) {
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

        if(v.getId() == binding.btnReportSideEffects.getId()) {
            Intent intent = new Intent(getBaseContext(), ReportSideEffectActivity.class);
            intent.putExtra(MedicationActivity.MEDICATION_PARAM, medication);
            startActivity(intent);
        }
    }
}