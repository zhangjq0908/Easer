/*
 * Copyright (c) 2016 - 2019 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills.event.nfc_tag;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;

import com.orhanobut.logger.Logger;

import ryey.easer.R;
import ryey.easer.commons.ui.CommonBaseActivity;

public class WaitForNfcActivity extends CommonBaseActivity {

    public static final String EXTRA_ID = "extra_id";

    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoggy_single_message);

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Logger.e("No nfc adapter. This page shouldn't be opened because it won't be detected as \"compatible\"");
            finish();
        }

        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                Logger.d("NFC adapter not enabled");
                promptToEnableNfc();
                return;
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableWaiterActivity();
    }

    @SuppressWarnings("ConstantConditions")
    private void disableWaiterActivity() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, WaitForNfcActivity.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onNewIntent(Intent intent) {
        resolveIntent(intent);
        super.onNewIntent(intent);
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Logger.d("NFC Tag detected");
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] tag_id = tag.getId();
            Intent return_intent = new Intent();
            return_intent.putExtra(EXTRA_ID, tag_id);
            setResult(RESULT_OK, return_intent);
            Logger.v("result handed. finishing self");
            finish();
        }
    }


    private void promptToEnableNfc() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.event_nfc_tag__prompt_enable_nfc_first);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
    }

}
