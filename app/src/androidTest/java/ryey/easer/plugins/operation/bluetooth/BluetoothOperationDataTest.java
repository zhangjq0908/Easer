package ryey.easer.plugins.operation.bluetooth;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class BluetoothOperationDataTest {

    @Test
    public void testParcel() {
        BluetoothOperationData dummyData = new BluetoothOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BluetoothOperationData parceledData = BluetoothOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}