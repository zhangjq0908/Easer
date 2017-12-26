package ryey.easer.plugins.operation.synchronization;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class SynchronizationOperationDataTest {

    @Test
    public void testParcel() {
        SynchronizationOperationData dummyData = new SynchronizationOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        SynchronizationOperationData parceledData = SynchronizationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}