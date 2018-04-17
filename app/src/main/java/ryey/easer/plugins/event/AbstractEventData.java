package ryey.easer.plugins.event;

import android.support.annotation.NonNull;

import ryey.easer.commons.plugindef.eventplugin.EventData;

public abstract class AbstractEventData implements EventData {

    @Override
    public boolean match(@NonNull Object obj) {
        return equals(obj);
    }

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractEventData))
            return false;
        return true;
    }
}
