package ryey.easer.plugins.event;

import com.orhanobut.logger.Logger;

import java.util.Set;

import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.commons.plugindef.eventplugin.EventType;

public abstract class TypedEventData implements EventData {
    protected Set<EventType> availableTypes;
    protected EventType type = null, default_type = null;

    @Override
    public void setType(EventType type) {
        if (type == null) {
            Logger.w("got invalid type. fallback to the default type: %s", default_type);
            this.type = default_type;
        } else {
            if (!isAvailable(type)) {
                Logger.e("Attempt to set improper EventType (available [%s], attempt [%s])", availableTypes(), type);
                throw new IllegalArgumentException("Improper EventType to set");
            }
            this.type = type;
        }
    }

    @Override
    public EventType type() {
        if (type != null)
            return type;
        else
            return default_type;
    }

    @Override
    public Set<EventType> availableTypes() {
        return availableTypes;
    }

    @Override
    public boolean isAvailable(EventType type) {
        return availableTypes().contains(type);
    }

    @Override
    public boolean match(Object obj) {
        return equals(obj);
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;
        TypedEventData data = (TypedEventData) obj;
        if (type() != data.type())
            return false;
        if (!get().equals(data.get()))
            return false;
        return true;
    }
}
