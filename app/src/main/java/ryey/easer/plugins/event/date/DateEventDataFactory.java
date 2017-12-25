package ryey.easer.plugins.event.date;

import android.support.annotation.NonNull;

import java.util.Calendar;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class DateEventDataFactory implements EventDataFactory<DateEventData> {
    @NonNull
    @Override
    public Class<DateEventData> dataClass() {
        return DateEventData.class;
    }

    @NonNull
    @Override
    public DateEventData emptyData() {
        return new DateEventData();
    }

    @ValidData
    @NonNull
    @Override
    public DateEventData dummyData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        DateEventData dummyData = new DateEventData(calendar);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public DateEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new DateEventData(data, format, version);
    }
}
