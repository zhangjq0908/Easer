package ryey.easer.plugins.event.calendar;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class CalendarEventDataFactory implements EventDataFactory<CalendarEventData> {
    @NonNull
    @Override
    public Class<CalendarEventData> dataClass() {
        return CalendarEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public CalendarEventData dummyData() {
        CalendarData calendarData = new CalendarData();
        calendarData.calendar_id = 20;
        for (int i = 0; i < CalendarData.condition_name.length; i++) {
            if (i % 2 == 0) {
                calendarData.conditions.add(CalendarData.condition_name[i]);
            }
        }
        CalendarEventData dummyData = new CalendarEventData(calendarData);
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public CalendarEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CalendarEventData(data, format, version);
    }
}
