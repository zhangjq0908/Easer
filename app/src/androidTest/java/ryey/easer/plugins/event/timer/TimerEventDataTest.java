package ryey.easer.plugins.event.timer;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class TimerEventDataTest {

    public static TimerEventData createDummyData() {
        TimerEventData dummyData = new TimerEventData();
        TimerEventData.Timer timer = new TimerEventData.Timer();
        timer.exact = true;
        timer.repeat = true;
        timer.minutes = 102;
        dummyData.set(timer);
        return dummyData;
    }

    @Test
    public void testParcel() {
        TimerEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        TimerEventData parceledData = TimerEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}