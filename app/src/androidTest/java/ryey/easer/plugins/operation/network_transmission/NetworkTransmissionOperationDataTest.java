package ryey.easer.plugins.operation.network_transmission;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class NetworkTransmissionOperationDataTest {

    public static NetworkTransmissionOperationData createDummyData() {
        NetworkTransmissionOperationData dummyData = new NetworkTransmissionOperationData();
        TransmissionData transmissionData = new TransmissionData();
        transmissionData.protocol = TransmissionData.Protocol.tcp;
        transmissionData.remote_port = 146;
        transmissionData.remote_address = "192.168.0.143";
        transmissionData.data = "aaaData";
        dummyData.set(transmissionData);
        return dummyData;
    }

    @Test
    public void testParcel() {
        NetworkTransmissionOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        NetworkTransmissionOperationData parceledData = NetworkTransmissionOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}