package app.bruner.watch.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.wearable.Wearable;

import app.bruner.library.moc.MedicationsMoc;
import app.bruner.library.services.MedicationSyncService;
import app.bruner.library.utils.DataSyncUtils;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop the medicationSyncService
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        stopService(serviceIntent);
    }

    private void init(){
        binding.btnDailyMedication.setOnClickListener(this);
        binding.btnNextMedication.setOnClickListener(this);
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        startService(serviceIntent);

//        testWearConnection();

        // ADICIONAR: Forçar sync manual para teste
//        Log.d("MainActivity", "Forcing manual sync from watch");
//        DataSyncUtils.sendUpdate(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_daily_medication) {
            startActivity(new Intent(this, MedicationListActivity.class));
        } else if (v.getId() == R.id.btn_next_medication) {
            startActivity(new Intent(this, MedicationActivity.class));
        }
    }


    private void initShardPreferences() {
        MedicationUtils.add(getApplicationContext(), MedicationsMoc.getMedication());
    }

    // test conection with the phone
//    private void testWearConnection() {
//
//        Wearable.getNodeClient(this).getConnectedNodes()
//                .addOnSuccessListener(nodes -> {
//                    Log.d("MainActivity", "Connected nodes: " + nodes.size());
//                    for (com.google.android.gms.wearable.Node node : nodes) {
//                        Log.d("MainActivity", "Node: " + node.getDisplayName() + " - " + node.getId());
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("MainActivity", "Failed to get connected nodes", e);
//                });
//    }
}