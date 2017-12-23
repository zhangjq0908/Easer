package ryey.easer.plugins.event;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public abstract class TypedEventData implements EventData {
    protected Set<EventType> availableTypes;
    protected EventType default_type;
    private EventType type = null;

    @Override
    public void setType(@NonNull EventType type) {
        if (!isAvailable(type)) {
            Logger.e("Attempt to set improper EventType (available [%s], attempt [%s])", availableTypes(), type);
            throw new IllegalArgumentException("Improper EventType to set");
        }
        this.type = type;
    }

    @NonNull
    @Override
    public EventType type() {
        if (type != null)
            return type;
        else
            return default_type;
    }

    @NonNull
    @Override
    public Set<EventType> availableTypes() {
        return availableTypes;
    }

    @Override
    public boolean isAvailable(@NonNull EventType type) {
        return availableTypes().contains(type);
    }

    @Override
    public boolean match(@NonNull Object obj) {
        return equals(obj);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TypedEventData))
            return false;
        if (type() != ((TypedEventData) obj).type())
            return false;
        return true;
    }
}
