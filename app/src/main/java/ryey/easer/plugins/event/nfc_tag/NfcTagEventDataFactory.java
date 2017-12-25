package ryey.easer.plugins.event.nfc_tag;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class NfcTagEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return NfcTagEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new NfcTagEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        NfcTagEventData dummyData = new NfcTagEventData();
        dummyData.id = new byte[]{1, 47, 92, 63};
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NfcTagEventData(data, format, version);
    }
}
