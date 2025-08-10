package app.bruner.watch.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Date;

import app.bruner.library.models.Medication;
import app.bruner.library.models.SideEffect;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.watch.R;
import app.bruner.watch.databinding.ActivityReportSideEffectBinding;
import utils.ConfirmUtils;

public class ReportSideEffectActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityReportSideEffectBinding binding;

    Medication medication;

    private static final int SPEECH_REQUEST_CODE = 100;

    public static final String MEDICATION_PARAM = "medication_obj";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportSideEffectBinding.inflate(getLayoutInflater());
        init();
        setContentView(binding.getRoot());
    }

    private void init() {
        medication = getIntent().getSerializableExtra(MEDICATION_PARAM, Medication.class);
        binding.btnSave.setOnClickListener(this);
        setSideEffect();
    }

    private void setSideEffect() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(voiceIntent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String sideEffect = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                binding.txtSideEffect.setText(sideEffect);
            }
        } else {
            Snackbar.make(binding.getRoot(), "Failed to recognize speech", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            String sideEffect = binding.txtSideEffect.getText().toString();
            if (sideEffect.isEmpty()) {
                Snackbar.make(binding.getRoot(), "Please enter a side effect", Snackbar.LENGTH_LONG).show();
                return;
            }

            medication.getSideEffects().add(new SideEffect(sideEffect, new Date()));
            MedicationUtils.update(getBaseContext(), medication);
            ConfirmUtils.showSavedMessage(getString(R.string.txt_side_effect_reported), this);

            // delay for 1 second to show confirmation message
            new Handler().postDelayed(() -> {
                Intent redirectIntent = new Intent(this, MedicationListActivity.class);
                redirectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(redirectIntent);
            }, 1000);
        }
    }
}