package ryey.easer.plugins.event.time;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class TimeEventDataTest {

    @Test
    public void testParcel() {
        TimeEventData dummyData = (TimeEventData) new TimeEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        TimeEventData parceledData = TimeEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}