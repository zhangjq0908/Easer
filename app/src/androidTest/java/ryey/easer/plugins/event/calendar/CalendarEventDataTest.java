package ryey.easer.plugins.event.calendar;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class CalendarEventDataTest {

    @Test
    public void testParcel() {
        CalendarEventData dummyData = new CalendarEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        CalendarEventData parceledData = CalendarEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}