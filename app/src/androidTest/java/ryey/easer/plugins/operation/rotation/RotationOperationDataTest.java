package ryey.easer.plugins.operation.rotation;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class RotationOperationDataTest {

    @Test
    public void testParcel() {
        RotationOperationData dummyData = new RotationOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        RotationOperationData parceledData = RotationOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}