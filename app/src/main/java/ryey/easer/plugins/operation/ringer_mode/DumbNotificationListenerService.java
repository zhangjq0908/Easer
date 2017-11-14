package ryey.easer.plugins.operation.ringer_mode;

import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class DumbNotificationListenerService extends NotificationListenerService {
    ConditionVariable cv = new ConditionVariable();
    @Override
    public void onListenerConnected() {
//        cv.open(); //FIXME: due to unknown reason, this function is never called, so synchronization can't be done.
    }

    @Override
    public IBinder onBind(Intent intent) {
        DumbBinder binder = new DumbBinder();
        binder.listenerService = this;
        return binder;
    }

    class DumbBinder extends Binder {
        DumbNotificationListenerService listenerService;

        void requestInterruptionFilter(int interruptionFilter) {
//            cv.block();
            listenerService.requestInterruptionFilter(interruptionFilter); // This will succeed even if `onListenerConnected()` is not called.
        }

        void setSilent() {
            requestInterruptionFilter(INTERRUPTION_FILTER_NONE);
        }
    }
}
