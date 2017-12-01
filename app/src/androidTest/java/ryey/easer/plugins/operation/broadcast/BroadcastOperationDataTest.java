package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;
import android.os.Parcel;

import org.junit.Test;

import java.util.ArrayList;

import ryey.easer.plugins.operation.TestHelper;

import static org.junit.Assert.*;

public class BroadcastOperationDataTest {

    public static BroadcastOperationData createDummyData() {
        BroadcastOperationData dummyData = new BroadcastOperationData();
        IntentData intentData = new IntentData();
        intentData.action = "testAction";
        intentData.category = new ArrayList<>();
        intentData.category.add("testCategory");
        intentData.type = "myType";
        intentData.data = Uri.parse("myprot://seg1/seg2");
        intentData.extras = new ArrayList<>();
        IntentData.ExtraItem extraItem = new IntentData.ExtraItem();
        extraItem.key = "extra_key1";
        extraItem.value = "extra_value1";
        extraItem.type = "string";
        intentData.extras.add(extraItem);
        return dummyData;
    }

    @Test
    public void testParcel() {
        BroadcastOperationData dummyData = createDummyData();
        Parcel parcel = TestHelper.writeToParcel(dummyData);
        BroadcastOperationData parceledData = BroadcastOperationData.CREATOR.createFromParcel(parcel);
        assertEquals(dummyData, parceledData);
    }

}