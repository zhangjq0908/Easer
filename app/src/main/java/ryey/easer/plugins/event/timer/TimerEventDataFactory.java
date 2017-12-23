package ryey.easer.plugins.event.timer;

import android.support.annotation.NonNull;

import ryey.easer.commons.C;
import ryey.easer.commons.IllegalStorageDataException;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventDataFactory;

class TimerEventDataFactory implements EventDataFactory<TimerEventData> {
    @NonNull
    @Override
    public Class<TimerEventData> dataClass() {
        return TimerEventData.class;
    }

    @NonNull
    @Override
    public TimerEventData emptyData() {
        return new TimerEventData();
    }

    @NonNull
    @Override
    public TimerEventData dummyData() {
        TimerEventData dummyData = new TimerEventData();
        TimerEventData.Timer timer = new TimerEventData.Timer();
        timer.exact = true;
        timer.repeat = true;
        timer.minutes = 102;
        dummyData.timer = timer;
        return dummyData;
    }

    @NonNull
    @Override
    public TimerEventData parse(@NonNull String data, @NonNull C.Format format, int version) throws IllegalStorageDataException {
        return new TimerEventData(data, format, version);
    }
}
