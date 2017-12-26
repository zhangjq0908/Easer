package ryey.easer.plugins.event.headset;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class HeadsetEventDataFactory implements EventDataFactory<HeadsetEventData> {
    @NonNull
    @Override
    public Class<HeadsetEventData> dataClass() {
        return HeadsetEventData.class;
    }

    @NonNull
    @Override
    public HeadsetEventData emptyData() {
        return new HeadsetEventData();
    }

    @ValidData
    @NonNull
    @Override
    public HeadsetEventData dummyData() {
        return new HeadsetEventData(
                HeadsetEventData.HeadsetAction.plug_in,
                HeadsetEventData.HeadsetType.without_microphone);
    }

    @ValidData
    @NonNull
    @Override
    public HeadsetEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new HeadsetEventData(data, format, version);
    }
}
