package ryey.easer.plugins.event.dayofweek;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class DayOfWeekEventDataTest {

    @Test
    public void testParcel() {
        DayOfWeekEventData dummyData = new DayOfWeekEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        DayOfWeekEventData parceledData = DayOfWeekEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}