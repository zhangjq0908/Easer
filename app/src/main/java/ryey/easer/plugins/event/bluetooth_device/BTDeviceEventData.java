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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.EnumSet;

import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;
import ryey.easer.commons.plugindef.eventplugin.EventPlugin;
import ryey.easer.commons.plugindef.eventplugin.EventType;
import ryey.easer.plugins.event.TypedEventData;

import static ryey.easer.plugins.event.bluetooth_device.BTDeviceEventPlugin.pname;


public class BTDeviceEventData extends TypedEventData {
    String hardware_address = null;

    {
        default_type = EventType.is;
        availableTypes = EnumSet.of(EventType.is, EventType.is_not);
    }

    public BTDeviceEventData() {}

    public BTDeviceEventData(String hardware_address, EventType type) {
        this.hardware_address = hardware_address;
        setType(type);
    }

    @Override
    public Object get() {
        return hardware_address;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            hardware_address = (String) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (hardware_address == null)
            return false;
        return true;
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return BTDeviceEventPlugin.class;
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.EventHelper.readSingleSituation(parser);
        set(str_data);
        EventType type = XmlHelper.EventHelper.readLogic(parser);
        setType(type);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        String wifi = (String) get();
        if (wifi != null) {
            XmlHelper.EventHelper.writeSingleSituation(serializer, pname(), wifi);
            XmlHelper.EventHelper.writeLogic(serializer, type());
        }
    }
}
