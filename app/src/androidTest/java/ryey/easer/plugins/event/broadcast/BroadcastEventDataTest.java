package ryey.easer.plugins.event.broadcast;

import android.os.Parcel;

import org.junit.Test;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BroadcastEventDataTest {

    public static BroadcastEventData createDummyData() {
        BroadcastEventData dummyData = new BroadcastEventData();
        ReceiverSideIntentData intentData = new ReceiverSideIntentData();
        intentData.action.add("action1");
        intentData.action.add("action2");
        intentData.category.add("category1");
        intentData.category.add("category2");
        dummyData.set(intentData);
        return dummyData;
    }

    @Test
    public void testParcel() {
        BroadcastEventData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BroadcastEventData parceledData = BroadcastEventData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}