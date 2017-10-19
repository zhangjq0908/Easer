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

package ryey.easer.plugins.event.bluetooth_device;

import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import ryey.easer.Utils;
import ryey.easer.commons.C;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

import static ryey.easer.plugins.event.bluetooth_device.BTDeviceEventPlugin.pname;


public class BTDeviceEventData extends TypedEventData {
    List<String> hwaddresses = new ArrayList<>();

    {
        default_type = EventType.any;
        availableTypes = EnumSet.of(EventType.any, EventType.none);
    }

    public BTDeviceEventData() {}

    public BTDeviceEventData(String hardware_address) {
        set(hardware_address);
    }

    public BTDeviceEventData(String hardware_address, EventType type) {
        set(hardware_address);
        setType(type);
    }

    @Override
    public Object get() {
        return hwaddresses;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            set(((String) obj).split("\n"));
        } else if (obj instanceof String[]) {
            for (String hardware_address : (String[]) obj) {
                if (!Utils.isBlank(hardware_address))
                    hwaddresses.add(hardware_address.trim());
            }
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public String toString() {
        String text = "";
        boolean is_first = true;
        for (String hwaddress : hwaddresses) {
            if (!is_first)
                text += "\n";
            text += hwaddress;
            is_first = false;
        }
        return text;
    }

    @Override
    public boolean isValid() {
        if (hwaddresses.size() == 0)
            return false;
        return true;
    }

    @Override
    public void parse(XmlPullParser parser, int version) throws IOException, XmlPullParserException, IllegalXmlException {
        if (version == C.VERSION_DEFAULT) {
            String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
            set(str_data);
        } else {
            set(XmlHelper.EventHelper.readMultipleSituation(parser));
        }
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        if (version == C.VERSION_DEFAULT) {
            if (type == EventType.is)
                type = EventType.any;
            else if (type == EventType.is_not)
                type = EventType.none;
        }
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        if (!isValid()) {
            Logger.wtf("Invalid data should not be serialized");
        }
        XmlHelper.EventHelper.writeMultipleSituation(serializer, pname(), hwaddresses.toArray(new String[0]));
        XmlHelper.EventHelper.writeLogic(serializer, type());
    }

    @Override
    public boolean match(Object obj) {
        if (obj instanceof String) {
            return hwaddresses.contains(((String) obj).trim());
        }
        return super.match(obj);
    }
}
