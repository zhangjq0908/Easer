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

package ryey.easer.plugins.event.dayofweek;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

import static ryey.easer.plugins.event.dayofweek.DayOfWeekEventPlugin.pname;

public class DayOfWeekEventData extends TypedEventData {

    Set<Integer> days;

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public DayOfWeekEventData() {}

    public DayOfWeekEventData(Set<Integer> days, EventType type) {
        this.days = days;
        setType(type);
    }

    @Override
    public Object get() {
        return days;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof Set) {
            days = (Set<Integer>) obj;
        } else {
            throw new RuntimeException("illegal data type");
        }
    }

    @Override
    public boolean isValid() {
        if (days == null)
            return false;
        if (days.isEmpty())
            return false;
        return true;
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return DayOfWeekEventPlugin.class;
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.readSingleSituation(parser);
        try {
            set(Utils.str2set(str_data));
            EventType type = XmlHelper.readLogic(parser);
            setType(type);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalXmlException(String.format("Illegal Event: illegal time format %s", str_data));
        }
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (days != null) {
            XmlHelper.writeSingleSituation(serializer, pname(), Utils.set2str(days));
            XmlHelper.writeLogic(serializer, type());
        }
    }
}
