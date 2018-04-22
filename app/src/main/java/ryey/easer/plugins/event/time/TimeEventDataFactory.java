package ryey.easer.plugins.event.time;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class TimeEventDataFactory implements EventDataFactory<TimeEventData> {
    @NonNull
    @Override
    public Class<TimeEventData> dataClass() {
        return TimeEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public TimeEventData dummyData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 23);
        return new TimeEventData(calendar);
    }

    @ValidData
    @NonNull
    @Override
    public TimeEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new TimeEventData(data, format, version);
    }
}
