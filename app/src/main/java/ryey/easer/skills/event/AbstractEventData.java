package ryey.easer.skills.event;

import ryey.easer.commons.local_skill.eventskill.EventData;

public abstract class AbstractEventData implements EventData {

    @SuppressWarnings({"SimplifiableIfStatement", "RedundantIfStatement"})
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof AbstractEventData))
            return false;
        return true;
    }
}
