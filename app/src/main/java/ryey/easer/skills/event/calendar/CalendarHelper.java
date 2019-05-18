/*
 * Copyright (c) 2016 - 2018 Rui Zhao <renyuneyun@gmail.com>
 *
 * This file is part of Easer.
 *
 * Easer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Easer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easer.  If not, see <http://www.gnu.org/licenses/>.
 */

package ryey.easer.skills.event.calendar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.Calendar;

@SuppressLint("MissingPermission")
class CalendarHelper {

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
        String res;
        if (cur.moveToNext())
            res = cur.getString(1);
        else
            res = null;
        cur.close();
        return res;
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
        Long res;
        if (cur.moveToNext())
            res = cur.getLong(1);
        else
            res = null;
        cur.close();
        return res;
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
        Long res;
        if (cur.moveToNext())
            res = cur.getLong(2);
        else
            res = null;
        cur.close();
        return res;
    }
}
