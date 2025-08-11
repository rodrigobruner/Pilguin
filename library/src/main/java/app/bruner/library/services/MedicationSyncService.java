package app.bruner.library.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import app.bruner.library.utils.DataSyncUtils;

/**
 * Service to handle medication data synchronization between phone and watch
  */
public class MedicationSyncService extends Service implements DataClient.OnDataChangedListener {

    private static final String TAG = "MedicationSyncService";
    private static final String MEDICATIONS_SYNC_PATH = "/medications_sync";

    @Override
    public void onCreate() {
        super.onCreate();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().equals(MEDICATIONS_SYNC_PATH)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String source = dataMap.getString("source", "unknown");
                    DataSyncUtils.receiveUpdate(this, dataMap);
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}