package ryey.easer.plugins.event;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.PatternMatcher;

import com.orhanobut.logger.Logger;

import java.util.Locale;

import ryey.easer.commons.plugindef.eventplugin.AbstractSlot;

public abstract class SelfNotifiableSlot extends AbstractSlot {
    // Fields used in relevant Intent
    private static final String ACTION_SATISFIED = "ryey.easer.triggerlotus.abstractslot.SATISFIED";
    private static final String ACTION_UNSATISFIED = "ryey.easer.triggerlotus.abstractslot.UNSATISFIED";
    private static final String CATEGORY_NOTIFY_SLOT = "ryey.easer.triggetlotus.category.NOTIFY_SLOT";
    /*
     * Mechanisms and fields used to notify the slot itself, and then proceed to `onPositiveNotified()`.
     * This is because some system-level checking mechanisms (e.g. data/time) need a PendingIntent.
     */
    protected final Uri uri = Uri.parse(String.format(Locale.US, "slot://%s/%d", getClass().getSimpleName(), hashCode()));
    // After sent, this will trigger onPositiveNotified().
    // Meant to be used when the event is going to a positive state.
    protected final PendingIntent notifySelfIntent_positive;
    // After sent, this will trigger onNegativeNotified().
    // Meant to be used when the event is going to a negative state.
    protected final PendingIntent notifySelfIntent_negative;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d("self notifying Intent received. action: %s", intent.getAction());
            if (intent.getAction().equals(ACTION_SATISFIED)) {
                onPositiveNotified();
            } else if (intent.getAction().equals(ACTION_UNSATISFIED)) {
                onNegativeNotified();
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
        notifySelfIntent_positive = PendingIntent.getBroadcast(context, 0, intent, 0);
        intent.setAction(ACTION_UNSATISFIED);
        notifySelfIntent_negative = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    protected void onPositiveNotified() {
        Logger.v("onPositiveNotified");
        changeSatisfiedState(true);
    }

    protected void onNegativeNotified() {
        Logger.v("onNegativeNotified");
        changeSatisfiedState(false);
    }
}
