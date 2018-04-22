package ryey.easer.plugins.event.sms;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class SmsEventDataFactory implements EventDataFactory<SmsEventData> {
    @NonNull
    @Override
    public Class<SmsEventData> dataClass() {
        return SmsEventData.class;
    }

    @ValidData
    @NonNull
    @Override
    public SmsEventData dummyData() {
        SmsInnerData innerData = new SmsInnerData();
        innerData.sender = "15077707777";
        innerData.content = "aaa";
        return new SmsEventData(innerData);
    }

    @ValidData
    @NonNull
    @Override
    public SmsEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsEventData(data, format, version);
    }
}
