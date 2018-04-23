package ryey.easer.plugins.operation.launch_app;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class LaunchAppOperationDataTest {

    @Test
    public void testParcel() {
        LaunchAppOperationData dummyData = new LaunchAppOperationDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        LaunchAppOperationData parceledData = LaunchAppOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}