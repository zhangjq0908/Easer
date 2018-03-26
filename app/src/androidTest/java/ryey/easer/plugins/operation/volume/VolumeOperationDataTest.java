package ryey.easer.plugins.operation.volume;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.*;

public class VolumeOperationDataTest {

    @Test
    public void testParcel() {
        VolumeOperationData dummyData = new VolumeOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        VolumeOperationData parceledData = VolumeOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}