package ryey.easer.plugins.operation.event_control;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class EventControlOperationDataTest {

    @Test
    public void testParcel() {
        EventControlOperationData dummyData = new EventControlDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        EventControlOperationData parceledData = EventControlOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}