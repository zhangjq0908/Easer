/*
 * Copyright (c) 2016 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.commons;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.util.Log;

public abstract class AbstractSlot {
    protected static final String ACTION_SATISFIED = "ryey.easer.triggerlotus.abstractslot.SATISFIED";
    protected static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.triggetlotus.category.NOTIFY_SLOT";

    protected boolean satisfied;

    protected Uri uri = Uri.parse(String.format("slot://%s/%d", getClass().getSimpleName(), hashCode()));
    protected PendingIntent notifySelfIntent;

    protected PendingIntent notifyLotusIntent = null;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_SATISFIED))
                onSatisfied();
        }
    };

    public AbstractSlot(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SATISFIED);
        filter.addCategory(CATEGORY_NOTIFY_SLOT);
        filter.addDataScheme(uri.getScheme());
        filter.addDataAuthority(uri.getAuthority(), null);
        filter.addDataPath(uri.getPath(), PatternMatcher.PATTERN_LITERAL);
        context.registerReceiver(mReceiver, filter);

        Intent intent = new Intent(ACTION_SATISFIED);
        intent.addCategory(CATEGORY_NOTIFY_SLOT);
        intent.setData(uri);
        notifySelfIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public abstract void set(EventData data);

    public abstract boolean isValid();

    public abstract void apply();

    public abstract void cancel();

    public void onRegister(PendingIntent notifyLotusIntent) {
        this.notifyLotusIntent = notifyLotusIntent;
    }

    void onSatisfied() {
        Log.d(getClass().getSimpleName(), "onSatisfied");
        if (notifyLotusIntent == null) {
            Log.wtf(getClass().getSimpleName(), "slot never registered");
            throw new RuntimeException("slot never registered");
        }
        satisfied = true;
        try {
            notifyLotusIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
