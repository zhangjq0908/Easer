package ryey.easer.plugins.event.calendar;

import android.support.v4.util.ArraySet;

import java.util.Set;

class CalendarData {
    static final String[] condition_name = new String[]{
            "start",
            "end",
    };

    long calendar_id;
    Set<String> conditions = new ArraySet<>(condition_name.length);
}
