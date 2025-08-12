package app.bruner.watch.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.wearable.Wearable;

import app.bruner.library.services.MedicationSyncService;
import app.bruner.library.utils.DataSyncUtils;
import app.bruner.library.utils.MedicationUtils;
import app.bruner.watch.R;
import app.bruner.watch.databinding.ActivityMainBinding;
import utils.Constants;

/**
 * Main activity of the app
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop the medicationSyncService
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        stopService(serviceIntent);
    }

    private void init(){
        // set click listeners
        binding.btnDailyMedication.setOnClickListener(this);
        binding.btnNextMedication.setOnClickListener(this);
        binding.btnLatestMedication.setOnClickListener(this);

        // strat the medicationSyncService
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        startService(serviceIntent);
        requestPermitions();
    }

    @Override
    public void onClick(View v) { // deal with button clicks
        if (v.getId() == R.id.btn_daily_medication) {
            startActivity(new Intent(this, MedicationListActivity.class));
        } else if (v.getId() == R.id.btn_next_medication) {
            startActivity(new Intent(this, MedicationActivity.class));
        } else if (v.getId() == R.id.btn_latest_medication) {
            startActivity(new Intent(this, LatestMedicationActivity.class));
        }
    }

    // ==============resquest permissions

    public void requestPermitions() {
        // request permissions for recording audio, posting notifications, and scheduling alarms
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.SCHEDULE_EXACT_ALARM
        };

        ActivityCompat.requestPermissions(this, permissions, Constants.PERMITION_CODE);
    }

    // deal with the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.PERMITION_CODE) { // Check if all permissions were granted
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