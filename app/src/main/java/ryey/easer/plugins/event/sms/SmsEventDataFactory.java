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

    @NonNull
    @Override
    public SmsEventData emptyData() {
        return new SmsEventData();
    }

    @ValidData
    @NonNull
    @Override
    public SmsEventData dummyData() {
        SmsEventData dummyData = new SmsEventData();
        SmsInnerData innerData = new SmsInnerData();
        innerData.sender = "15077707777";
        innerData.content = "aaa";
        dummyData.innerData = innerData;
        return dummyData;
    }

    @ValidData
    @NonNull
    @Override
    public SmsEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new SmsEventData(data, format, version);
    }
}
