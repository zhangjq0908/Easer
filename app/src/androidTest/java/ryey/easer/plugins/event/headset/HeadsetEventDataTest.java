package ryey.easer.plugins.event.headset;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class HeadsetEventDataTest {

    @Test
    public void testParcel() {
        HeadsetEventData dummyData = new HeadsetEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        HeadsetEventData parceledData = HeadsetEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}