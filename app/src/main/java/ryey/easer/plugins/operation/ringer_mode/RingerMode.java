package ryey.easer.plugins.operation.ringer_mode;

import android.os.Build;

enum RingerMode {
    normal,
    vibrate,
    silent,

    //Do Not Disturb mode
    dnd_all,
    dnd_priority,
    dnd_none,
    dnd_alarms,
    ;

    static RingerMode compatible(RingerMode mode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            switch (mode) {
                case dnd_all:
                case dnd_priority:
                case dnd_none:
                case dnd_alarms:
                    mode = silent;
            }
        } else {
            if (mode == silent)
                mode = dnd_priority;
            else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (mode == dnd_alarms)
                    mode = dnd_priority;
            }
        }
        return mode;
    }
}
