package ryey.easer.plugins.operation.bluetooth;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BluetoothOperationDataTest {

    public static BluetoothOperationData createDummyData() {
        BluetoothOperationData dummyData = new BluetoothOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        BluetoothOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BluetoothOperationData parceledData = BluetoothOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}