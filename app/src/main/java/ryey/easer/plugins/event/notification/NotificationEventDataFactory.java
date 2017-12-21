package ryey.easer.plugins.event.notification;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class NotificationEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return NotificationEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new NotificationEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        NotificationEventData dummyData = new NotificationEventData();
        NotificationSelection notificationSelection = new NotificationSelection();
        notificationSelection.app = "example.app";
        notificationSelection.title = "title example";
        notificationSelection.content = "content example";
        dummyData.set(notificationSelection);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NotificationEventData(data, format, version);
    }
}
