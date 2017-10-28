package ryey.easer.plugins.event.calendar;

import android.support.v4.util.ArrayMap;

import java.util.Map;

class CalendarData {
    static final String[] condition_name = new String[]{
            "start",
            "end",
    };

    long calendar_id;
    Map<String, Boolean> conditions = new ArrayMap<>(condition_name.length);

    {
        for (String key : condition_name) {
            conditions.put(key, false);
        }
    }
}
