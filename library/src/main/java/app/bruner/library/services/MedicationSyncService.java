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
 * Service to deal with medication data sync between phone and watch
 */
public class MedicationSyncService extends Service implements DataClient.OnDataChangedListener {

    private static final String TAG = "MedicationSyncService";

    // path for medication sync
    private static final String MEDICATIONS_SYNC_PATH = "/medications_sync";

    @Override
    public void onCreate() {
        super.onCreate();
        // register the data listener
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // remove the data listener
        Wearable.getDataClient(this).removeListener(this);
    }

    // on data changed callback
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) { // for each data event
            if (event.getType() == DataEvent.TYPE_CHANGED) { // check if the data item has changed
                DataItem item = event.getDataItem(); // get the data item
                // check if the data item is to medication
                if (item.getUri().getPath().equals(MEDICATIONS_SYNC_PATH)) {
                    // get the data map
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
//                    String source = dataMap.getString("source", "unknown");
                    DataSyncUtils.receiveUpdate(this, dataMap); // process the data map
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