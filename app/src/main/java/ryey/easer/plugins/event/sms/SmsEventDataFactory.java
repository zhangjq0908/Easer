package ryey.easer.plugins.event.sms;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class SmsEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return SmsEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new SmsEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        SmsEventData dummyData = new SmsEventData();
        SmsInnerData innerData = new SmsInnerData();
        innerData.sender = "15077707777";
        innerData.content = "aaa";
        dummyData.innerData = innerData;
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsEventData(data, format, version);
    }
}
