package ryey.easer.plugins.operation.network_transmission;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class NetworkTransmissionOperationDataTest {

    @Test
    public void testParcel() {
        NetworkTransmissionOperationData dummyData = new NetworkTransmissionOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NetworkTransmissionOperationData parceledData = NetworkTransmissionOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}