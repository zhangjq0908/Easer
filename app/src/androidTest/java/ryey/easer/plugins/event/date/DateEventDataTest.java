package ryey.easer.plugins.event.date;

import android.os.Parcel;

import org.junit.Test;

import java.util.Calendar;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class DateEventDataTest {

    public static DateEventData createDummyData() {
        DateEventData dummyData = new DateEventData();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 2);
        dummyData.set(calendar);
        return dummyData;
    }

    @Test
    public void testParcel() {
        DateEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        DateEventData parceledData = DateEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}