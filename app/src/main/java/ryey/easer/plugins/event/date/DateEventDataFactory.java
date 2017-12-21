package ryey.easer.plugins.event.date;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DateEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return DateEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new DateEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        DateEventData dummyData = new DateEventData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        dummyData.set(calendar);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DateEventData(data, format, version);
    }
}
