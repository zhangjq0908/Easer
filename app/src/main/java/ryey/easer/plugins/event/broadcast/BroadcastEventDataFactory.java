package ryey.easer.plugins.event.broadcast;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class BroadcastEventDataFactory implements EventDataFactory<BroadcastEventData> {
    @NonNull
    @Override
    public Class<BroadcastEventData> dataClass() {
        return BroadcastEventData.class;
    }

    @NonNull
    @Override
    public BroadcastEventData emptyData() {
        return new BroadcastEventData();
    }

    @NonNull
    @Override
    public BroadcastEventData dummyData() {
        ReceiverSideIntentData intentData = new ReceiverSideIntentData();
        intentData.action.add("action1");
        intentData.action.add("action2");
        intentData.category.add("category1");
        intentData.category.add("category2");
        BroadcastEventData dummyData = new BroadcastEventData(intentData);
        return dummyData;
    }

    @NonNull
    @Override
    public BroadcastEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new BroadcastEventData(data, format, version);
    }
}
