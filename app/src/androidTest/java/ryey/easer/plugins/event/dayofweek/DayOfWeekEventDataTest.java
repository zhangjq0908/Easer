package ryey.easer.plugins.event.dayofweek;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class DayOfWeekEventDataTest {

    public static DayOfWeekEventData createDummyData() {
        DayOfWeekEventData dummyData = new DayOfWeekEventData();
        dummyData.set(new String[]{"2", "4", "5"});
        return dummyData;
    }

    @Test
    public void testParcel() {
        DayOfWeekEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        DayOfWeekEventData parceledData = DayOfWeekEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}