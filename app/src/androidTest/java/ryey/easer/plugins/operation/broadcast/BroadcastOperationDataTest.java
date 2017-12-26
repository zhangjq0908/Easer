package ryey.easer.plugins.operation.broadcast;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.commons.C;
import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class BroadcastOperationDataTest {

    @Test
    public void testParcel() {
        BroadcastOperationData dummyData = new BroadcastOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BroadcastOperationData parceledData = BroadcastOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

    @Test
    public void testSerialize() throws Exception {
        BroadcastOperationDataFactory factory = new BroadcastOperationDataFactory();
        BroadcastOperationData dummyData = factory.dummyData();
        for (C.Format format : C.Format.values()) {
            String serialized_data = dummyData.serialize(format);
            BroadcastOperationData parsed_data = factory.parse(serialized_data, format, C.VERSION_CURRENT);
            assertEquals(dummyData, parsed_data);
        }
    }

}