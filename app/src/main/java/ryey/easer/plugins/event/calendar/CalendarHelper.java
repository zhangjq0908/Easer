package ryey.easer.plugins.event.calendar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("MissingPermission")
class CalendarHelper {

    private final static String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };

    private final static int PROJECTION_ID_INDEX = 0;
    private final static int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private final static int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private final static int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    static String getCalendarName(ContentResolver contentResolver, long calendar_id) {
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String[] PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
        };
        String SELECTION = "((" + CalendarContract.Calendars._ID + " = ?))";
        String[] ARGS = new String[]{
                String.valueOf(calendar_id),
        };
        Cursor cur = contentResolver.query(uri, PROJECTION, SELECTION, ARGS, null);
        if (cur == null)
            return null;
        if (cur.moveToNext())
            return cur.getString(1);
        else
            return null;
    }

    static List<CalendarPluginViewFragment.CalendarWrapper> getCalendars(ContentResolver contentResolver) {
        List<CalendarPluginViewFragment.CalendarWrapper> calendars = new ArrayList<>();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Cursor cur = contentResolver.query(uri, EVENT_PROJECTION, null, null, null);
        while (cur.moveToNext()) {
            long calID = cur.getLong(PROJECTION_ID_INDEX);
            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            String accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            String ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            Logger.d("id: <%d> displayName: <%s> accountName: <%s> ownerName: <%s>",
                    calID, displayName, accountName, ownerName);

            CalendarPluginViewFragment.CalendarWrapper wrapper = new CalendarPluginViewFragment.CalendarWrapper();
            wrapper.id = calID;
            wrapper.name = displayName;
            calendars.add(wrapper);
        }
        return calendars;
    }

    static Long nextEvent_start(ContentResolver contentResolver, long calendar_id) {
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Events._ID,                           // 0
                CalendarContract.Events.DTSTART,                  // 1
                CalendarContract.Events.DTEND,         // 2
        };
        Calendar calendar = Calendar.getInstance();
        long current_time = calendar.getTimeInMillis();

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?)" +
                " AND (" + CalendarContract.Events.DTSTART + " > ?)" +
                ")";
        String[] selectionArgs = new String[] {
                String.valueOf(calendar_id),
                String.valueOf(current_time),
        };
        Cursor cur = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs,
                CalendarContract.Events.DTSTART + " ASC");
        if (cur == null)
            return null;
        if (cur.moveToNext())
            return cur.getLong(1);
        else
            return null;
    }

    static Long nextEvent_end(ContentResolver contentResolver, long calendar_id) {
        final String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Events._ID,                           // 0
                CalendarContract.Events.DTSTART,                  // 1
                CalendarContract.Events.DTEND,         // 2
        };
        Calendar calendar = Calendar.getInstance();
        long current_time = calendar.getTimeInMillis();

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?)" +
                " AND (" + CalendarContract.Events.DTEND + " > ?)" +
                ")";
        String[] selectionArgs = new String[] {
                String.valueOf(calendar_id),
                String.valueOf(current_time),
        };
        Cursor cur = contentResolver.query(uri, EVENT_PROJECTION, selection, selectionArgs,
                CalendarContract.Events.DTEND + " ASC");
        if (cur == null)
            return null;
        if (cur.moveToNext())
            return cur.getLong(2);
        else
            return null;
    }
}
