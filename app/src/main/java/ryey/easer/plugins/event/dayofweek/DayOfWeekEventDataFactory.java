package ryey.easer.plugins.event.dayofweek;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DayOfWeekEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return DayOfWeekEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new DayOfWeekEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        DayOfWeekEventData dummyData = new DayOfWeekEventData();
        dummyData.set(new String[]{"2", "4", "5"});
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DayOfWeekEventData(data, format, version);
    }
}
