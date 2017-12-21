package ryey.easer.plugins.operation.broadcast;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class BroadcastOperationDataTest {

    @Test
    public void testParcel() {
        BroadcastOperationData dummyData = (BroadcastOperationData) new BroadcastOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BroadcastOperationData parceledData = BroadcastOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}