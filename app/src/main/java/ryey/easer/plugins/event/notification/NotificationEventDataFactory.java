package ryey.easer.plugins.event.notification;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class NotificationEventDataFactory implements EventDataFactory<NotificationEventData> {
    @NonNull
    @Override
    public Class<NotificationEventData> dataClass() {
        return NotificationEventData.class;
    }

    @NonNull
    @Override
    public NotificationEventData emptyData() {
        return new NotificationEventData();
    }

    @ValidData
    @NonNull
    @Override
    public NotificationEventData dummyData() {
        String app = "example.app";
        String title = "title example";
        String content = "content example";
        return new NotificationEventData(app, title, content);
    }

    @ValidData
    @NonNull
    @Override
    public NotificationEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NotificationEventData(data, format, version);
    }
}
