package ryey.easer.core.data;

import ryey.easer.Utils;
import ryey.easer.commons.plugindef.eventplugin.EventData;
import ryey.easer.core.data.storage.ScenarioDataStorage;

public class ScenarioStructure implements Named, Verifiable {

    private String name;
    private EventData eventData;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
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
