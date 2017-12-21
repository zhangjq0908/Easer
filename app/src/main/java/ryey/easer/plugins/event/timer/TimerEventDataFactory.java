package ryey.easer.plugins.event.timer;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class TimerEventDataFactory implements EventDataFactory {
    @NonNull
    @Override
    public Class<? extends EventData> dataClass() {
        return TimerEventData.class;
    }

    @NonNull
    @Override
    public EventData emptyData() {
        return new TimerEventData();
    }

    @NonNull
    @Override
    public EventData dummyData() {
        TimerEventData dummyData = new TimerEventData();
        TimerEventData.Timer timer = new TimerEventData.Timer();
        timer.exact = true;
        timer.repeat = true;
        timer.minutes = 102;
        dummyData.set(timer);
        return dummyData;
    }

    @NonNull
    @Override
    public EventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new TimerEventData(data, format, version);
    }
}
