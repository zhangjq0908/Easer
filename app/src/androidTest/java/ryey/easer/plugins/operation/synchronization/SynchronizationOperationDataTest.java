package ryey.easer.plugins.operation.synchronization;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class SynchronizationOperationDataTest {

    public static SynchronizationOperationData createDummyData() {
        SynchronizationOperationData dummyData = new SynchronizationOperationData();
        dummyData.set(true);
        return dummyData;
    }

    @Test
    public void testParcel() {
        SynchronizationOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SynchronizationOperationData parceledData = SynchronizationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}