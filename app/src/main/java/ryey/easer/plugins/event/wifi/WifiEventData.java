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

package ryey.easer.plugins.event.wifi;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

import ryey.easer.commons.EventData;
import ryey.easer.commons.EventPlugin;
import ryey.easer.commons.IllegalXmlException;
import ryey.easer.commons.XmlHelper;

import static ryey.easer.plugins.event.wifi.WifiEventPlugin.pname;

public class WifiEventData implements EventData {
    String ssid = null;

    public WifiEventData() {}

    public WifiEventData(String ssid) {
        this.ssid = ssid;
    }

    @Override
    public Object get() {
        return ssid;
    }

    @Override
    public void set(Object obj) {
        if (obj instanceof String) {
            ssid = (String) obj;
        } else {
            throw new RuntimeException("illegal data");
        }
    }

    @Override
    public boolean isValid() {
        if (ssid == null)
            return false;
        return true;
    }

    @Override
    public Class<? extends EventPlugin> pluginClass() {
        return WifiEventPlugin.class;
    }

    @Override
    public void parse(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalXmlException {
        String str_data = XmlHelper.readSingleSituation(parser);
        set(str_data);
    }

    @Override
    public void serialize(XmlSerializer serializer) throws IOException {
        String wifi = (String) get();
        if (wifi != null) {
            XmlHelper.writeSingleSituation(serializer, pname(), wifi);
            XmlHelper.writeLogic(serializer);
        }
    }
}
