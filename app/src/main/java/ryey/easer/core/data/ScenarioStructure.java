package ryey.easer.core.data;

import android.support.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.storage.ScenarioDataStorage;

public class ScenarioStructure implements Named, Verifiable {

    private final String name;
    private EventData eventData;

    public ScenarioStructure(@NonNull String name, @ValidData EventData eventData) {
        this.name = name;
        this.eventData = eventData;
    }

    @Override
    public String getName() {
        return name;
    }

    public EventData getEventData() {
        return eventData;
    }

    public void setEventData(EventData eventData) {
        this.eventData = eventData;
    }

    public boolean isTmpScenario() {
        return ScenarioDataStorage.hasTmp(name);
    }

    @Override
    public boolean isValid() {
        if ((name == null) || (name.isEmpty()))
            return false;
        if ((eventData == null) || (!eventData.isValid()))
            return false;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ScenarioStructure))
            return false;
        if (!Utils.nullableEqual(name, ((ScenarioStructure) obj).name))
            return false;
        if (!Utils.nullableEqual(eventData, ((ScenarioStructure) obj).eventData))
            return false;
        return true;
    }
}
