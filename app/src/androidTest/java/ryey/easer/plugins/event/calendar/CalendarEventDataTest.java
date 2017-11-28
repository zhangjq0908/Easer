package ryey.easer.plugins.event.calendar;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class CalendarEventDataTest {

    public static CalendarEventData createDummyData() {
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

    @Test
    public void testParcel() {
        CalendarEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CalendarEventData parceledData = CalendarEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}