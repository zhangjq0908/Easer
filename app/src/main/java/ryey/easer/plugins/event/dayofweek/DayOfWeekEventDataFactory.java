package ryey.easer.plugins.event.dayofweek;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DayOfWeekEventDataFactory implements EventDataFactory<DayOfWeekEventData> {
    @NonNull
    @Override
    public Class<DayOfWeekEventData> dataClass() {
        return DayOfWeekEventData.class;
    }

    @NonNull
    @Override
    public DayOfWeekEventData emptyData() {
        return new DayOfWeekEventData();
    }

    @NonNull
    @Override
    public DayOfWeekEventData dummyData() {
        DayOfWeekEventData dummyData = new DayOfWeekEventData();
        dummyData.days.add(2);
        dummyData.days.add(4);
        dummyData.days.add(5);
        return dummyData;
    }

    @NonNull
    @Override
    public DayOfWeekEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DayOfWeekEventData(data, format, version);
    }
}
