package ryey.easer.plugins.event.timer;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class TimerEventDataTest {

    @Test
    public void testParcel() {
        TimerEventData dummyData = (TimerEventData) new TimerEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        TimerEventData parceledData = TimerEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}