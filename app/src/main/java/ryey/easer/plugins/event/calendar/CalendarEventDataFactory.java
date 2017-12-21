package ryey.easer.plugins.event.calendar;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class CalendarEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return CalendarEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new CalendarEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        CalendarEventData dummyData = new CalendarEventData();
        CalendarData calendarData = new CalendarData();
        calendarData.calendar_id = 20;
        for (int i = 0; i < CalendarData.condition_name.length; i++) {
            if (i % 2 == 0) {
                calendarData.conditions.add(CalendarData.condition_name[i]);
            }
        }
        dummyData.set(calendarData);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new CalendarEventData(data, format, version);
    }
}
