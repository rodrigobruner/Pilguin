package app.bruner.pillguin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import app.bruner.library.services.MedicationSyncService;
import app.bruner.pillguin.R;
import app.bruner.pillguin.databinding.ActivityMainBinding;
import app.bruner.pillguin.utils.Constants;

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
        // stop MedicationSyncService
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        stopService(serviceIntent);
    }

    private void init(){
        binding.btnContinue.setOnClickListener(this);

        startSyncService();

        //TODO: app onboarding

        // redirect to HomeActivity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }

    // set up button to continue
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_continue){
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }


    // =================== sync service

    private void startSyncService() {
        // start the sync service
        Intent serviceIntent = new Intent(this, MedicationSyncService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d("Sync service", "APP: Sync service started");
        checkWearableConnection();
    }


    // service connection to observe sync
    private android.content.ServiceConnection serviceConnection = new android.content.ServiceConnection() {
        @Override
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            Log.d("Sync service", "APP: Sync service connected");
        }

        @Override
        public void onServiceDisconnected(android.content.ComponentName name) {
            Log.d("Sync service", "APP: Sync service disconnected");
        }
    };

    // check if the wearable is connected
    private void checkWearableConnection() {
        Wearable.getNodeClient(this).getConnectedNodes()
                .addOnSuccessListener(nodes -> {
                    Log.d("Sync service", "WATCH: #nodes: " + nodes.size());
                    for (Node node : nodes) {
                        Log.d("Sync service", "WATCH: Node: " + node.getDisplayName());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Sync service", "WATCH: Error get connected nodes", e);
                });
    }
}