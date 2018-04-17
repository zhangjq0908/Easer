package ryey.easer.core.data;

import android.support.annotation.NonNull;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.plugindef.ValidData;
import ryey.easer.commons.plugindef.eventplugin.EventData;

public class ScenarioStructure implements Named, Verifiable, WithCreatedVersion {
    private final int createdVersion;

    private final String name;
    private EventData eventData;

    public static ScenarioStructure createTmpScenario(@ValidData EventData eventData) {
        return new ScenarioStructure(eventData);
    }

    private ScenarioStructure(@ValidData EventData eventData) {
        createdVersion = C.VERSION_CREATED_IN_RUNTIME;
        name = null;
        this.eventData = eventData;
    }

    public ScenarioStructure(int createdVersion, @NonNull String name, @ValidData EventData eventData) {
        this.createdVersion = createdVersion;
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
        return name == null;
    }

    @Override
    public boolean isValid() {
        if ((name != null) && (name.isEmpty()))
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

    @Override
    public int createdVersion() {
        return createdVersion;
    }
}
