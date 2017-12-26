package ryey.easer.plugins.event.nfc_tag;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class NfcListenerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = getIntent().getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Intent iIntent = new Intent(NfcListenerService.ACTION_NFC_SCANNED);
            iIntent.putExtras(getIntent());
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(iIntent);
        }
        finish();
    }

}
