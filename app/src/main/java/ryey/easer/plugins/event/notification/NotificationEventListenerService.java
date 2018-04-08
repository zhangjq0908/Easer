package ryey.easer.plugins.event.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NotificationEventListenerService extends NotificationListenerService {
    private static final String ACTION_LISTEN = "ryey.easer.plugins.event.notification.action.LISTEN";
    private static final String ACTION_CANCEL = "ryey.easer.plugins.event.notification.action.CANCEL";
    private static final String EXTRA_DATA = "ryey.easer.plugins.event.notification.extra.DATA";
    private static final String EXTRA_POSITIVE = "ryey.easer.plugins.event.notification.extra.POSITIVE";
    private static final String EXTRA_NEGATIVE = "ryey.easer.plugins.event.notification.extra.NEGATIVE";

    List<CompoundData> dataList = new ArrayList<>();

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null)
                return;
            if (!(action.equals(ACTION_LISTEN) || action.equals(ACTION_CANCEL)))
                return;
            Logger.d("broadcast received");
            NotificationEventData eventData = intent.getParcelableExtra(EXTRA_DATA);
            PendingIntent pendingIntent_positive = intent.getParcelableExtra(EXTRA_POSITIVE);
            PendingIntent pendingIntent_negative = intent.getParcelableExtra(EXTRA_NEGATIVE);
            if (action.equals(ACTION_LISTEN)) {
                addListenToNotification(eventData, pendingIntent_positive, pendingIntent_negative);
            } else if (action.equals(ACTION_CANCEL)) {
                delListenToNotification(eventData, pendingIntent_positive, pendingIntent_negative);
            }
        }
    };

    static void listen(Context context,
                       NotificationEventData eventData,
                       PendingIntent pendingIntent_positive,
                       PendingIntent pendingIntent_negative) {
        Intent intent = new Intent(ACTION_LISTEN);
        intent.putExtra(EXTRA_DATA, eventData);
        intent.putExtra(EXTRA_POSITIVE, pendingIntent_positive);
        intent.putExtra(EXTRA_NEGATIVE, pendingIntent_negative);
        Logger.d("informing 'listen'");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }

    static void cancel(Context context,
                       NotificationEventData eventData,
                       PendingIntent pendingIntent_positive,
                       PendingIntent pendingIntent_negative) {
        Intent intent = new Intent(ACTION_CANCEL);
        intent.putExtra(EXTRA_DATA, eventData);
        intent.putExtra(EXTRA_POSITIVE, pendingIntent_positive);
        intent.putExtra(EXTRA_NEGATIVE, pendingIntent_negative);
        Logger.d("informing 'cancel'");
        LocalBroadcastManager.getInstance(context.getApplicationContext()).sendBroadcast(intent);
    }

    private static boolean match(StatusBarNotification sbn, String t_app, String t_title, String t_content) {
        Logger.d("app: <%s> <%s>", t_app, sbn.getPackageName());
        if (t_app != null) {
            if (!t_app.equals(sbn.getPackageName()))
                return false;
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        String contentText = extras.getString(Notification.EXTRA_TEXT);
        if (t_title != null) {
            if (title == null || !title.contains(t_title))
                return false;
        }
        if (t_content != null) {
            if (contentText == null || !contentText.contains(t_content))
                return false;
        }
        return true;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        for (CompoundData compoundData : dataList) {
            NotificationEventData eventData = compoundData.notificationEventData;
            if (match(sbn, eventData.app, eventData.title, eventData.content)) {
                try {
                    compoundData.positive.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onNotificationPosted(sbn);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onCreate() {
        Logger.i("NotificationEventListenerService onCreate()");
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_LISTEN);
        filter.addAction(ACTION_CANCEL);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        Logger.i("NotificationEventListenerService onDestroy()");
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
        if (dataList.size() > 0) {
            Logger.w("Listener to notifications not cleaned up completely: %d left",
                    dataList.size());
        }
    }

    private void addListenToNotification(
            NotificationEventData notificationEventData,
            PendingIntent pendingIntent_positive,
            PendingIntent pendingIntent_negative) {
        dataList.add(new CompoundData(
                notificationEventData,
                pendingIntent_positive,
                pendingIntent_negative));
    }

    private void delListenToNotification(
            NotificationEventData notificationEventData,
            PendingIntent pendingIntent_positive,
            PendingIntent pendingIntent_negative) {
        CompoundData compoundData = new CompoundData(
                notificationEventData,
                pendingIntent_positive,
                pendingIntent_negative);
        dataList.remove(compoundData);
    }

    private static class CompoundData {
        final NotificationEventData notificationEventData;
        final PendingIntent positive;
        final PendingIntent negative;
        private CompoundData(
                NotificationEventData notificationEventData,
                PendingIntent positive,
                PendingIntent negative) {
            this.notificationEventData = notificationEventData;
            this.positive = positive;
            this.negative = negative;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof CompoundData))
                return false;
            return positive.equals(((CompoundData) obj).positive);
        }
    }
}
