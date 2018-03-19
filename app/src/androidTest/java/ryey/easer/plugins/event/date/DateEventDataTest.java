package ryey.easer.plugins.event.date;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class DateEventDataTest {

    @Test
    public void testParcel() {
        DateEventData dummyData = new DateEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        DateEventData parceledData = DateEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}