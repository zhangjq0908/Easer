package ryey.easer.plugins.event.time;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class TimeEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return TimeEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new TimeEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        TimeEventData dummyData = new TimeEventData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 3);
        dummyData.set(calendar);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new TimeEventData(data, format, version);
    }
}
