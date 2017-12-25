package ryey.easer.plugins.event.nfc_tag;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class NfcTagEventDataFactory implements EventDataFactory<NfcTagEventData> {
    @NonNull
    @Override
    public Class<NfcTagEventData> dataClass() {
        return NfcTagEventData.class;
    }

    @NonNull
    @Override
    public NfcTagEventData emptyData() {
        return new NfcTagEventData();
    }

    @NonNull
    @Override
    public NfcTagEventData dummyData() {
        NfcTagEventData dummyData = new NfcTagEventData();
        dummyData.id = new byte[]{1, 47, 92, 63};
        return dummyData;
    }

    @NonNull
    @Override
    public NfcTagEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new NfcTagEventData(data, format, version);
    }
}
