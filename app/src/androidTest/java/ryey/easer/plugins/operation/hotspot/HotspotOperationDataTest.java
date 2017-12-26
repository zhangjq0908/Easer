package ryey.easer.plugins.operation.hotspot;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class HotspotOperationDataTest {

    @Test
    public void testParcel() {
        HotspotOperationData dummyData = new HotspotOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        HotspotOperationData parceledData = HotspotOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}