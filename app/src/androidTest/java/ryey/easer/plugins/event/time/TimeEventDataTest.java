package ryey.easer.plugins.event.time;

import android.os.Parcel;

import org.junit.Test;

import java.util.Calendar;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class TimeEventDataTest {

    public static TimeEventData createDummyData() {
        TimeEventData dummyData = new TimeEventData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 3);
        dummyData.set(calendar);
        return dummyData;
    }

    @Test
    public void testParcel() {
        TimeEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        TimeEventData parceledData = TimeEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}