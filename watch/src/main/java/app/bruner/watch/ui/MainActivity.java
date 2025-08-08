package app.bruner.watch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.bruner.library.moc.MedicationsMoc;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.watch.R;
import app.bruner.watch.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init(){
        binding.btnDailyMedication.setOnClickListener(this);
        binding.btnNextMedication.setOnClickListener(this);
        binding.btnReportSideEffect.setOnClickListener(this);
//        initShardPreferences();
    }

    @Override
    public void onClick(View v) {
        Log.i("Click", "Button clicked: " + v.getId());
        if (v.getId() == R.id.btn_daily_medication) {
            startActivity(new Intent(this, MedicationListActivity.class));
        } else if (v.getId() == R.id.btn_next_medication) {
            startActivity(new Intent(this, MedicationActivity.class));
        } else if (v.getId() == R.id.btn_report_side_effect) {
            startActivity(new Intent(this, ReportSideEffectActivity.class));
        }
    }


    private void initShardPreferences() {
        MedicationUtils.add(getApplicationContext(), MedicationsMoc.getMedication());
    }
}