package ryey.easer.plugins.event;

import ryey.easer.commons.local_plugin.eventplugin.EventData;

public abstract class AbstractEventData implements EventData {

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractEventData))
            return false;
        return true;
    }
}
