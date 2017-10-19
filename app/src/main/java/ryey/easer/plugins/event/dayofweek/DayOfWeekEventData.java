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

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.PluginRegistry;
import ryey.easer.plugins.event.TypedEventData;

public class DayOfWeekEventData extends TypedEventData {

    Set<Integer> days = new HashSet<>(7);

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public DayOfWeekEventData() {}

    public DayOfWeekEventData(Set<Integer> days) {
        this.days = days;
    }

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
        if (obj instanceof String) {
            set(((String) obj).split("\n"));
        } else if (obj instanceof String[]) {
            for (String str : (String[]) obj) {
                days.add(Integer.parseInt(str.trim()));
            }
        } else if (obj instanceof Set) {
            for (Object o : (Set) obj) {
                if (o instanceof Integer)
                    days.add((Integer) o);
                else
                    Logger.wtf("Data is Set but element <%s> is not Integer", o);
            }
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
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            try {
                set(Utils.str2set(str_data));
            } catch (ParseException e) {
                Logger.e(e, "Illegal Event: illegal time format %s", str_data);
                throw new IllegalXmlException(String.format("Illegal Event: illegal time format %s", str_data));
            }
        } else {
            set(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid DayOfWeekEventData shouldn't be serialized");
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, PluginRegistry.getInstance().event().findPlugin(this).name(),
                Utils.set2strlist(days).toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }
}
