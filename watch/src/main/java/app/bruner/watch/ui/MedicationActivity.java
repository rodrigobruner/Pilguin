package app.bruner.watch.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.bruner.library.models.Medication;
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
            if (medication.getSchedule() != null && medication.getSchedule().getTimes() != null && !medication.getSchedule().getTimes().isEmpty()) {
                binding.txtTime.setText(medication.getSchedule().getTimes().get(0));
            } else {
                binding.txtTime.setText("");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.btnTookThisMedication.getId()) {
            // TODO: save took medication
            ConfirmUtils.showSavedMessage(getString(R.string.txt_you_took_it), this);
        }
    }
}