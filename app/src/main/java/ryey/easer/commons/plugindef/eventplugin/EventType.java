package ryey.easer.commons.plugindef.eventplugin;

import android.content.Context;

import ryey.easer.R;

public enum EventType {
    after,
    any,
    before,
    is,
    is_not,
    none,
    ;
    public String desc(Context context) {
        String []types = context.getResources().getStringArray(R.array.event_type);
        return types[ordinal()];
    }
}
