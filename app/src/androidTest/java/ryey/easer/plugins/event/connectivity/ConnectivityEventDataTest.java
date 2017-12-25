package ryey.easer.plugins.event.connectivity;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.assertEquals;

public class ConnectivityEventDataTest {

    @Test
    public void testParcel() {
        ConnectivityEventData dummyData = new ConnectivityEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        ConnectivityEventData parceledData = ConnectivityEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}