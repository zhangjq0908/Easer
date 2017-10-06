/*
 * Copyright (c) 2016 - 2017 Rui Zhao <renyuneyun@gmail.com>
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

package ryey.easer.plugins.event.time;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.EnumSet;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

import static ryey.easer.plugins.event.time.TimeEventPlugin.pname;

public class TimeEventData extends TypedEventData {
    private static SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

    private static String TimeToText(Calendar calendar) {
        return sdf_time.format(calendar.getTime());
    }

    private static Calendar TextToTime(String text) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf_time.parse(text));
        return calendar;
    }

    Calendar time = null;

    {
        default_type = EventType.after;
        availableTypes = EnumSet.of(EventType.after, EventType.is, EventType.is_not);
    }

    public TimeEventData() {}

    public TimeEventData(Calendar time, EventType type) {
        this.time = time;
        setType(type);
    }

    @Override
    public Object get() {
        return time;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Calendar)
            time = (Calendar) obj;
        else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (time == null)
            return false;
        return true;
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return TimeEventPlugin.class;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
        try {
            set(TextToTime(str_data));
            EventType type = XmlHelper.EventHelper.readLogic(parser);
            setType(type);
        } catch (ParseException e) {
            Logger.e(e, "Illegal Event: illegal time format %s", str_data);
            throw new IllegalXmlException(String.format("Illegal Event: illegal time format %s", str_data));
        }
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        Calendar time = (Calendar) get();
        if (time != null) {
            XmlHelper.EventHelper.writeSingleSituation(serializer, pname(), TimeToText(time));
            XmlHelper.EventHelper.writeLogic(serializer, type());
        }
    }
}
