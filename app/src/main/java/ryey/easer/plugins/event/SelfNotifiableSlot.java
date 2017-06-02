package ryey.easer.plugins.event;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;
import android.util.Log;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;

public abstract class SelfNotifiableSlot extends AbstractSlot {
    // Fields used in relevant Intent
    protected static final String ACTION_SATISFIED = "ryey.easer.triggerlotus.abstractslot.SATISFIED";
    protected static final String ACTION_UNSATISFIED = "ryey.easer.triggerlotus.abstractslot.UNSATISFIED";
    protected static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.triggetlotus.category.NOTIFY_SLOT";
    /*
     * Mechanisms and fields used to notify the slot itself, and then proceed to `onNotified()`.
     * This is because some system-level checking mechanisms (e.g. data/time) need a PendingIntent.
     */
    protected Uri uri = Uri.parse(String.format("slot://%s/%d", getClass().getSimpleName(), hashCode()));
    protected PendingIntent notifySelfIntent, notifySelfUnsatisfiedIntent;
    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(getClass().getSimpleName(), "self notifying Intent received");
            if (intent.getAction().equals(ACTION_SATISFIED)) {
                onNotified();
            } else if (intent.getAction().equals(ACTION_UNSATISFIED)) {
                onUnsatisfied();
            }
        }
    };

    public SelfNotifiableSlot(Context context) {
        super(context);
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
        intent.setAction(ACTION_UNSATISFIED);
        notifySelfUnsatisfiedIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    protected void onNotified() {
        Log.d(getClass().getSimpleName(), "onNotified");
        changeSatisfiedState(true);
    }

    protected void onUnsatisfied() {
        Log.d(getClass().getSimpleName(), "onUnsatisfied");
        changeSatisfiedState(false);
    }
}
