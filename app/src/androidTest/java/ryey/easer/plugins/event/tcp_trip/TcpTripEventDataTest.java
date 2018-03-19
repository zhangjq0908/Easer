package ryey.easer.plugins.event.tcp_trip;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.TestHelper;

import static org.junit.Assert.assertEquals;

public class TcpTripEventDataTest {

    @Test
    public void testParcel() {
        TcpTripEventData dummyData = new TcpTripEventDataFactory().dummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        TcpTripEventData parceledData = TcpTripEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}