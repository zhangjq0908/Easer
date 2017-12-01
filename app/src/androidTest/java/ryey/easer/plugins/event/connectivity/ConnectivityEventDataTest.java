package ryey.easer.plugins.event.connectivity;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class ConnectivityEventDataTest {

    public static ConnectivityEventData createDummyData() {
        ConnectivityEventData dummyData = new ConnectivityEventData();
        dummyData.set(new String[]{"1", "2"});
        return dummyData;
    }

    @Test
    public void testParcel() {
        ConnectivityEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        ConnectivityEventData parceledData = ConnectivityEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}