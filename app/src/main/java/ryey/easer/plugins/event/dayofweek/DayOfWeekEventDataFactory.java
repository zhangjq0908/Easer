package ryey.easer.plugins.event.dayofweek;

import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;

import java.util.Arrays;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DayOfWeekEventDataFactory implements EventDataFactory<DayOfWeekEventData> {
    @NonNull
    @Override
    public Class<DayOfWeekEventData> dataClass() {
        return DayOfWeekEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public DayOfWeekEventData dummyData() {
        return new DayOfWeekEventData(new ArraySet<>(Arrays.asList(2, 4, 5)));
    }

    @ValidData
    @NonNull
    @Override
    public DayOfWeekEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DayOfWeekEventData(data, format, version);
    }
}
