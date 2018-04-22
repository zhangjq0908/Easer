package ryey.easer.plugins.operation.broadcast;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.operationplugin.OperationDataFactory;

class BroadcastOperationDataFactory implements OperationDataFactory<BroadcastOperationData> {
    @NonNull
    @Override
    public Class<BroadcastOperationData> dataClass() {
        return BroadcastOperationData.class;
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastOperationData dummyData() {
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
        return new BroadcastOperationData(intentData);
    }

    @ValidData
    @NonNull
    @Override
    public BroadcastOperationData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BroadcastOperationData(data, format, version);
    }
}
