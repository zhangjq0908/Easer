package ryey.easer.plugins.event.nfc_tag;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.ArraySet;

import java.util.Set;

public class NfcListenerService extends Service {
    static final String ACTION_NFC_SCANNED = "ryey.easer.plugins.event.nfc_tag.action.NFC_SCANNED";

    private final IntentFilter mFilter = new IntentFilter(ACTION_NFC_SCANNED);
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NFC_SCANNED.equals(intent.getAction())) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                for (NfcTagSlot slot : registeredSlots) {
                    slot.checkAndTrigger(tag);
                }
            }
        }
    };

    private Set<NfcTagSlot> registeredSlots = new ArraySet<>();

    public NfcListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new NLSBinder();
    }

    class NLSBinder extends Binder {
        void registerSlot(NfcTagSlot slot) {
            registeredSlots.add(slot);
        }

        void unregisterSlot(NfcTagSlot slot) {
            registeredSlots.remove(slot);
        }
    }
}
